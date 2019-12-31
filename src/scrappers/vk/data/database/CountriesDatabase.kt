package scrappers.vk.data.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.data.database.dao.RegionHistoryDao
import scrappers.vk.data.database.entity.RegionModel
import scrappers.vk.data.database.entity.RegionTypeEntity
import scrappers.vk.data.database.entity.createTable
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
            loadTypes()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun saveCountries(countries: List<Country>) {
        val regionTypes = getRegionTypes().map { it.name to it.id }.toMap()
        val regionId = regionTypes.get("region") ?: throw NullPointerException()

        transaction {
            for (country in countries) {
                if (hasRegionInDbById(country.id)) {
                    RegionModel.update({ RegionModel.id eq country.id }) {
                        it[name] = country.name
                    }
                } else {
                    RegionModel.insert {
                        it[uuid] = country.uuid
                        it[id] = country.id
                        it[name] = country.name
                        it[regionType] = regionId
                        it[region] = country.id
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
            val result = RegionModel.select({ RegionModel.id eq id }).single()
            result?.let {
                country = Country(result[RegionModel.uuid], result[RegionModel.id], result[RegionModel.name])
            }
        }

        return country
    }

    override fun getRegionById(id: Int): Region {
        var region: Region? = null

        transaction {
            val res = RegionModel.select { RegionModel.id eq id }.single()
            getCountryById(res[RegionModel.region])?.let {
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
            RegionModel.select({RegionModel.region eq country.id}).forEach {
                regions.add(Region(it[RegionModel.uuid], country, it[RegionModel.id], it[RegionModel.name]))
            }
        }

        return regions
    }

    override fun getRegionTypes(): List<RegionType> {

        if (regionTypeList.size > 0) return regionTypeList

        transaction {
            for (regionType in RegionTypeEntity.selectAll()) {
                regionTypeList.add(
                    RegionType(
                        regionType[RegionTypeEntity.uuid],
                        regionType[RegionTypeEntity.name]
                    )
                )
            }
        }

        return regionTypeList
    }

    /**
     * Предзагрузка регион-типов
     */
    private fun loadTypes() {

        transaction {
            RegionTypeEntity.insertIgnore {
                it[name] = "region"
                it[uuid] = UUID.randomUUID().toString()
            }
            RegionTypeEntity.insertIgnore {
                it[name] = "region"
                it[uuid] = UUID.randomUUID().toString()
            }
            RegionTypeEntity.insertIgnore {
                it[name] = "city"
                it[uuid] = UUID.randomUUID().toString()
            }
        }
    }


    override fun saveRegions(country: Country, regions: List<Region>) {
        val regionTypes = getRegionTypes().map { it.name to it.id }.toMap()
        val regionId = regionTypes.get("region") ?: throw NullPointerException()

        transaction {
            for (reg in regions) {
                if (hasRegionInDbById(reg.id)) {
                    RegionModel.update({ RegionModel.id eq reg.id }) {
                        it[name] = reg.name
                        it[region] = reg.country.id
                    }
                } else {
                    RegionModel.insert {
                        it[uuid] = reg.uuid
                        it[id] = reg.id
                        it[name] = reg.name
                        it[regionType] = regionId
                        it[region] = reg.country.id
                    }
                }

            }
        }

    }

    override fun saveCities(region: Region, cities: List<City>) {
        val regionTypes = getRegionTypes().map { it.name to it.id }.toMap()
        val regionId = regionTypes.get("city") ?: throw NullPointerException()

        transaction {
            for (reg in cities) {
                if (hasRegionInDbById(reg.id)) {
                    RegionModel.update({ RegionModel.id eq reg.id }) {
                        it[name] = reg.name
                        it[RegionModel.region] = reg.region.id
                    }
                } else {
                    RegionModel.insert {
                        it[uuid] = reg.uuid
                        it[id] = reg.id
                        it[name] = reg.name
                        it[regionType] = regionId
                        it[RegionModel.region] = reg.region.id
                        it[RegionModel.region] = reg.region.id
                        it[area] = reg.area
                        it[regionName] = reg.regionName
                    }
                }

            }
        }
    }
}