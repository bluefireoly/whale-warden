package net.axay.whalewarden.common.logging

/**
 * Logs an information. The implementation of this function may change.
 */
fun logInfo(info: String) = println("INFO > $info")

/**
 * Logs an error. The implementation of this function may change.
 */
fun logError(info: String) = System.err.println("ERROR > $info")

/**
 * Logs this exception without the stacktrace, using [logError].
 */
fun Exception.logError() = logError("(${this::class.simpleName}) $message")
