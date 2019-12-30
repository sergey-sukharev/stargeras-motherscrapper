package scrappers.vk.data.database.dao

import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.RegionType

interface CountriesDao {
    fun saveCountries(countries: List<Country>)
    fun getCountries(): List<Country>

    fun getRegionTypes(): List<RegionType>
}