import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.features.*
import org.slf4j.event.*
import io.ktor.http.content.*
import io.ktor.util.date.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import io.ktor.http.cio.websocket.*
import java.time.*
import io.ktor.auth.*
import io.ktor.gson.*
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.features.auth.basic.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.websocket.*
import io.ktor.client.features.websocket.WebSockets
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.channels.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.UserAgent
import io.ktor.client.features.BrowserUserAgent

object WsClientApp {
    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking {
            val client = HttpClient(CIO).config { install(WebSockets) }
            client.ws(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/myws/echo") {
                send(Frame.Text("Hello World"))
                for (message in incoming.map { it as? Frame.Text }.filterNotNull()) {
                    println("Server said: " + message.readText())
                }
            }
        }
    }
}
