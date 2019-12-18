package scrappers.vk.data.repository

import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region

interface CountryRepository {
    fun saveCountries(countries: List<Country>)
    fun getCountries(): List<Country>
    fun saveRegions(country: Country, regions: List<Region>)
    fun getRegions(country: Country): List<Region>
    fun saveCity(region: Region, city: List<City>)
    fun getCities(region: Region): List<City>
}