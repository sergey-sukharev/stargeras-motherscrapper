package scrappers.vk.data.repository.region

import scrappers.vk.data.database.CountriesDatabase
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import scrappers.vk.domain.model.RegionType

object RegionRepositoryInstance: RegionRepository {

    private val dao: CountriesDao = CountriesDatabase

    override fun saveCountries(countries: List<Country>) {
        println("Need saving ${countries.size} countries")
        dao.saveCountries(countries)
    }

    override fun getCountries(): List<Country> {
        return dao.getCountries()
    }

    override fun getCountryById(id: Int): Country? {
        return dao.getCountryById(id)
    }

    override fun saveRegions(country: Country, regions: List<Region>) : List<Region> {
        return dao.saveRegions(country, regions)
    }

    override fun getRegions(country: Country): List<Region> {
        return dao.getRegions(country)
    }

    override fun getRegionById(regionId: Int): Region {
        return dao.getRegionById(regionId)
    }

    override fun saveCity(region: Region, city: List<City>) {
        dao.saveCities(region, city)
    }

    override fun getCities(region: Region): List<City> {
        return listOf()
    }

    override fun getRegionTypes(): List<RegionType> {
        return dao.getRegionTypes()
    }
}