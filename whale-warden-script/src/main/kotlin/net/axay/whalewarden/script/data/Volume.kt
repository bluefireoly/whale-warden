package net.axay.whalewarden.script.data

data class Volume(
    val name: String,
    val readOnly: Boolean?,
    val driver: String?,
) {
    val ro get() = copy(readOnly = true)
}
