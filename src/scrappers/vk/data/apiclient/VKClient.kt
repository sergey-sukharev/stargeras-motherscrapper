package scrappers.vk.data.apiclient

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.REDIRECT_URI
import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.UserAuthResponse

object VKClient {

    private val client: VkApiClient
    private lateinit var actor: UserActor

    private val APP_ID = 0
    private val CLIENT_SECRET = ""
    private val code = ""

    init {
        val transportClient: TransportClient = HttpTransportClient.getInstance()
        client = VkApiClient(transportClient)

        auth()
    }

    private fun auth() {
        val authResponse: UserAuthResponse = client.oauth()
            .userAuthorizationCodeFlow(
                APP_ID,
                CLIENT_SECRET,
                REDIRECT_URI,
                code
            )
            .execute()

        actor = UserActor(authResponse.userId, authResponse.accessToken)
    }

    fun getDatabaseClient() = client.database()
    fun getActor() = actor
}