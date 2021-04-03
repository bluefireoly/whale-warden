package net.axay.whalewarden.script.builder

import net.axay.whalewarden.script.builder.api.Builder
import net.axay.whalewarden.script.data.Network

/**
 * Builds a new network.
 *
 * @param name the name of the network
 * @param builder (optional) the [NetworkBuilder]
 */
inline fun network(name: String, builder: NetworkBuilder.() -> Unit = { }) {
    val network = NetworkBuilder(name).apply(builder).internalBuilder.build()
}

class NetworkBuilder(
    val name: String,
) {
    inner class Internal : Builder<Network> {
        override fun build() = Network(name, ipv6, driver, internal, attachable)
    }

    /**
     * INTERNAL! You probably should not use this.
     */
    val internalBuilder = this.Internal()

    /**
     * By settings this to true, you can enable IPv6 on the network.
     */
    var ipv6: Boolean? = null

    /**
     * Name of the network driver plugin to use.
     */
    var driver: String? = null

    /**
     * Restrict external access to the network.
     */
    var internal: Boolean? = null

    /**
     * Globally scoped network is manually attachable by regular containers from workers in swarm mode.
     */
    var attachable: Boolean? = null
}
