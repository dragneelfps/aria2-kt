import io.github.dragneelfps.aria2kt.Aria2Client
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

suspend fun main() {

    val aria2Client = Aria2Client()

    aria2Client.start()
    aria2Client.addListener { notification ->
        println(notification)
    }

    aria2Client.addUri(uris = listOf("https://raw.githubusercontent.com/out386/aria-telegram-mirror-bot/master/LICENSE.md"))
    delay(100)
    aria2Client.addUri(uris = listOf("https://raw.githubusercontent.com/out386/aria-telegram-mirror-bot/master/LICENSE.md"))
    delay(100)
    aria2Client.addUri(uris = listOf("https://raw.githubusercontent.com/out386/aria-telegram-mirror-bot/master/LICENSE.md"))
    delay(100)
    aria2Client.addUri(uris = listOf("https://raw.githubusercontent.com/out386/aria-telegram-mirror-bot/master/LICENSE.md"))

//
    aria2Client.stop()
}
