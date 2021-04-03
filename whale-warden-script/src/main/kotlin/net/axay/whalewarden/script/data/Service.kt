package net.axay.whalewarden.script.data

data class Service(
    val image: String,
    val name: String?,
    val tty: Boolean,
    val env: Map<String, String>,
    val events: Events,
    val mounts: Collection<Mount>,
    val ports: Map<Int, Int>,
    val restartPolicy: RestartPolicy?,
) {
    class Events(
        val onFirstStart: (() -> Unit)?,
        val onStart: (() -> Unit)?,
        val onRestart: (() -> Unit)?,
        val onStop: (() -> Unit)?,
    )

    data class Mount(
        val source: String,
        val target: String,
        val type: Type,
        val readOnly: Boolean,
    ) {
        enum class Type {
            VOLUME, BIND,
        }
    }

    data class RestartPolicy(
        val type: Type,
        val maximumRetryCount: Int,
    ) {
        enum class Type {
            NO_RESTART, ALWAYS, UNLESS_STOPPED, ON_FAILURE
        }
    }
}
