package scrappers.vk.data.database.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CountryModel : Table() {
    override val tableName: String
        get() = "country"

    val uuid = varchar("uuid", 36).primaryKey()
    val id = integer("id")
    val name = varchar("name", length = 100) // Column<String>
    val updateTime = long("update_time")
}

object RegionModel : Table() {
    override val tableName: String
        get() = "region"

    val uuid = varchar("uuid", 36).primaryKey()
    val id = integer("id")
    val name = varchar("name", length = 100)
    val updateTime = long("update_time")
    val country = varchar("country_id", 100) references CountryModel.uuid
}

object CityModel : Table() {
    override val tableName: String
        get() = "city"

    val uuid = varchar("uuid", 36).primaryKey()
    val id = integer("id")
    val name = varchar("name", length = 256)
    val area = varchar("area", 50).nullable()
    val region = varchar("region", 50).nullable()
    val updateTime = long("update_time")
    val region_id = varchar("region_id", 36).references(RegionModel.uuid).nullable()
}

object UpdateHistoryModel: Table() {
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
        SchemaUtils.create(CountryModel, RegionModel, CityModel, UpdateHistoryModel)
    }
}