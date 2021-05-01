package net.axay.whalewarden.manager.script

import com.github.dockerjava.api.command.CreateContainerCmd
import com.github.dockerjava.api.model.*
import net.axay.whalewarden.script.data.Bind
import net.axay.whalewarden.script.data.Mount
import net.axay.whalewarden.script.data.Service
import net.axay.whalewarden.script.data.Volume
import com.github.dockerjava.api.model.Mount as DockerMount

fun CreateContainerCmd.applyService(service: Service) = with(service) {
    if (name != null)
        withName(name)
    withTty(tty)
    withEnv(env.map { "${it.key}=${it.value}" })

    val hostConfig = HostConfig()

    if (mounts.isNotEmpty())
        hostConfig.withMounts(mounts.map {
            DockerMount().withTarget(it.target).apply {
                val mount = it.internalMount
                withSource(mount.source)
                withType(mount.type.dockerJavaType)
                withReadOnly(mount.readOnly)
                when (mount) {
                    is Bind -> withBindOptions(BindOptions().apply {
                        if (mount.propagation != null)
                            withPropagation(mount.propagation!!.dockerJavaType)
                    })
                    is Volume -> withVolumeOptions(VolumeOptions().apply {
                        if (mount.noCopy != null)
                            withNoCopy(mount.noCopy!!)
                        if (mount.driver != null)
                            withDriverConfig(Driver().withName(mount.driver))
                    })
                }
            }
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

private val Mount.Type.dockerJavaType get() = when (this) {
    Mount.Type.VOLUME -> MountType.VOLUME
    Mount.Type.BIND -> MountType.BIND
}

private val Bind.Propagation.dockerJavaType get() = when(this) {
    Bind.Propagation.PRIVATE -> BindPropagation.PRIVATE
    Bind.Propagation.RPRIVATE -> BindPropagation.R_PRIVATE
    Bind.Propagation.SHARED -> BindPropagation.SHARED
    Bind.Propagation.RSHARED -> BindPropagation.R_SHARED
    Bind.Propagation.SLAVE -> BindPropagation.SLAVE
    Bind.Propagation.RSLAVE -> BindPropagation.R_SLAVE
}

private val Service.RestartPolicy.dockerJavaType get() = when(type) {
    Service.RestartPolicy.Type.NO_RESTART -> RestartPolicy.noRestart()
    Service.RestartPolicy.Type.ALWAYS -> RestartPolicy.alwaysRestart()
    Service.RestartPolicy.Type.UNLESS_STOPPED -> RestartPolicy.unlessStoppedRestart()
    Service.RestartPolicy.Type.ON_FAILURE -> RestartPolicy.onFailureRestart(maximumRetryCount)
}
