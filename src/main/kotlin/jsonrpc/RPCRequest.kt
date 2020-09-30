@file:Suppress("UNCHECKED_CAST")

package io.github.dragneelfps.aria2kt.jsonrpc

import io.github.dragneelfps.aria2kt.jsonrpc.ParamType.LIST
import io.github.dragneelfps.aria2kt.jsonrpc.ParamType.OBJ
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*
import java.util.*

@Serializable
data class RPCRequest(
    val jsonrpc: String,
    val id: String = UUID.randomUUID().toString(),
    val method: String,
    val params: JsonElement
) {

    companion object {

        fun create(jsonrpc: String = "2.0", method: String, params: JsonElement): RPCRequest =
            RPCRequest(jsonrpc, method = method, params = params)

//        fun create(jsonrpc: String = "2.0", method: String, params: JsonObject): RPCRequest =
//            create(jsonrpc, method = method, params = params as JsonElement)
//
//
//        fun create(jsonrpc: String = "2.0", method: String, params: JsonArray) =
//            create(jsonrpc, method = method, params = params as JsonElement)

        inline fun <reified T> create(
            jsonrpc: String = "2.0",
            method: String,
            paramType: ParamType,
            params: T
        ): RPCRequest {
            val pJson = Json.encodeToJsonElement(params)
            val pActual = when (paramType) {
                LIST -> buildJsonArray {
                    pJson.jsonObject.values.forEach(this::add)
                }
                OBJ -> pJson.jsonObject
            }
            return create(jsonrpc, method = method, params = pActual)
        }

        fun create(jsonrpc: String = "2.0", method: String, vararg params: Any): RPCRequest {
            return create(jsonrpc, method, params.buildJsonArray())

            // TODO: 9/26/2020 See if similar is available as before
//            return create(jsonrpc, method, paramType = LIST, params = params)
        }
    }
}

fun Array<*>.buildJsonArray(): JsonArray {
    return buildJsonArray {
        forEach { param ->
            when (param) {
                is Number -> add(param)
                is String -> add(param)
                is Boolean -> add(param)
                is Array<*> -> add(param.buildJsonArray())
                is List<*> -> add(param.buildJsonArray())
                is Map<*, *> -> add((param as Map<String, Any>).buildJsonMap())
                else -> add(Json.encodeToJsonElement(param))
            }
        }
    }
}

fun Map<String, Any>.buildJsonMap(): JsonObject {
    return buildJsonObject {
        forEach { (k, v) ->
            when (v) {
                is Number -> put(k, v)
                is String -> put(k, v)
                is Boolean -> put(k, v)
                is List<*> -> put(k, v.buildJsonArray())
                is Map<*, *> -> put(k, (v as Map<String, Any>).buildJsonMap())
            }
        }
    }
}

fun List<*>.buildJsonArray(): JsonArray {
    return toTypedArray().buildJsonArray()
}

enum class ParamType {
    LIST, OBJ;
}
