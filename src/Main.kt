import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import scrappers.vk.data.apiclient.VKClient
import scrappers.vk.domain.CountryInteractor
import scrappers.vk.domain.CountryInteractorImpl

fun main(args: Array<String>) {
    val server = embeddedServer(Jetty, port = 8090) {
        routing {
            get("/") {
                call.respondText("${VKClient.getActor()?.accessToken}", ContentType.Text.Plain)
            }
            get("/complete/vk-oauth2/") {
                call.respondText("HELLO WORLD!")
            }

            get("/countries/") {
                val interactor: CountryInteractor = CountryInteractorImpl(VKClient.getDatabaseClient(),
                    VKClient.getActor())
                val countries = interactor.loadCountries()
                call.respondText { "$countries" }
            }
        }
    }
    server.start(wait = true)
}