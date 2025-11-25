@file:OptIn(ExperimentalObjCName::class)

package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN
import kotlin.experimental.ExperimentalObjCName

/**
 * An abstract base implementation of the [Logger] interface that provides default
 * implementations for debug ([d]), info ([i]), warn ([w]), and error ([e]) logging methods.
 *
 * Subclasses are only required to implement the single abstract method [log],
 * which handles the actual logging logic for a given [Level], tag, message, and optional throwable.
 *
 * This design centralizes log routing and reduces boilerplate in concrete logger implementations
 * across Kotlin Multiplatform targets (e.g., Android, iOS, JVM).
 *
 * ### Example
 * ```kotlin
 * class ConsoleLogger : AbsLogger() {
 *     override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
 *         val output = "[${level.name}/$tag] $msg" + if (tr != null) " ${tr.stackTraceToString()}" else ""
 *         println(output)
 *     }
 * }
 * ```
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
abstract class AbsLogger : Logger {

    override fun d(tag: String, msg: String, tr: Throwable?) {
        log(DEBUG, tag, msg, tr)
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        log(INFO, tag, msg, tr)
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        log(WARN, tag, msg, tr)
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        log(ERROR, tag, msg, tr)
    }

    /**
     * Performs the actual logging operation.
     *
     * This method must be implemented by subclasses to define how log records
     * are processed, formatted, or sent (e.g., to console, file, remote service).
     *
     * @param level The log severity level (e.g., [DEBUG], [INFO], [WARN], [ERROR]).
     * @param tag A short identifier for the source of the log (e.g., class name).
     * @param msg The human-readable log message.
     * @param tr An optional [Throwable] to include stack trace information.
     */
    protected abstract fun log(level: Level, tag: String, msg: String, tr: Throwable? = null)

}