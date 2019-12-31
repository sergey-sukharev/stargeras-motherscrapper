package scrappers.vk

import com.vk.api.sdk.exceptions.ApiAuthException
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.GsonConverter
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.*
import scrappers.vk.data.apiclient.VKClient
import scrappers.vk.data.apiclient.model.country.region.CitiesLoadRequest
import scrappers.vk.data.apiclient.model.country.region.RegionLoadRequest
import scrappers.vk.domain.interactor.region.RegionLoaderInteractor
import scrappers.vk.domain.interactor.region.RegionLoaderInteractorImpl
import scrappers.vk.domain.model.Country


fun Application.vkScrapperModule() {
    routing {

        install(ContentNegotiation) {
            register(ContentType.Application.Json, GsonConverter())

            gson {
                setPrettyPrinting()
            }

        }

        get("/vk-auth/") {
            call.respondRedirect(
                "https://oauth.vk.com/authorize?client_id=5635196" +
                        "&display=page&redirect_uri=http://localhost:8090/auth/vk&scope=friends&response_type=code&v=5.103"
            )

        }
        get("/auth/vk") {
            val code: String? = call.parameters["code"]
            var token = ""
            code?.let { token = VKClient.auth(code) }
            call.respondText("${token}", ContentType.Text.Plain)
        }

        get("/countries/") {
            val interactor: RegionLoaderInteractor =
                RegionLoaderInteractorImpl()
            val countries = interactor.loadCountries()
            call.respondText { "$countries" }
        }


        get("countries/get/all") {
            call.respond(HttpStatusCode.BadRequest, Country("yhj", 23, "Russia"))
        }

        post("/regions/load/") {

            val request = call.receive<RegionLoadRequest>()

            val interactor: RegionLoaderInteractor = RegionLoaderInteractorImpl()

            interactor.loadRegions(request.country)
        }

        post("/cities/load/") {
            val request = call.receive<CitiesLoadRequest>()
            val interactor: RegionLoaderInteractor =
                RegionLoaderInteractorImpl()

            interactor.loadCities(request.region)
        }

        get("/loader/deep/{id}") {
            val id = call.parameters["id"]
            id?.let {
                val interactor: RegionLoaderInteractor = RegionLoaderInteractorImpl()
                try {
                    interactor.deepLoadByCountry(id.toInt())
                } catch (e: ApiAuthException) {
                    call.respond(HttpStatusCode.NonAuthoritativeInformation, "Необходима авторизация в ВК")
                }

            }

        }
    }
}
