package net.axay.whalewarden.script.data

class RestartPolicy(
    val name: String,
    val maximumRetryCount: Int,
) {
    companion object {
        val ALWAYS = RestartPolicy("always", 0)
    }
}
