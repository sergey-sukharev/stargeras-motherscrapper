package scrappers.vk.data.apiclient

import com.vk.api.sdk.client.TransportClient
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.objects.UserAuthResponse

object VKClient {

    private val client: VkApiClient
    private lateinit var actor: UserActor

    private val APP_ID = 5635196
    private val CLIENT_SECRET = "JsHPWPtqyk7PNadNgQbZ"
    private val code = "db735ac9f0bed93572"
    private val REDIRECT_URL = "http://localhost:8090/auth/vk"

    init {
        val transportClient: TransportClient = HttpTransportClient.getInstance()
        client = VkApiClient(transportClient)

        val userId = 26547370
        val token = "ff407d20f5487eb1fcd938e68cdab1ddcef5a64e8ad6f1ebd9ffc1e38b59fa484244183f7b995e30d9069"
        actor = UserActor(userId, token)

//        try {
//            auth()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
    }

    /*
    *
    * https://oauth.vk.com/authorize?client_id=5635196&display=page&redirect_uri=http://localhost:8090/auth/vk&scope=friends&response_type=code&v=5.103
    *
    * */

    private fun auth() {
        val authResponse: UserAuthResponse = client.oauth()
            .userAuthorizationCodeFlow(
                APP_ID,
                CLIENT_SECRET,
                REDIRECT_URL,
                code
            )
            .execute()

        actor = UserActor(authResponse.userId, authResponse.accessToken)
    }

    fun getDatabaseClient() = client.database()
    fun getActor() = actor
}