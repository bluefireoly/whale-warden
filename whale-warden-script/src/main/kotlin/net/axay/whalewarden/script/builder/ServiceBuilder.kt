@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.whalewarden.script.builder

import net.axay.whalewarden.script.builder.api.Builder
import net.axay.whalewarden.script.data.RestartPolicy
import net.axay.whalewarden.script.data.Service
import net.axay.whalewarden.script.logging.logInfo
import net.axay.whalewarden.script.registry.Registry

/**
 * Builds a new service.
 *
 * @param image the image of the service
 * @param builder the [ServiceBuilder]
 */
inline fun service(image: String, builder: ServiceBuilder.() -> Unit) {
    val service = ServiceBuilder(image).apply(builder).internalBuilder.build()
    logInfo("Built a service: $service")
    Registry.services += service
}

class ServiceBuilder(
    val image: String,
) {
    inner class Internal : Builder<Service> {
        val eventBuilder = EventBuilder()

        val mounts = mutableListOf<Service.Mount>()

        val ports = HashMap<Int, Int>()

        var restartPolicy: RestartPolicy? = null

        override fun build() = Service(
            image, name, tty, env, eventBuilder.internalBuilder.build(), mounts, ports, restartPolicy
        )
    }

    /**
     * INTERNAL! You probably should not use this.
     */
    val internalBuilder = this.Internal()

    class EventBuilder {
        inner class Internal : Builder<Service.Events> {
            var onFirstStart: (() -> Unit)? = null
            var onStart: (() -> Unit)? = null
            var onRestart: (() -> Unit)? = null
            var onStop: (() -> Unit)? = null

            override fun build() = Service.Events(onFirstStart, onStart, onRestart, onStop)
        }

        val internalBuilder = this.Internal()

        /**
         * Executed when a container is started the first time.
         */
        fun onFirstStart(action: () -> Unit) {
            internalBuilder.onFirstStart = action
        }

        /**
         * Executed when a container is started.
         */
        fun onStart(action: () -> Unit) {
            internalBuilder.onStart = action
        }

        /**
         * Executed when a container is restarted.
         */
        fun onRestart(action: () -> Unit) {
            internalBuilder.onRestart = action
        }

        /**
         * Executed when a container is stopped.
         */
        fun onStop(action: () -> Unit) {
            internalBuilder.onStop = action
        }
    }

    /**
     * The name of the service.
     */
    var name: String? = null

    /**
     * Attach standard streams to a TTY, including `stdin` if it is not closed.
     */
    var tty = false

    /**
     * The environment variables for the service.
     */
    val env = HashMap<String, String>()

    /**
     * Opens a scope in which you can define some event
     * callbacks.
     */
    inline fun events(builder: EventBuilder.() -> Unit) {
        internalBuilder.eventBuilder.apply(builder)
    }

    /**
     * Adds all the specified mounts to the host configuration
     * of the service.
     */
    fun mounts(vararg mounts: Pair<String, String>) {
        mounts.mapTo(internalBuilder.mounts) { Service.Mount(it.first, it.second) }
    }

    /**
     * Define multiple environment variables at once.
     */
    fun env(vararg variables: Pair<String, Any>) {
       env.putAll(variables.map { it.first to it.second.toString() })
    }

    /**
     * Define the port bindings.
     */
    fun ports(vararg ports: Pair<Int, Int>) {
        internalBuilder.ports.putAll(ports)
    }

    /**
     * Define the restart policy.
     *
     * @param maxRetryAmount the maximum amount of retries until docker
     * will give up to restart the container (null for unlimited or undefined)
     */
    fun restart(policy: RestartPolicy, maxRetryAmount: Int? = null) {
        internalBuilder.restartPolicy =
            RestartPolicy(policy.name, maxRetryAmount ?: policy.maximumRetryCount)
    }
}
