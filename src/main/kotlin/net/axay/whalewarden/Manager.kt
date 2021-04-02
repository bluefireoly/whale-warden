package net.axay.whalewarden

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.PullImageResultCallback
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val dockerClient: DockerClient = run {
    val config = DefaultDockerClientConfig.createDefaultConfigBuilder().build()
    val httpClient = ApacheDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .build()

    DockerClientImpl.getInstance(config, httpClient)
}

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.mainModule() {
    routing {
        get(Env.webhookPath) {
            val token = call.parameters["token"]
            if (token == Env.webhookToken) {
                withContext(Dispatchers.IO) {
                    println("Listing containers")
                    val updateContainers = dockerClient.listContainersCmd()
                        .withNameFilter(Env.containerNames)
                        .exec()

                    updateContainers
                        .mapTo(HashSet()) {
                            println("Image of container ${it.names[0]} is ${it.image}")
                            it.image
                        }
                        .map {
                            launch {
                                val callback = PullImageResultCallback()
                                dockerClient.pullImageCmd(it).exec(callback)
                                callback.awaitStarted()
                                println("Now pulling image with name $it...")
                                callback.awaitCompletion()
                                println("Pulled image with name $it")
                            }
                        }.joinAll()

                    updateContainers.map {
                        launch {
                            dockerClient.stopContainerCmd(it.id).exec()
                            println("Updated container ${it.id} with name ${it.names[0]}")
                        }
                    }.joinAll()

                    println("Updated all containers")
                }
                call.respond(HttpStatusCode.OK, "Updated containers")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Wrong or missing token")
            }
        }
    }
}
