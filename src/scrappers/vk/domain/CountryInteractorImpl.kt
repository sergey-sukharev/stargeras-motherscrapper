package scrappers.vk.domain

import com.vk.api.sdk.actions.Database
import com.vk.api.sdk.client.actors.UserActor
import scrappers.vk.data.repository.CountryRepository
import scrappers.vk.data.repository.CountryRepositoryInstance
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import java.util.*

class CountryInteractorImpl(val databaseClient: Database, val actor: UserActor) : CountryInteractor {

    private val repository: CountryRepository

    init {
        repository = CountryRepositoryInstance
    }

    override fun loadCountries(): List<Country> {

        val countriesModelList = mutableListOf<Country>()
        getCountryListFromVk(countriesModelList)
//        countriesModelList.add(Country(UUID.randomUUID().toString(),
//            1, "Россия"))
//        countriesModelList.add(Country(UUID.randomUUID().toString(),
//            2, "USA"))
        repository.saveCountries(countriesModelList)

        return repository.getCountries()
    }

    private fun getCountryListFromVk(
        countriesModelList: MutableList<Country>,
        count: Int = 100, offset: Int = 0
    ) {
        val responseBuilder = databaseClient.getCountries(actor).apply {
            needAll(true)
            count(count)
            offset(offset)
        }

        val countriesResultExecutor = responseBuilder.execute()
        val countriesResultList = countriesResultExecutor.items
        countriesResultList.forEach {
            countriesModelList.add(
                Country(
                    UUID.randomUUID().toString(),
                    it.id, it.title
                )
            )
        }

        if (countriesResultExecutor.count > countriesModelList.size)
            getCountryListFromVk(countriesModelList, count, offset + count)
    }

    override fun loadRegions(countryId: Int, count: Int, offset: Int) {
        val regionModelList = mutableListOf<Region>()
        val country = repository.getCountryById(countryId)
        country?.let {
            getRegionListFromVk(regionModelList, it, count, offset)
            repository.saveRegions(it, regionModelList)
        }
    }

    private fun getRegionListFromVk(
        regionModelList: MutableList<Region>, country: Country,
        count: Int = 1000, offset: Int = 0
    ) {
        val responseBuilder = databaseClient.getRegions(actor, country.id).apply {
            count(count)
            offset(offset)
        }

        val countriesResultExecutor = responseBuilder.execute()
        val countriesResultList = countriesResultExecutor.items
        countriesResultList.forEach {
            regionModelList.add(
                Region(
                    UUID.randomUUID().toString(),
                    country,
                    it.id,
                    it.title
                )
            )
        }

        if (countriesResultExecutor.count > regionModelList.size)
            getRegionListFromVk(regionModelList, country, count, offset + count)
    }

    override fun loadCities(region: Int, count: Int, offset: Int) {

    }
}