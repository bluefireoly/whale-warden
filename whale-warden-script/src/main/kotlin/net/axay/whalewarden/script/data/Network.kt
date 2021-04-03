package net.axay.whalewarden.script.data

data class Network(
    val name: String,
    val ipv6: Boolean?,
    val driver: String?,
    val internal: Boolean?,
    val attachable: Boolean?,
)
