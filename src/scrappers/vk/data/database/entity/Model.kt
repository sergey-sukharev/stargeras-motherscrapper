package scrappers.vk.data.database.entity

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object RegionTypeEntity: Table() {
    override val tableName: String
        get() = "region_type"
    val uuid = varchar("uuid", 36)
    val name = varchar("name", 30).primaryKey()
}

object RegionModel : Table() {
    override val tableName: String
        get() = "region"

    val uuid = varchar("uuid", 36)
    val id = integer("id").primaryKey()
    val name = varchar("name", length = 50)
    val area = varchar("area", 50).nullable()
    val regionName = varchar("region", 50).nullable()
    val region = integer("region_id") references RegionModel.id
    val regionType = varchar("region_type", 36) references RegionTypeEntity.uuid
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
        SchemaUtils.create(RegionModel, UpdateHistoryModel)
    }
}