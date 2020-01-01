package scrappers.vk.data.database.dao

import scrappers.vk.data.database.entity.RegionModel
import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import scrappers.vk.domain.model.RegionType

interface CountriesDao {
    fun saveCountries(countries: List<Country>)
    fun getCountries(): List<Country>
    fun getCountryById(id: Int): Country?
    fun getRegionTypes(): List<RegionType>
    fun saveRegions(country: Country, regions: List<Region>): List<Region>
    fun getRegionById(id: Int): Region
    fun getRegions(country: Country): List<Region>
    fun saveCities(region: Region, city: List<City>)
}