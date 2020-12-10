package io.github.dragneelfps.aria2kt.jsonrpc

import io.github.dragneelfps.aria2kt.jsonrpc.ParamType.LIST
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class JsonRpcClient(
    val host: String,
    val port: Int,
    val path: String
) {

    private val wsListenerScope = CoroutineScope(Dispatchers.IO)
    private var wss: WebSocketSession? = null

    suspend fun openWebSocket(listener: JsonRpcListener) {
        if (wss?.isActive == true) wss?.cancel()
        wss = httpClient.webSocketSession(method = HttpMethod.Get, host = host, port = port, path = path).apply {
            incoming.receiveAsFlow()
                .onEach { listener(it) }
                .launchIn(wsListenerScope)
        }
    }

    suspend fun closeWebSocket() {
        wsListenerScope.cancel()
        wss?.close()
        httpClient.close()
    }

    suspend inline fun <T, V, reified U> call(
        method: String,
        paramType: ParamType = LIST,
        params: U
    ): RPCResponse<T, V> {
        return httpClient.post(
            host = host,
            path = path,
            port = port,
            body = Json.encodeToString(RPCRequest.create(method = method, paramType = paramType, params = params))
        )
    }

    suspend inline fun <reified T, reified V> call(method: String, vararg params: Any): RPCResponse<T, V> {
        return httpClient.post(
            host = host,
            path = path,
            port = port,
            body = Json.encodeToString(RPCRequest.create(method = method, params = params))
        )
    }

    companion object {
        val httpClient = HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }

            install(WebSockets)

            expectSuccess = false

            install(JsonFeature) {
                acceptContentTypes = listOf(ContentType("application", "json-rpc"))
                serializer = KotlinxSerializer()
            }
        }
    }
}

fun interface JsonRpcListener {
    operator fun invoke(frame: Frame)
}

interface JsonRpcRemoveListener {
    suspend operator fun invoke()
}