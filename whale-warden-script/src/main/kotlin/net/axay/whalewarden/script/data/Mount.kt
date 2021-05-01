package net.axay.whalewarden.script.data

sealed class Mount {
    abstract val source: String
    abstract val type: Type
    abstract val readOnly: Boolean?
    abstract val consistency: Consistency?

    /**
     * Returns this mount, with [readOnly] set to true.
     */
    abstract val ro: Mount

    enum class Type {
        VOLUME, BIND,
    }

    enum class Consistency {
        DEFAULT, CONSISTENT, CACHED, DELEGATED,
    }
}

data class Bind(
    override val source: String,
    override val readOnly: Boolean?,
    override val consistency: Consistency?,
    val propagation: Propagation?,
    val nonRecursive: Boolean?,
) : Mount() {
    override val type = Type.BIND
    override val ro get() = copy(readOnly = true)

    enum class Propagation {
        PRIVATE, RPRIVATE, SHARED, RSHARED, SLAVE, RSLAVE
    }
}

data class Volume(
    override val source: String,
    override val readOnly: Boolean?,
    override val consistency: Consistency?,
    val noCopy: Boolean?,
    val driver: String?,
) : Mount() {
    override val type = Type.VOLUME
    override val ro get() = copy(readOnly = true)
}
