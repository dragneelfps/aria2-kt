import io.nooblabs.aria2kt.Aria2Client
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val aria2Client = Aria2Client()

    launch {
        aria2Client.openWebSocket { notification ->
            println(notification)
        }
    }

    val res =
        aria2Client.addUri(uris = listOf("https://raw.githubusercontent.com/out386/aria-telegram-mirror-bot/master/LICENSE.md"))

    println(res)

}
