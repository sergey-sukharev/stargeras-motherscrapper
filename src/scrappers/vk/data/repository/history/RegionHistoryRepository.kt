package scrappers.vk.data.repository.history

import scrappers.vk.domain.model.RegionHistory

interface RegionHistoryRepository {
    fun getHistory(id: Int) : RegionHistory?
    fun saveHistory(history: RegionHistory)
    fun getHistoryAll() : List<RegionHistory>
}