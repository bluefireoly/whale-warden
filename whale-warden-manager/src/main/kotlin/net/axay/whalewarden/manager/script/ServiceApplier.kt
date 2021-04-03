package net.axay.whalewarden.manager.script

import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.*
import net.axay.whalewarden.script.data.Service

fun CreateContainerCmd.applyService(service: Service) = with(service) {
    val hostConfig = HostConfig()

    if (name != null)
        withName(name)
    withTty(tty)
    withEnv(env.map { "${it.key}=${it.value}" })
    if (mounts.isNotEmpty())
        hostConfig.withMounts(mounts.map {
            Mount().withSource(it.source).withTarget(it.target)
                .withType(it.type.dockerJavaType)
                .withReadOnly(it.readOnly)
        })
    if (ports.isNotEmpty())
        hostConfig.withPortBindings(
            ports.map { PortBinding(Ports.Binding.bindPort(it.key), ExposedPort(it.value)) }
        )
    if (restartPolicy != null)
        hostConfig.withRestartPolicy(restartPolicy!!.dockerJavaType)

    withHostConfig(hostConfig)

    this@applyService
}

private val Service.Mount.Type.dockerJavaType get() = when (this) {
    Service.Mount.Type.VOLUME -> MountType.VOLUME
    Service.Mount.Type.BIND -> MountType.BIND
}

private val Service.RestartPolicy.dockerJavaType get() = when(type) {
    Service.RestartPolicy.Type.NO_RESTART -> RestartPolicy.noRestart()
    Service.RestartPolicy.Type.ALWAYS -> RestartPolicy.alwaysRestart()
    Service.RestartPolicy.Type.UNLESS_STOPPED -> RestartPolicy.unlessStoppedRestart()
    Service.RestartPolicy.Type.ON_FAILURE -> RestartPolicy.onFailureRestart(maximumRetryCount)
}
