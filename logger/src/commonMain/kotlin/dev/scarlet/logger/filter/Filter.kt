@file:JvmName("Filters")
@file:JvmMultifileClass

package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger
import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.filter.Filter.Companion.ALL
import dev.scarlet.logger.filter.Filter.Companion.NONE
import dev.scarlet.logger.filter.Filter.Companion.default
import dev.scarlet.logger.minus
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * A log filter that determines whether a log message should be recorded.
 *
 * [Filter] is a sealed interface with concrete behavior defined by its functional subtypes:
 * - [AnyFilter]: context-free boolean decision (e.g., constant filters);
 * - [LevelFilter]: filters based on log level;
 * - [TagFilter]: filters based on log tag.
 *
 * Predefined common instances are provided:
 * - [ALL]: passes all log messages;
 * - [NONE]: discards all log messages;
 * - [default]: the global default filter, initially set to [ALL].
 *
 * Logger functions such as [Logger.d] and [Logger.i] consult the [default] filter
 * to decide whether to process a log message. Messages rejected by the filter are
 * silently ignored with no runtime overhead.
 *
 * Filters can be combined using operators:
 * - `+` (**OR**): passes if **either** filter allows the message;
 * - `-` (**AND**): passes only if **both** filters allow the message.
 *
 * Example setup:
 * ```kotlin
 * // Allow WARN+ logs from "Network" OR any ERROR+ logs
 * val networkWarn = LevelFilter { it >= Level.WARN } - TagFilter { it == "Network" }
 * val errors = LevelFilter { it >= Level.ERROR }
 * Filter.default = networkWarn + errors
 * ```
 *
 * Later usage:
 * ```kotlin
 * Logger.w("Network") { "Logged (WARN + Network)" }
 * Logger.w("Database") { "Not logged (WARN but not Network, and not ERROR)" }
 * Logger.e("Auth") { "Logged (ERROR+, regardless of tag)" }
 * ```
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
sealed interface Filter {

    companion object {

        private const val TAG = "Filter"

        /**
         * A filter that allows all log messages to pass through.
         */
        val ALL: Filter by lazy { SwitchFilter(true) }

        /**
         * A filter that discards all log messages.
         */
        val NONE: Filter by lazy { SwitchFilter(false) }

        @Suppress("ObjectPropertyName")
        /* VisibleForTesting */internal var _default: Filter? = null

        /**
         * The global default filter.
         *
         * Logger functions such as [Logger.d] and [Logger.i] use this filter to determine
         * whether a log message should be recorded. Messages that do not pass the filter
         * are silently skipped with no runtime overhead.
         *
         * The initial value is chosen based on platform and typical usage:
         * - **Android**: allows all levels (including [Level.DEBUG]);
         * - **iOS/macOS**: allows [Level.INFO] and above;
         * - **JVM (server)**: allows [Level.INFO] and above;
         * - **JavaScript**: allows [Level.WARN] and above.
         *
         * > ⚠️ These defaults are provided for convenience and may be adjusted in future releases
         * > to better align with platform best practices or security considerations.
         * > For stable behavior, explicitly set [default] during application startup.
         *
         * You can assign a custom filter at any time to adjust logging behavior dynamically.
         */
        @JvmStatic
        var default: Filter
            get() = _default ?: platformFilter.also { _default = it }
            set(value) {
                _default = value.also {
                    val logger = Logger.default - FilterLogger
                    logger.i(TAG, "Default filter change to $it.")
                }
            }

        /**
         * Creates a filter that accepts logs at or above the specified [level].
         *
         * For example, [atLeast(Level.INFO)] allows INFO, WARN, and ERROR messages,
         * but excludes DEBUG and VERBOSE.
         *
         * @param level The minimum log level to allow.
         */
        @JvmStatic
        fun atLeast(level: Level): Filter = when (level) {
            Level.DEBUG -> ALL
            else -> LevelFilterImpl(level)
        }

    }

}

/**
 * Platform-specific default filter.
 *
 * Initialized according to platform conventions (e.g., more verbose on Android Debug,
 * stricter on JS or iOS Release).
 */
internal expect val platformFilter: Filter

/**
 * A context-free log filter.
 *
 * This functional interface is used when no log metadata (such as level or tag) is needed—
 * typically for constant or state-based decisions.
 *
 * Commonly used to implement [Filter.ALL] and [Filter.NONE].
 *
 * Example:
 * ```kotlin
 * val alwaysOn = AnyFilter { true }
 * val featureEnabled = AnyFilter { FeatureFlags.loggingEnabled }
 * ```
 */
fun interface AnyFilter : Filter {

    /**
     * Determines whether the current log message should be recorded.
     *
     * Since no contextual information is available, this method usually returns a constant
     * or depends on external state.
     *
     * @return `true` to allow the log, `false` to discard it.
     */
    fun filter(): Boolean

}

@JvmInline
private value class SwitchFilter(val enabled: Boolean) : AnyFilter {
    override fun filter(): Boolean = enabled
    override fun toString(): String = when {
        enabled -> "Filter.ALL"
        else -> "Filter.NONE"
    }
}

/**
 * A log filter based on log level.
 *
 * Use this interface when filtering decisions depend on the [Level] of the log message.
 * Typically created via factory methods like [Filter.atLeast].
 *
 * Example:
 * ```kotlin
 * val warnOnly = LevelFilter { it == Level.WARN }
 * val infoOrAbove = LevelFilter { it >= Level.INFO }
 * ```
 */
fun interface LevelFilter : Filter {

    /**
     * Determines whether a log message with the given [level] should be recorded.
     *
     * @param level The log level of the current message.
     * @return `true` to allow the log, `false` to discard it.
     */
    fun filter(level: Level): Boolean

}

@JvmInline
private value class LevelFilterImpl(val level: Level) : LevelFilter {
    override fun filter(level: Level): Boolean = level >= this.level
    override fun toString(): String = "Filter.atLeast($level)"
}

/**
 * A log filter based on log tag.
 *
 * Useful for filtering logs by module, component, or custom categories using string tags.
 *
 * Example:
 * ```kotlin
 * val networkOnly = TagFilter { it == "Network" }
 * val excludeDebugTags = TagFilter { !it.startsWith("DEBUG_") }
 * ```
 */
fun interface TagFilter : Filter {

    /**
     * Determines whether a log message with the given [tag] should be recorded.
     *
     * @param tag The tag associated with the current log message.
     * @return `true` to allow the log, `false` to discard it.
     */
    fun filter(tag: String): Boolean

}

private fun interface CompositeFilter : Filter {

    fun filter(level: Level, tag: String): Boolean

}

/**
 * Combines this filter with [other] using logical **OR**:
 * a log message is allowed if **either** this filter **or** [other] allows it.
 *
 * This operator is equivalent to calling [or].
 *
 * Example:
 * ```kotlin
 * val isInfo = LevelFilter { it == Level.INFO }
 * val isWarn = LevelFilter { it == Level.WARN }
 * val infoOrWarn = isInfo + isWarn  // Passes INFO or WARN logs
 * ```
 * @see or
 */
operator fun Filter.plus(other: Filter): Filter = or(other)

/**
 * Combines this filter with [other] using logical **OR**.
 *
 * A log message is recorded if it passes **this filter** or the [other] filter.
 *
 * Example:
 * ```kotlin
 * val errors = LevelFilter { it >= Level.ERROR }
 * val networkDebug = LevelFilter { it == Level.DEBUG } - TagFilter { it == "Network" }
 * val combined = errors.or(networkDebug)  // ERROR+ anywhere, or DEBUG from "Network"
 * ```
 * @see plus
 */
@JvmName("any")
fun Filter.or(other: Filter): Filter =
    CompositeFilter { level, tag -> this.filter(level, tag) || other.filter(level, tag) }

/**
 * Combines this filter with [other] using logical **AND**:
 * a log message is allowed only if **both** this filter **and** [other] allow it.
 *
 * This operator is equivalent to calling [and].
 *
 * Example:
 * ```kotlin
 * val atLeastWarn = Filter.atLeast(Level.WARN)
 * val fromNetwork = TagFilter { it == "Network" }
 * val networkWarn = atLeastWarn - fromNetwork  // WARN+ logs AND tag=="Network"
 * ```
 * @see and
 */
operator fun Filter.minus(other: Filter): Filter = and(other)

/**
 * Combines this filter with [other] using logical **AND**.
 *
 * A log message is recorded only if it passes **both** this filter and the [other] filter.
 *
 * Example:
 * ```kotlin
 * val errorsOnly = LevelFilter.atLeast(Level.ERROR)
 * val authModule = TagFilter { it == "Auth" }
 * val authErrors = errorsOnly.and(authModule)  // Only ERROR logs from "Auth"
 * ```
 * @see minus
 */
@JvmName("both")
fun Filter.and(other: Filter): Filter =
    CompositeFilter { level, tag -> this.filter(level, tag) && other.filter(level, tag) }

internal fun Filter.filter(level: Level, tag: String) = when (this) {
    is AnyFilter -> filter()
    is LevelFilter -> filter(level)
    is TagFilter -> filter(tag)
    is CompositeFilter -> filter(level, tag)
}

/**
 * Returns a logger that applies the given [filter] to all log messages.
 *
 * Messages rejected by the filter will not be logged.
 *
 * Example:
 * ```kotlin
 * val logger = ConsoleLogger().withFilter(Filter.atLeast(Level.WARN))
 * logger.d { "Not logged" }
 * logger.w { "Logged" }
 * ```
 */
fun Logger.withFilter(filter: Filter): Logger = FilterLogger.of(this, filter)

/**
 * Returns a logger with all filtering layers removed.
 *
 * This restores the original logging behavior without any filters applied.
 *
 * Example:
 * ```kotlin
 * val base = ConsoleLogger()
 * val filtered = base.withFilter(Filter.atLeast(Level.INFO))
 * val restored = filtered.withoutFilter()  // behaves like [base]
 * ```
 */
fun Logger.withoutFilter(): Logger = when (this) {
    is FilterLogger -> logger
    else -> this
}