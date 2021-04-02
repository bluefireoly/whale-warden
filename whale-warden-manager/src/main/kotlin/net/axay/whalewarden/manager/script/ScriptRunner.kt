package net.axay.whalewarden.manager.script

import net.axay.whalewarden.manager.Env
import javax.script.ScriptEngineManager

fun main() = runConfigScript()

fun runConfigScript() {
    val scriptSource = buildString {
        appendLine(
            """
                import net.axay.whalewarden.script.builder.service
                import net.axay.whalewarden.script.data.RestartPolicy.Companion.ALWAYS
                import net.axay.whalewarden.script.data.RestartPolicy.Companion.NO_RESTART
                import net.axay.whalewarden.script.data.RestartPolicy.Companion.ON_FAILURE
                import net.axay.whalewarden.script.data.RestartPolicy.Companion.UNLESS_STOPPED
                import net.axay.whalewarden.script.logging.logInfo.*
            """.trimIndent()
        )
        append(Env.configFile.readText())
    }

    val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    scriptEngine.eval(scriptSource)
}
