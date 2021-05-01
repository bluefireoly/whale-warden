package net.axay.whalewarden.manager.script

import net.axay.whalewarden.manager.Env
import javax.script.ScriptEngineManager

fun runConfigScript() {
    val scriptSource = buildString {
        appendLine(
            """
                import net.axay.whalewarden.script.builder.service
                import net.axay.whalewarden.script.builder.network
                import net.axay.whalewarden.script.builder.bind
                import net.axay.whalewarden.script.builder.volume
                import net.axay.whalewarden.script.data.Service.RestartPolicy.Type.*
                import net.axay.whalewarden.script.logging.logInfo
            """.trimIndent()
        )
        append(Env.configFile.readText())
    }

    val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    scriptEngine.eval(scriptSource)
}
