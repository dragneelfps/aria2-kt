package io.github.dragneelfps.aria2kt.jsonrpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RPCNotification(
    val jsonrpc: String,
    val method: String,
    val params: JsonElement
)
