@file:JvmName("Loggers")
@file:JvmMultifileClass

package dev.scarlet.logger

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A composite logger that combines two [Logger] instances.
 *
 * All log calls are delegated to both [head] and [tail].
 *
 * @author Scarlet Pan
 * @version 1.0.1
 */
internal class CompositeLogger(
    private val head: Logger,
    private val tail: Logger,
) : AbsLogger() {

    private val properties: List<Logger> by lazy {
        val list = mutableListOf<Logger>()
        if (head is CompositeLogger) list.addAll(head.properties) else list.add(head)
        if (tail is CompositeLogger) list.addAll(tail.properties) else list.add(tail)
        list
    }

    override fun toString(): String = properties.toString()

    override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
        head.log(level, tag, msg, tr)
        tail.log(level, tag, msg, tr)
    }

}

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