package scrappers.vk.data.repository.region

import scrappers.vk.domain.model.City
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import scrappers.vk.domain.model.RegionType

interface RegionRepository {
    fun saveCountries(countries: List<Country>)
    fun getCountries(): List<Country>
    fun getCountryById(id: Int): Country?

    fun saveRegions(country: Country, regions: List<Region>) : List<Region>
    fun getRegions(country: Country): List<Region>
    fun getRegionById(regionId: Int): Region


    fun saveCity(region: Region, city: List<City>)
    fun getCities(region: Region): List<City>

    fun getRegionTypes(): List<RegionType>
}