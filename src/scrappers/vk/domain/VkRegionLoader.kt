package scrappers.vk.domain

import com.vk.api.sdk.actions.Database
import com.vk.api.sdk.client.actors.UserActor
import scrappers.vk.data.apiclient.VKClient
import scrappers.vk.domain.model.Country
import scrappers.vk.domain.model.Region
import java.util.*

object VkRegionLoader {

    private val databaseClient: Database
    private val actor: UserActor

    init {
        databaseClient =VKClient.getDatabaseClient()
        actor = VKClient.getActor()
    }

    fun loadCountries(countriesModelList: MutableList<Country>,
                      count: Int = 100, offset: Int = 0) {
        val responseBuilder = databaseClient.getCountries(actor).apply {
            needAll(true)
            count(count)
            offset(offset)
        }

        val countriesResultExecutor = responseBuilder.execute()
        val countriesResultList = countriesResultExecutor.items
        countriesResultList.forEach {
            countriesModelList.add(
                Country(
                    UUID.randomUUID().toString(),
                    it.id, it.title
                )
            )
        }

        if (countriesResultExecutor.count > countriesModelList.size)
            loadCountries(countriesModelList, count, offset + count)

    }

    fun loadRegions(country: Country, regionModelList: MutableList<Region>,
                    count: Int = 1000, offset: Int = 0) {
        val responseBuilder = databaseClient.getRegions(actor, country.id).apply {
            count(count)
            offset(offset)
        }

        val countriesResultExecutor = responseBuilder.execute()
        val countriesResultList = countriesResultExecutor.items
        countriesResultList.forEach {
            regionModelList.add(
                Region(
                    UUID.randomUUID().toString(),
                    country,
                    it.id,
                    it.title
                )
            )
        }

        if (countriesResultExecutor.count > regionModelList.size)
            loadRegions(country, regionModelList, count, offset + count)
    }



}