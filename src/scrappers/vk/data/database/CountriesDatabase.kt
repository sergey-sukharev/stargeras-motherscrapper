package scrappers.vk.data.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.data.database.entity.createTable
import scrappers.vk.domain.model.Country
import java.sql.Connection

object CountriesDatabase: CountriesDao {

    private lateinit var database: Database

    init {
        try {
            database = Database.connect("jdbc:sqlite:countries.db", "org.sqlite.JDBC")
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE // Or Connection.TRANSACTION_READ_UNCOMMITTED

            createTable()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun saveCountries(countries: List<Country>) {

    }


}