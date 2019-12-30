package scrappers.vk.data.repository

import scrappers.vk.data.database.CountriesDatabase
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import scrappers.vk.domain.model.RegionType

object CountryRepositoryInstance: CountryRepository {

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

    override fun saveRegions(country: Country, regions: List<Region>) {
        dao.saveRegions(country, regions)
    }

    override fun getRegions(country: Country): List<Region> {
        return listOf()
    }

    override fun saveCity(region: Region, city: List<City>) {

    }

    override fun getCities(region: Region): List<City> {
        return listOf()
    }

    override fun getRegionTypes(): List<RegionType> {
        return dao.getRegionTypes()
    }
}