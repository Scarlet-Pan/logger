package dev.scarlet.logger

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic

/**
 * Represents a structured log content that may include a message and an optional associated [Throwable].
 *
 * This sealed interface enables type-safe handling of different kinds of log payloads.
 * It supports two variants:
 * - A simple [Message] containing only a string.
 * - An [Error] containing a message and a non-null [Throwable] (e.g., for exceptions).
 *
 * ### Usage Examples
 * ```kotlin
 * // Simple message
 * val content1 = Content.of("User logged in")
 *
 * // Message with exception
 * val content2 = Content.of("Failed to load data", IOException("Network down"))
 *
 * // Infix-style creation
 * val content3 = "Database connection failed" with SQLException("Timeout")
 * ```
 *
 * The companion object provides factory methods to construct instances safely and idiomatically.
 *
 * @see Message
 * @see Error
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
sealed interface Content {

    companion object {

        /**
         * Creates a [Content] instance from a plain log message.
         *
         * This internal method is used by the logging framework to wrap string-only logs.
         * External users should prefer public overloads or extension functions.
         *
         * @param msg the log message string
         * @return a [Message] instance wrapping the given string
         */
        internal fun of(msg: String): Content = Message(msg)

        /**
         * Creates a [Content] instance that includes both a message and a [Throwable].
         *
         * Useful for logging exceptions while preserving stack trace information.
         *
         * @param msg the descriptive log message
         * @param tr the associated exception or error
         * @return an [Error] instance containing both message and throwable
         */
        @JvmStatic
        fun of(msg: String, tr: Throwable): Content = Error(msg, tr)

        /**
         * Extension function that associates a [Throwable] with a log message string.
         *
         * Provides a fluent, readable way to create error logs:
         * ```kotlin
         * "Failed to parse config" with e
         * ```
         *
         * @receiver the log message
         * @param exception the [Throwable] to attach
         * @return a [Error] instance
         */
        infix fun String.with(exception: Throwable) = of(this, exception)

    }

    /**
     * The primary log message text.
     */
    val message: String

    /**
     * An optional [Throwable] associated with this log entry (e.g., an exception).
     *
     * Returns `null` if no throwable is present (as in [Message]).
     * Non-null for [Error] instances.
     */
    val throwable: Throwable? get() = null

}

/**
 * A lightweight wrapper for a plain log message without any associated exception.
 *
 * Implemented as a `@JvmInline value class` to avoid runtime allocation overhead on JVM,
 * while still being compatible with Kotlin/Native and JS.
 *
 * @property message the log message string
 */
@JvmInline
private value class Message(override val message: String) : Content {
    override fun toString(): String = message
}

/**
 * Represents a log entry that includes both a message and a non-null [Throwable].
 *
 * Typically used when logging caught exceptions or system errors.
 *
 * @property message the descriptive log message
 * @property throwable the associated exception or error (never null)
 */
private data class Error(override val message: String, override val throwable: Throwable) : Content
