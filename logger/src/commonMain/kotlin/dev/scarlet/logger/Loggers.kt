@file:JvmName("Loggers")
@file:JvmMultifileClass

package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads

/**
 * Logs a message with the specified log level, tag, message, and optional throwable.
 *
 * This is a convenience extension function that routes the log call to the appropriate
 * platform-specific logging method based on the provided [level]:
 * - [Level.DEBUG] → [dev.scarlet.logger.Logger.d]
 * - [Level.INFO]  → [dev.scarlet.logger.Logger.i]
 * - [Level.WARN]  → [dev.scarlet.logger.Logger.w]
 * - [Level.ERROR] → [dev.scarlet.logger.Logger.e]
 *
 * @param level The severity level of the log message. Must not be null.
 * @param tag A short string identifying the source of the log message (e.g., class name).
 * @param msg The actual log message to be recorded.
 * @param tr An optional [Throwable] associated with the log event (e.g., for stack traces).
 *           If provided, it will be included in the output where supported by the platform.
 *
 * @see Level
 * @see dev.scarlet.logger.Logger.d
 * @see dev.scarlet.logger.Logger.i
 * @see dev.scarlet.logger.Logger.w
 * @see dev.scarlet.logger.Logger.e
 */
@JvmOverloads
fun Logger.log(level: Level, tag: String, msg: String, tr: Throwable? = null) = when (level) {
    Level.DEBUG -> d(tag, msg, tr)
    Level.INFO -> i(tag, msg, tr)
    Level.WARN -> w(tag, msg, tr)
    Level.ERROR -> e(tag, msg, tr)
}