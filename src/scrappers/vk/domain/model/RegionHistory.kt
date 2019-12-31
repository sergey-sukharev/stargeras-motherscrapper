package scrappers.vk.domain.model


data class RegionHistory(
    val id: Int,
    val uuid: String,
    val itemsCount: Int,
    val itemsLoadedCount: Int,
    val isLoaded: Boolean,
    val lastUpdateTime: Long
)