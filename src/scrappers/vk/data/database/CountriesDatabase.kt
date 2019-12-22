package scrappers.vk.data.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.BatchInsertStatement
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import scrappers.vk.data.database.dao.CountriesDao
import scrappers.vk.data.database.entity.CountryModel
import scrappers.vk.data.database.entity.createTable
import scrappers.vk.domain.model.Country
import java.sql.Connection

object CountriesDatabase : CountriesDao {

    private lateinit var database: Database

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

    override fun saveCountries(countries: List<Country>) {
        transaction {
            CountryModel.batchInsertOnDuplicateKeyUpdate( countries,
                listOf(CountryModel.id, CountryModel.name, CountryModel.updateTime) ) { batch, country ->
                batch[id] = country.id
                batch[uuid] = country.uuid
                batch[name] = country.name
                batch[updateTime] = System.currentTimeMillis() / 1000
            }
//            for (country in countries) {
//                CountryModel.batchInsertOnDuplicateKeyUpdate {
//                    it[uuid] = country.uuid
//                    it[id] = country.id
//                    it[name] = country.name
//                    it[updateTime] = System.currentTimeMillis() / 1000
//                }
//            }
        }
    }

    /**
     * Получение списка всех стран из БД
     */
    override fun getCountries(): List<Country> {
        val countries = mutableListOf<Country>()
        transaction {
            for (city in CountryModel.selectAll()) {
                countries.add(
                    Country(
                        city[CountryModel.uuid],
                        city[CountryModel.id],
                        city[CountryModel.name]
                    )
                )
            }
        }

        return countries
    }

    // The below code is just a copy-paste that should actually be in the lib
    class BatchInsertUpdateOnDuplicate(table: Table, val onDupUpdate: List<Column<*>>) : BatchInsertStatement(table, false) {
        override fun prepareSQL(transaction: Transaction): String {
            val onUpdateSQL = if (onDupUpdate.isNotEmpty()) {
                " ON DUPLICATE KEY UPDATE " + onDupUpdate.joinToString { "${transaction.identity(it)}=VALUES(${transaction.identity(it)})" }
            } else ""
            return super.prepareSQL(transaction) + onUpdateSQL
        }
    }

    fun <T : Table, E> T.batchInsertOnDuplicateKeyUpdate(data: List<E>, onDupUpdateColumns: List<Column<*>>, body: T.(BatchInsertUpdateOnDuplicate, E) -> Unit) {
        data.
            takeIf { it.isNotEmpty() }?.
            let {
                val insert = BatchInsertUpdateOnDuplicate(this, onDupUpdateColumns)
                data.forEach {
                    insert.addBatch()
                    body(insert, it)
                }
                TransactionManager.current().exec(insert)
            }
    }
}