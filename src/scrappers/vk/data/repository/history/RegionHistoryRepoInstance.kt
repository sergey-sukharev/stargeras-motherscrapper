package scrappers.vk.data.repository.history

import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import scrappers.vk.data.database.CountriesDatabase
import scrappers.vk.data.database.entity.UpdateHistoryModel
import scrappers.vk.domain.model.RegionHistory

object RegionHistoryRepoInstance : RegionHistoryRepository {

    private val database = CountriesDatabase.database

    /**
     * Получение истории обновления по id региона
     */
    override fun getHistory(id: Int): RegionHistory? {

        var queryResult : Query? = null

        transaction {
            queryResult = UpdateHistoryModel.select {UpdateHistoryModel.id eq id}
        }

        queryResult?.let {
            if (it.empty()) return null
            val result = it.single()
            return RegionHistory(result[UpdateHistoryModel.id], result[UpdateHistoryModel.uuid],
                result[UpdateHistoryModel.itemsCount], result[UpdateHistoryModel.itemsLoaded],
                result[UpdateHistoryModel.isLoaded], result[UpdateHistoryModel.lastUpdateTime])
        }

        return null
    }

    override fun saveHistory(history: RegionHistory) {

        transaction {
            UpdateHistoryModel.insert {
                it[this.id] = history.id
                it[this.uuid] = history.uuid
                it[this.itemsCount] = history.itemsCount
                it[this.itemsLoaded] = history.itemsLoadedCount
                it[this.isLoaded] = history.isLoaded
                it[this.lastUpdateTime] = history.lastUpdateTime
            }
        }
    }

    override fun getHistoryAll(): List<RegionHistory> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}