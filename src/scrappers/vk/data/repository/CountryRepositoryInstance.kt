package scrappers.vk.data.repository

import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region

object CountryRepositoryInstance: CountryRepository {

    override fun saveCountries(countries: List<Country>) {
        println("Need saving ${countries.size} countries")
    }

    override fun getCountries(): List<Country> {
        return listOf();
    }

    override fun saveRegions(country: Country, regions: List<Region>) {

    }

    override fun getRegions(country: Country): List<Region> {
        return listOf()
    }

    override fun saveCity(region: Region, city: List<City>) {

    }

    override fun getCities(region: Region): List<City> {
        return listOf()
    }
}