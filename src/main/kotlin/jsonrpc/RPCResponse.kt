package io.github.dragneelfps.aria2kt.jsonrpc

import kotlinx.serialization.Serializable

@Serializable
data class RPCResponse<T, V>(
    val jsonrpc: String,
    val id: String,
    val method: String? = null,
    val result: T? = null,
    val error: V? = null,
)
