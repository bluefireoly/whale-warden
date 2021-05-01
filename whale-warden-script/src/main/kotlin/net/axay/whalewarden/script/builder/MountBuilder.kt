package net.axay.whalewarden.script.builder

import net.axay.whalewarden.script.builder.api.Builder
import net.axay.whalewarden.script.data.Bind
import net.axay.whalewarden.script.data.Mount
import net.axay.whalewarden.script.data.Volume
import net.axay.whalewarden.script.registry.Registry

/**
 * Builds a new bind.
 *
 * @param path the path on the host system
 * @param builder (optional) the [BindBuilder]
 */
inline fun bind(path: String, builder: BindBuilder.() -> Unit = { }) =
    BindBuilder(path).apply(builder).internalBuilder.build()

/**
 * Builds a new volume.
 *
 * @param name the name of the volume
 * @param builder (optional) the [VolumeBuilder]
 */
inline fun volume(name: String, builder: VolumeBuilder.() -> Unit = { }): Volume {
    val volume = VolumeBuilder(name).apply(builder).internalBuilder.build()
    Registry.volumes += volume
    return volume
}

abstract class MountBuilder {
    /**
     * Whether the mount should be read-only.
     */
    var readOnly: Boolean? = null

    /**
     * The consistency requirement for the mount.
     */
    var consistency: Mount.Consistency? = null
}

class BindBuilder(
    val path: String,
) : MountBuilder() {
    inner class Internal : Builder<Bind> {
        override fun build() = Bind(path, readOnly, consistency, propagation, nonRecursive)
    }

    /**
     * INTERNAL! You probably should not use this.
     */
    val internalBuilder = this.Internal()

    /**
     * A propagation mode with the value (r)private, (r)shared, or (r)slave.
     */
    var propagation: Bind.Propagation? = null

    /**
     * Disable recursive bind mount.
     */
    var nonRecursive: Boolean? = null
}


class VolumeBuilder(
    val name: String,
) : MountBuilder() {
    inner class Internal : Builder<Volume> {
        override fun build() = Volume(name, readOnly, consistency, noCopy, driver)
    }

    /**
     * INTERNAL! You probably should not use this.
     */
    val internalBuilder = this.Internal()

    /**
     * Populate volume with data from the target.
     */
    var noCopy: Boolean? = null

    /**
     * Name of the driver to use to create the volume.
     */
    var driver: String? = null
}
