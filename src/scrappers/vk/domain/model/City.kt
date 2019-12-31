package scrappers.vk.domain.model

data class City (val uuid: String, val region: Region, val id: Int,
                 val name: String, val area: String, val regionName: String)