package scrappers.vk.domain.interactor.region

import scrappers.vk.data.repository.region.RegionRepository
import scrappers.vk.data.repository.region.RegionRepositoryInstance
import scrappers.vk.domain.VkRegionLoader
import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region

class RegionLoaderInteractorImpl : RegionLoaderInteractor {

    private val repository: RegionRepository

    init {
        repository = RegionRepositoryInstance
    }

    override fun loadCountries(): List<Country> {
        val countriesModelList = mutableListOf<Country>()
        VkRegionLoader.loadCountries(countriesModelList)
        repository.saveCountries(countriesModelList)
        return repository.getCountries()
    }

    override fun loadRegions(countryId: Int, count: Int, offset: Int) {
        val regionModelList = mutableListOf<Region>()
        val country = repository.getCountryById(countryId)
        country?.let {
            VkRegionLoader.loadRegions(it, regionModelList, count, offset)
            repository.saveRegions(it, regionModelList)
        }
    }

    override fun loadCities(regionId: Int, count: Int, offset: Int) {
        val region = repository.getRegionById(regionId)
    }

    override fun deepLoadByCountry(id: Int) {

    }

}