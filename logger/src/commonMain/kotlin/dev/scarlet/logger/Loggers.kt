@file:JvmName("Loggers")

package dev.scarlet.logger

import kotlin.jvm.JvmName

/**
 * Combines this logger with another [Logger], returning a new composite logger.
 *
 * The returned [Logger] dispatches every log call **sequentially** to both original loggers:
 * first to `this`, then to [other].
 *
 * Chaining is supported. For example:
 * ```kotlin
 * Logger.default = Logger.SYSTEM + FileLogger + NetworkLogger
 * ```
 *
 * The resulting logger fully adheres to the [Logger] interface and can be further composed
 * or assigned to [Logger.default].
 *
 * @param other The logger to combine with this one.
 * @return A new [Logger] instance that sends each log message first to `this`, then to [other].
 */
@JvmName("combine")
operator fun Logger.plus(other: Logger): Logger = CompositeLogger(this, other)