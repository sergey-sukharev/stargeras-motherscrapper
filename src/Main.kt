import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.jetty.*
import scrappers.vk.vkScrapperModule

fun main(args: Array<String>) {
    val server = embeddedServer(Jetty, port = 8090,
        module = Application::vkScrapperModule,
        watchPaths = listOf("/")).start(true)
}