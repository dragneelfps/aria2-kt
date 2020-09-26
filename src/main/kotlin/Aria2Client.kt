package io.nooblabs.aria2kt

import io.ktor.http.cio.websocket.*
import io.nooblabs.aria2kt.Aria2Notification.Event
import io.nooblabs.aria2kt.jsonrpc.JsonRpcClient
import io.nooblabs.aria2kt.jsonrpc.RPCNotification
import io.nooblabs.aria2kt.jsonrpc.RPCResponse
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


class Aria2Client(
    private val secret: String? = null,
    host: String = "127.0.0.1",
    port: Int = 6800,
    path: String = "/jsonrpc"
) {
    private val jsonRpcClient = JsonRpcClient(host, port, path)

    suspend fun openWebSocket(consumer: suspend (notification: Aria2Notification) -> Unit) {
        jsonRpcClient.openWebSocket { frame ->
            if (frame is Frame.Text) {
                val rpcNotification = Json.decodeFromString<RPCNotification>(frame.readText())
                consumer(
                    Aria2Notification(
                        method = rpcNotification.method,
                        event = Event(gid = rpcNotification.params.jsonArray.first().jsonObject["gid"]!!.jsonPrimitive.content)
                    )
                )
            }
        }
    }

    suspend fun addUri(
        uris: List<String>,
        options: Map<String, String>? = null,
        position: Int? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, uris, options, position).callMethod("aria2.addUri")

    suspend fun addTorrent(
        torrent: String,
        uris: List<String>? = null,
        options: Map<String, String>? = null,
        position: Int? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, torrent, uris, options, position).callMethod("aria2.addTorrent")

    suspend fun addMetalink(
        metalink: String,
        options: Map<String, String>? = null,
        position: Int? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, metalink, options, position).callMethod("aria2.addMetalink")

    suspend fun remove(
        gid: String
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.remove")

    suspend fun forceRemove(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.forceRemove")

    suspend fun pause(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.pause")

    suspend fun pauseAll(): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.pauseAll")

    suspend fun forcePause(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.forcePause")

    suspend fun forcePauseAll(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.forcePauseAll")

    suspend fun unpause(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.unpause")

    suspend fun unpauseAll(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.unpauseAll")

    suspend fun tellStatus(
        gid: String,
        keys: List<String>? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid, keys).callMethod("aria2.tellStatus")

    suspend fun getUris(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.getUris")

    suspend fun getFiles(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.getFiles")

    suspend fun getPeers(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.getPeers")

    suspend fun getServers(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.getServers")

    suspend fun tellActive(
        keys: List<String>? = null,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, keys).callMethod("aria2.tellActive")

    suspend fun tellWaiting(
        offset: Int,
        num: Int,
        keys: List<String>? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, offset, num, keys).callMethod("aria2.tellWaiting")

    suspend fun tellStopped(
        offset: Int,
        num: Int,
        keys: List<String>? = null
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, offset, num, keys).callMethod("aria2.tellStopped")

    suspend fun changePosition(
        gid: String,
        pos: Int,
        how: Pos
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid, pos, how.name).callMethod("aria2.changePosition")

    suspend fun changeUri(
        gid: String,
        fileIndex: Int,
        delUris: List<String>,
        addUris: List<String>,
        position: Int? = null
    ): RPCResponse<Gid, ErrorVal> =
        listOf(secret, gid, fileIndex, delUris, addUris, position).callMethod("aria2.changeUri")

    suspend fun getOption(
        gid: String,
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.getOption")

    suspend fun changeOption(
        gid: String,
        options: Map<String, String>
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid, options).callMethod("aria2.changeOption")

    suspend fun getGlobalOption(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.getGlobalOption")

    suspend fun changeGlobalOption(
        options: Map<String, String>
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, options).callMethod("aria2.changeGlobalOption")

    suspend fun getGlobalStat(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.getGlobalStat")

    suspend fun purgeDownloadResult(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.purgeDownloadResult")

    suspend fun removeDownloadResult(
        gid: String
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, gid).callMethod("aria2.removeDownloadResult")

    suspend fun getVersion(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.getVersion")

    suspend fun getSessionInfo(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.getSessionInfo")

    suspend fun shutdown(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.shutdown")

    suspend fun forceShutdown(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.forceShutdown")

    suspend fun saveSession(
    ): RPCResponse<Gid, ErrorVal> = listOf(secret).callMethod("aria2.saveSession")

    suspend fun multicall(
        methods: List<Map<String, Any>>
    ): RPCResponse<Gid, ErrorVal> = listOf(secret, methods).callMethod("aria2.multicall")

    suspend fun listMethods(
    ): RPCResponse<Gid, ErrorVal> = emptyList<Nothing>().callMethod("aria2.listMethods")


    private suspend inline fun <reified T, reified V> List<Any?>.callMethod(method: String): RPCResponse<T, V> =
        jsonRpcClient.call(method, *filterNotNull().toTypedArray())

}

enum class Pos {
    POS_SET, POS_CUR, POS_END;
}

typealias Gid = String

@Serializable
data class ErrorVal(val code: Int, val message: String)

data class Aria2Notification(val method: String, val event: Event) {
    data class Event(val gid: String)
}

