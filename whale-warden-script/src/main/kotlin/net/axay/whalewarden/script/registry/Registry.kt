package net.axay.whalewarden.script.registry

import net.axay.whalewarden.script.data.Network
import net.axay.whalewarden.script.data.Service
import net.axay.whalewarden.script.data.Volume

object Registry {
    val services = mutableListOf<Service>()
    val networks = mutableListOf<Network>()
    val volumes = mutableListOf<Volume>()
}
