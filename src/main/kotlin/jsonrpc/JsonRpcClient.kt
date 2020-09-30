package io.github.dragneelfps.aria2kt.jsonrpc

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import io.github.dragneelfps.aria2kt.jsonrpc.ParamType.LIST
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class JsonRpcClient(
    val host: String,
    val port: Int,
    val path: String
) {

    suspend fun openWebSocket(consumer: suspend (frame: Frame) -> Unit) {
        httpClient.ws(method = HttpMethod.Get, host = host, port = port, path = path) {
            incoming.consumeAsFlow()
                .onEach(consumer)
                .collect()
        }
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

            @OptIn(KtorExperimentalAPI::class)
            install(WebSockets)

            expectSuccess = false

            install(JsonFeature) {
                @OptIn(KtorExperimentalAPI::class)
                acceptContentTypes = listOf(ContentType("application", "json-rpc"))
                serializer = KotlinxSerializer()
            }
        }
    }
}

