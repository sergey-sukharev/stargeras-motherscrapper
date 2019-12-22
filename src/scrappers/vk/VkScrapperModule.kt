package scrappers.vk

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import scrappers.vk.data.apiclient.VKClient
import scrappers.vk.domain.CountryInteractor
import scrappers.vk.domain.CountryInteractorImpl
import scrappers.vk.domain.model.Country


fun Application.vkScrapperModule(){
    routing {

        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }

        get("/vk-auth/") {
            call.respondRedirect("https://oauth.vk.com/authorize?client_id=5635196" +
                    "&display=page&redirect_uri=http://localhost:8090/auth/vk&scope=friends&response_type=code&v=5.103")

        }
        get("/auth/vk") {
            val code: String? = call.parameters["code"]
            var token = ""
            code?.let { token = VKClient.auth(code) }
            call.respondText("${token}", ContentType.Text.Plain)
        }

        get("/countries/") {
            val interactor: CountryInteractor = CountryInteractorImpl(
                VKClient.getDatabaseClient(),
                VKClient.getActor())
            val countries = interactor.loadCountries()
            call.respondText { "$countries" }
        }

        get("countries/get/all") {
            call.respond(HttpStatusCode.BadRequest, Country("yhj", 23, "Russia"))
        }
    }
}
