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
 * @version 1.1.0
 */
internal data class CompositeLogger(
    val head: Logger,
    val tail: Logger,
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

/**
 * Returns a new [Logger] that excludes the **first occurrence** of a logger equal to the specified [other],
 * as determined by the `==` operator.
 *
 * This is typically used with **singleton logger objects** (e.g., `object NetworkLogger : AbsLogger()`),
 * where `other` is the singleton itself.
 *
 * - If `this` is a single logger and `this == other`, returns [EmptyLogger].
 * - If `this` is a composite logger, only the **first matching logger** (`logger == other`) is removed.
 * - If no match is found, returns the original logger unchanged.
 * - If removal results in zero loggers, returns [EmptyLogger].
 * - If exactly one logger remains, it is returned directly (unwrapped).
 *
 * This operation is **non-destructive**.
 *
 * Example setup:
 * ```kotlin
 * Logger.default = Logger.SYSTEM + FileLogger + NetworkLogger
 * ```
 *
 * Later usage:
 * ```kotlin
 * val logger = Logger.default - NetworkLogger  // removes the NetworkLogger singleton
 * ```
 *
 * > Note: Since most logger singletons do not override `equals()`, `==` behaves as reference equality.
 *
 * @param other The logger instance to remove (e.g., a singleton object). Only the first `==` match is removed.
 * @return A new [Logger] with the first matching occurrence excluded.
 */
@JvmName("exclude")
operator fun Logger.minus(other: Logger): Logger = when (this) {
    other -> EmptyLogger
    is CompositeLogger -> this - other
    else -> this
}

private operator fun CompositeLogger.minus(other: Logger): Logger {
    if (other is CompositeLogger) {
        return this - other.head - other.tail
    }
    val head = head - other
    if (head == EmptyLogger) {
        return tail
    }
    val tail = tail - other
    return when {
        tail == EmptyLogger -> head
        head == this.head && tail == this.tail -> this
        else -> CompositeLogger(head, tail)
    }
}

/**
 * Returns a new [Logger] that excludes **all loggers** associated with the given [key].
 *
 * This is designed for **logger types that may have multiple instances**, where the [key] is typically
 * their `companion object` implementing [Key]. All instances whose logical identity matches [key] are removed.
 *
 * Behavior:
 * - **All matching instances** are removed (not just the first).
 * - Matching is based on logical identity: a logger matches if its class's companion `== key`.
 * - If no logger matches, the original logger is returned unchanged.
 * - If all loggers are removed, returns [EmptyLogger].
 * - If one logger remains, it is returned directly.
 *
 * This operation is **non-destructive**.
 *
 * Example setup:
 * ```kotlin
 * Logger.default = Logger.SYSTEM + FileLogger("debug") + FileLogger("release")
 * ```
 *
 * Later usage:
 * ```kotlin
 * val logger = Logger.default - FileLogger  // removes all FileLogger instances
 * ```
 *
 * @param key A [Key] representing a logger type (typically a companion object like `FileLogger`).
 * @return A new [Logger] with all instances matching [key] excluded.
 */
internal operator fun Logger.minus(key: Key<out Logger>): Logger = when {
    key.`class`.isInstance(this) -> EmptyLogger
    this is CompositeLogger -> this - key
    else -> this
}

private operator fun CompositeLogger.minus(key: Key<out Logger>): Logger {
    val head = head - key
    val tail = tail - key
    return when {
        head == this.head && tail == this.tail -> this
        head == EmptyLogger && tail == EmptyLogger -> EmptyLogger
        head == EmptyLogger -> tail
        tail == EmptyLogger -> head
        else -> CompositeLogger(head, tail)
    }
}

/**
 * A no-op logger that discards all log messages.
 * Used as the result when a composite logger becomes empty.
 */
internal object EmptyLogger : AbsLogger() {
    override fun toString(): String = "EmptyLogger"
    override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) = Unit
}