package scrappers.vk.domain

import scrappers.vk.domain.model.Country

interface CountryInteractor {
    fun loadCountries(): List<Country>
    fun loadRegions(country: Int, count: Int = 1000, offset: Int = 0)
    fun loadCities(region: Int, count: Int = 1000, offset: Int = 0)
}