package net.axay.whalewarden

object Env {
    private fun env(name: String): String? = System.getenv(name)

    val webhookToken = env("WEBHOOK_TOKEN")
    val webhookPath: String = env("WEBHOOK_PATH") ?: "/webhook/update"
    val containerNames = env("UPDATE_CONTAINERS")?.split(' ') ?: emptyList()
}
