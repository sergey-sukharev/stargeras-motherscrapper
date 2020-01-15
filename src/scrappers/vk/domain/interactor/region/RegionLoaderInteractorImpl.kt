package scrappers.vk.domain.interactor.region

import scrappers.vk.data.repository.RegionNotFoundException
import scrappers.vk.data.repository.history.RegionHistoryRepoInstance
import scrappers.vk.data.repository.history.RegionHistoryRepository
import scrappers.vk.data.repository.region.RegionRepository
import scrappers.vk.data.repository.region.RegionRepositoryInstance
import scrappers.vk.domain.VkRegionLoader
import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import scrappers.vk.domain.model.RegionHistory

class RegionLoaderInteractorImpl : RegionLoaderInteractor {

    private val repository: RegionRepository
    private val updateRepository: RegionHistoryRepository
    private val regionLoader: VkRegionLoader

    init {
        repository = RegionRepositoryInstance
        updateRepository = RegionHistoryRepoInstance
        regionLoader = VkRegionLoader(repository, updateRepository)
    }

    override fun loadCountries(): List<Country> {
        val countriesModelList = mutableListOf<Country>()
        regionLoader.loadCountries(countriesModelList)
        repository.saveCountries(countriesModelList)
        return repository.getCountries()
    }

    override fun loadRegions(countryId: Int, count: Int, offset: Int) {
        val regionModelList = mutableListOf<Region>()
        val country = repository.getCountryById(countryId)

        country?.let {
            regionLoader.loadRegions(it, regionModelList, count, offset)
        }
    }

    override fun loadCities(regionId: Int, count: Int, offset: Int) {
        val region = repository.getRegionById(regionId)

    }

    /**
     *  Глубокое обновление городов по стране
     */
    override fun deepLoadByCountry(id: Int, fullUpdate: Boolean) {
        val country = repository.getCountryById(id) ?: throw RegionNotFoundException()
        var countryHistory = updateRepository.getHistory(country.id)

        countryHistory ?: loadRegions(country.id)

        countryHistory = updateRepository.getHistory(country.id)

        val regions = repository.getRegions(country)
        val regionsHistory = updateRepository.getHistoryAll().map { it.id to it }.toMap()

        for (region in regions) {
            if (regionsHistory.get(region.id) == null || !regionsHistory.get(region.id)!!.isLoaded) {
                val citiesList = mutableListOf<City>()
                val count = regionLoader.loadCities(region, citiesList)
                repository.saveCity(region, citiesList)
                updateRepository.saveHistory(
                    RegionHistory(region.id, region.uuid,
                    count, citiesList.size, true, System.currentTimeMillis()/1000)
                )
            }

            Thread.sleep(2000)
        }
    }

    override fun loadRegionCities() {
        val citiesRegions = mutableListOf<City>()
        regionLoader.loadRegionCities(citiesRegions)
        try {
            repository.saveCity(null, citiesRegions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}