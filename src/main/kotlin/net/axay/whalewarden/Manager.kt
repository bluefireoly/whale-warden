package net.axay.whalewarden

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.routing.*
import io.ktor.server.netty.*

val client = HttpClient(CIO)

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.mainModule() {
    routing {
        get(Env.webhookPath) {

        }
    }
}
