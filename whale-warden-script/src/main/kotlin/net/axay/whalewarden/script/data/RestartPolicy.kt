package net.axay.whalewarden.script.data

class RestartPolicy(
    val name: String,
    val maximumRetryCount: Int,
) {
    companion object {
        val NO_RESTART = RestartPolicy("", 0)
        val ALWAYS = RestartPolicy("always", 0)
        val UNLESS_STOPPED = RestartPolicy("unless-stopped", 0)
        val ON_FAILURE = RestartPolicy("on-failure", 0)
    }
}
