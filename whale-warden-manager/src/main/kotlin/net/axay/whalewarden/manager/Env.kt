package net.axay.whalewarden.manager

import java.io.File

object Env {
    private fun env(name: String): String? = System.getenv(name)

    val configFile = File(env("CONFIG_FILE") ?: "./whale-warden.kts")
    val webhookToken = env("WEBHOOK_TOKEN")
    val webhookPath: String = env("WEBHOOK_PATH") ?: "/webhook/update"
    val containerNames = env("UPDATE_CONTAINERS")?.split(' ') ?: emptyList()
}
