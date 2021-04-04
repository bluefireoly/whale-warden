package net.axay.whalewarden.script.data

data class Bind(
    val path: String,
    val readOnly: Boolean?,
    val propagation: String?,
    val nonRecursive: Boolean?,
) {
    val ro get() = copy(readOnly = true)
}
