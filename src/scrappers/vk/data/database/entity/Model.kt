package scrappers.vk.data.database.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Country : Table() {
    override val tableName: String
        get() = "country"

    val uuid = varchar("uuid", 36)
    val id = integer("id").primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
    val updateTime = long("update_time")
}

object Region : Table() {
    override val tableName: String
        get() = "region"

    val uuid = varchar("uuid", 36)
    val id = integer("id").primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
    val updateTime = long("update_time")
    val country = integer("country_id") references Country.id
}

object City : Table() {
    override val tableName: String
        get() = "city"

    val uuid = varchar("uuid", 36)
    val id = integer("id").primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
    val area = varchar("area", 50)
    val regionName = varchar("region", 50)
    val updateTime = long("update_time")
    val region = integer("region_id") references Region.id
}

object UpdateHistory: Table() {
    override val tableName: String
        get() = "update_history"

    val id = integer("id").primaryKey()
    val uuid = varchar("uuid", 36)
    val itemsCount = integer("items_count")
    val itemsLoaded = integer("items_loaded")
    val isLoaded = bool("is_loaded")
    val lastUpdateTime = long("last_update_time")


}
fun createTable() {
    transaction {
        SchemaUtils.create(Country, Region, City, UpdateHistory)
    }
}