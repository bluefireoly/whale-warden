package net.axay.whalewarden.manager

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.command.PullImageResultCallback
import com.github.dockerjava.api.model.PullResponseItem
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.core.DockerClientImpl
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient
import kotlinx.coroutines.*
import net.axay.whalewarden.common.logging.logInfo
import net.axay.whalewarden.manager.script.applyService
import net.axay.whalewarden.manager.script.runConfigScript
import net.axay.whalewarden.script.registry.Registry

val dockerClient: DockerClient = run {
    val config = DefaultDockerClientConfig.createDefaultConfigBuilder()
        .withDockerHost("unix:///var/run/docker.sock")
        .build()
    val httpClient = ApacheDockerHttpClient.Builder()
        .dockerHost(config.dockerHost)
        .build()

    DockerClientImpl.getInstance(config, httpClient)
}

suspend fun main() = withContext(Dispatchers.IO) {
    runConfigScript()

    val createJobs = mutableListOf<Job>()
    Registry.services.forEach {
        createJobs += launch {
            val pullCallback = PullImageResultCallback()
            dockerClient.pullImageCmd(it.image).exec(pullCallback)
            pullCallback.awaitCompletion()
            logInfo("pulled image ${it.image}")

            dockerClient.createContainerCmd(it.image).applyService(it).exec()
            logInfo("Created service: ${it.name}")
        }
    }

    createJobs.joinAll()
    logInfo("All services created")
}

//fun main(args: Array<String>) = EngineMain.main(args)
//
//fun Application.mainModule() {
//    routing {
//        get(Env.webhookPath) {
//            val token = call.parameters["token"]
//            if (token == Env.webhookToken) {
//                withContext(Dispatchers.IO) {
//                    println("Listing containers")
//                    val updateContainers = dockerClient.listContainersCmd()
//                        .withNameFilter(Env.containerNames)
//                        .exec()
//
//                    updateContainers
//                        .mapTo(HashSet()) {
//                            println("Image of container ${it.names[0]} is ${it.image}")
//                            it.image
//                        }
//                        .map {
//                            launch {
//                                val callback = PullImageResultCallback()
//                                dockerClient.pullImageCmd(it).exec(callback)
//                                callback.awaitStarted()
//                                println("Now pulling image with name $it...")
//                                callback.awaitCompletion()
//                                println("Pulled image with name $it")
//                            }
//                        }.joinAll()
//
//                    updateContainers.map {
//                        launch {
//                            dockerClient.removeContainerCmd(it.id).exec()
//                            println("Updated container ${it.id} with name ${it.names[0]}")
//                        }
//                    }.joinAll()
//
//                    println("Updated all containers")
//                }
//                call.respond(HttpStatusCode.OK, "Updated containers")
//            } else {
//                call.respond(HttpStatusCode.Unauthorized, "Wrong or missing token")
//            }
//        }
//    }
//}
