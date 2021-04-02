package net.axay.whalewarden.script.data

class Service(
    val image: String,
    val name: String?,
    val tty: Boolean,
    val env: Map<String, String>,
    val events: Events,
) {
    class Events(
        val onFirstStart: (() -> Unit)?,
        val onStart: (() -> Unit)?,
        val onRestart: (() -> Unit)?,
        val onStop: (() -> Unit)?,
    )
}
