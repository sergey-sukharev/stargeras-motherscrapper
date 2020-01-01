package scrappers.vk.data.repository.history

import org.h2.command.dml.Update
import org.jetbrains.exposed.sql.*
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

        var resultHistory : RegionHistory? = null

        transaction {
            val queryResult = UpdateHistoryModel.select {UpdateHistoryModel.id eq id}
            if (queryResult.empty()) return@transaction

            val result = queryResult.single()
            resultHistory = RegionHistory(result[UpdateHistoryModel.id], result[UpdateHistoryModel.uuid],
                result[UpdateHistoryModel.itemsCount], result[UpdateHistoryModel.itemsLoaded],
                result[UpdateHistoryModel.isLoaded], result[UpdateHistoryModel.lastUpdateTime])
        }


        return resultHistory
    }

    override fun saveHistory(history: RegionHistory) {
        val currentHistory = getHistory(history.id)

        transaction {
            if (currentHistory == null) {
                UpdateHistoryModel.insert {
                    it[this.id] = history.id
                    it[this.uuid] = history.uuid
                    it[this.itemsCount] = history.itemsCount
                    it[this.itemsLoaded] = history.itemsLoadedCount
                    it[this.isLoaded] = history.isLoaded
                    it[this.lastUpdateTime] = history.lastUpdateTime
                }
            } else {
                UpdateHistoryModel.update({
                    UpdateHistoryModel.id eq history.id
                }){
                    it[this.itemsCount] = history.itemsCount
                    it[this.itemsLoaded] = history.itemsLoadedCount
                    it[this.isLoaded] = history.isLoaded
                    it[this.lastUpdateTime] = history.lastUpdateTime
                }
            }

        }
    }

    override fun getHistoryAll(): List<RegionHistory> {
        val result = mutableListOf<RegionHistory>()

        transaction {
            UpdateHistoryModel.selectAll().forEach {
                result.add(
                    RegionHistory(it[UpdateHistoryModel.id], it[UpdateHistoryModel.uuid],
                        it[UpdateHistoryModel.itemsCount], it[UpdateHistoryModel.itemsLoaded],
                        it[UpdateHistoryModel.isLoaded], it[UpdateHistoryModel.lastUpdateTime])
                )
            }
        }

        return result
    }
}