package scrappers.vk.data.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.data.database.dao.RegionHistoryDao
import scrappers.vk.data.database.entity.*
import scrappers.vk.data.database.entity.CityModel.region_id
import scrappers.vk.domain.model.*
import java.sql.Connection
import java.util.*
import kotlin.NoSuchElementException

object CountriesDatabase : CountriesDao, RegionHistoryDao {

    lateinit var database: Database

    private val regionTypeList = mutableListOf<RegionType>()

    init {
        try {
            database = Database.connect("jdbc:sqlite:countries.db", "org.sqlite.JDBC")
            TransactionManager.manager.defaultIsolationLevel =
                Connection.TRANSACTION_SERIALIZABLE // Or Connection.TRANSACTION_READ_UNCOMMITTED

            createTable()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun saveCountries(countries: List<Country>, needUpdate: Boolean) {

        transaction {
            for (country in countries) {
                if (needUpdate && hasRegionInDbById(country.id)) {
                    CountryModel.update({ CountryModel.id eq country.id }) {
                        it[name] = country.name
                    }
                } else {
                    CountryModel.insert {
                        it[uuid] = country.uuid
                        it[id] = country.id
                        it[name] = country.name
                        it[updateTime] = System.currentTimeMillis() / 1000
                    }
                }
            }
        }

    }

    private fun hasRegionInDbById(id: Int): Boolean {
        return !RegionModel.select {
            RegionModel.id.eq(id)
        }.empty()
    }

    private fun hasCityInDbById(id: Int): Boolean {
        return !CityModel.select {
            CityModel.id.eq(id)
        }.empty()
    }

    private fun updateCountries(countries: List<Country>) {

    }

    /**
     * Получение списка всех стран из БД
     */
    override fun getCountries(): List<Country> {
        val countries = mutableListOf<Country>()
        transaction {

        }

        return countries
    }

    override fun getCountryById(id: Int): Country? {
        var country: Country? = null

        transaction {
            val result = CountryModel.select({ CountryModel.id eq id }).single()
            result?.let {
                country = Country(result[CountryModel.uuid], result[CountryModel.id], result[CountryModel.name])
            }
        }

        return country
    }

    override fun getRegionById(id: Int): Region {
        var region: Region? = null

        transaction {
            val res = RegionModel.select { RegionModel.id eq id }.single()
            getCountryById(res[CountryModel.id])?.let {
                region = Region(
                    res[RegionModel.uuid], it, res[RegionModel.id],
                    res[RegionModel.name]
                )
            }
        }

        return region ?: throw NoSuchElementException()
    }

    override fun getRegions(country: Country): List<Region> {
        val regions = mutableListOf<Region>()
        transaction {
            RegionModel.select({ RegionModel.country eq country.id }).forEach {
                regions.add(Region(it[RegionModel.uuid], country, it[RegionModel.id], it[RegionModel.name]))
            }
        }

        return regions
    }


    override fun saveRegions(country: Country, regions: List<Region>, needUpdate: Boolean): List<Region> {
        transaction {
            for (reg in regions) {
                if (needUpdate && hasRegionInDbById(reg.id)) {
                    RegionModel.update({ RegionModel.id eq reg.id }) {
                        it[name] = reg.name
                        it[this.country] = reg.country.id
                    }
                } else {
                    RegionModel.insert {
                        it[uuid] = reg.uuid
                        it[id] = reg.id
                        it[name] = reg.name
                        it[updateTime] = System.currentTimeMillis() / 1000
                        it[this.country] = reg.country.id
                    }
                }

            }
        }

        return getRegions(country)
    }

    override fun saveCities(region: Region?, cities: List<City>) {
        transaction {
            for (reg in cities) {
                if (hasCityInDbById(reg.id)) {
                    CityModel.update({ CityModel.id eq reg.id }) {
                        it[name] = reg.name
                        it[region_id] = reg.region?.uuid
                    }
                } else {
                    CityModel.insert {
                        it[uuid] = reg.uuid
                        it[id] = reg.id
                        it[name] = reg.name
                        it[region_id] = reg.region?.uuid
                        it[area] = reg.area
                        it[this.region] = reg.regionName
                        it[updateTime] = System.currentTimeMillis() / 1000
                    }
                }

            }
        }
    }
}