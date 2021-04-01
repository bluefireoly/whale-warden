package net.axay.whalewarden

import java.lang.System.getenv

object Env {
    val webhookPath = getenv("WEBHOOK_PATH") ?: "/webhook/update"
}
