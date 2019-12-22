package scrappers.vk.data.database.dao

import scrappers.vk.domain.model.Country

interface CountriesDao {
    fun saveCountries(countries: List<Country>)
}