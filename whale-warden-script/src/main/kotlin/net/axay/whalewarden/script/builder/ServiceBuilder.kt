@file:Suppress("MemberVisibilityCanBePrivate")

package net.axay.whalewarden.script.builder

import net.axay.whalewarden.script.builder.api.Builder
import net.axay.whalewarden.script.data.Service

/**
 * Builds a new service.
 *
 * @param image the image of the service
 * @param builder the [ServiceBuilder]
 */
inline fun service(image: String, builder: ServiceBuilder.() -> Unit) {
    ServiceBuilder(image).apply(builder).internalBuilder.build()
}

class ServiceBuilder(
    val image: String,
) {
    inner class Internal : Builder<Service> {
        val eventBuilder = EventBuilder()

        override fun build() = Service(
            image, name, tty, env, eventBuilder.internalBuilder.build()
        )
    }

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

        fun onFirstStart(action: () -> Unit) {
            internalBuilder.onFirstStart = action
        }

        fun onStart(action: () -> Unit) {
            internalBuilder.onStart = action
        }

        fun onRestart(action: () -> Unit) {
            internalBuilder.onRestart = action
        }

        fun onStop(action: () -> Unit) {
            internalBuilder.onStop = action
        }
    }

    class VolumeBuilder {
        inner class Internal : Builder<Unit> {

            override fun build() = Unit
        }

        operator fun Pair<String, String>.unaryPlus() {

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
     * Opens a scope in which you can define the assigned volumes.
     */
    inline fun volumes(builder: VolumeBuilder.() -> Unit) {

    }
}

fun main() {

    service("mongo:latest") {
        name = "mongo"
        tty = true

        env["DB_USER"] = "test"

        events {
            onStart {

            }
        }

        volumes {
            +("data" to "/data")
        }
    }

}
