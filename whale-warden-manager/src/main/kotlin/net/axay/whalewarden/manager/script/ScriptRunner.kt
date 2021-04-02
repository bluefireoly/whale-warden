package net.axay.whalewarden.manager.script

import net.axay.whalewarden.manager.Env
import javax.script.ScriptEngineManager

private val defaultImports = """
    import net.axay.whalewarden.script.*
    import net.axay.whalewarden.script.RestartPolicy.*
""".trimIndent()

fun runConfigScript() {
    val scriptSource = buildString {
        appendLine("import net.axay.whalewarden.script.*")
        append(Env.configFile.readText())
    }

    val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
    scriptEngine.eval(scriptSource)
}
