@file:OptIn(ExperimentalObjCName::class)

package dev.scarlet.logger

import dev.scarlet.logger.Logger.Companion.SYSTEM
import dev.scarlet.logger.Logger.Companion.default
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN
import kotlin.experimental.ExperimentalObjCName
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.native.ObjCName

/**
 * A logging interface.
 *
 * Defines a unified API for logging at four levels: DEBUG, INFO, WARN, and ERROR.
 * This interface contains no logging logic itself—actual behavior is determined by its implementations.
 *
 * By default, the [companion object] delegates to [Logger.default], enabling direct usage like:
 * ```kotlin
 * Logger.d("Network", "Request sent.")
 * Logger.e("Database", "Failed to open it.", exception)
 * ```
 * The initial value of [Logger.default] is [SYSTEM].
 * Users may replace [default] to customize log routing (e.g., file, network, filtering, or composition).
 *
 * All methods are thread-safe (as guaranteed by implementations) and can be called from any thread or coroutine.
 *
 * @author Scarlet Pan
 * @version 1.0.1
 */
@ObjCName("Logging", exact = true)
interface Logger {

    /**
     * The default logger, equivalent to a delegate of [default], providing convenient direct calls:
     *
     * ```
     * Logger.d("Network", "Request sent.")
     * Logger.e("Database", "Failed to open it.", exception)
     * ```
     */
    @ObjCName("Logger", exact = true)
    companion object : AbsLogger() {

        private const val TAG = "Logger"

        /**
         * The system-provided default logger implementation.
         *
         * This value is supplied per platform via Kotlin's `expect/actual` mechanism,
         * representing the most basic logging capability of that platform:
         * - **Android**: Uses `android.util.Log`.
         * - **JVM**: Outputs to `System.out` / `System.err`; can be replaced to integrate with SLF4J, Logback, etc.
         * - **iOS/macOS**: Prints formatted log messages to standard output (visible in Xcode console).
         * - **JS**: Delegates to `console.debug`, `console.info`, `console.warn`, and `console.error`.
         *
         * @see default
         */
        @JvmStatic
        val SYSTEM: Logger = PlatformLogger
            @JvmName("getSystem") get

        /**
         * The global default logger instance.
         *
         * Initialized to [SYSTEM]. All calls to the companion object (e.g., `Logger.d()`)
         * are delegated to the current value of this property.
         *
         * Assigning a new value takes effect immediately and logs an INFO message via the new logger itself,
         * aiding runtime diagnostics of logger pipeline changes.
         *
         * Example:
         * ```kotlin
         * Logger.default = Logger.SYSTEM + FileLogger
         * Logger.i("App", "Now logging to multiple destinations")
         * ```
         *
         * @see SYSTEM
         */
        @JvmStatic
        var default: Logger = SYSTEM
            set(value) {
                field = value.also {
                    it.i(TAG, "Default logger changed to $value.")
                }
            }

        override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
            default.log(level, tag, msg, tr)
        }
    }

    /**
     * Logs a message at the [Level.DEBUG].
     *
     * Typically used for detailed diagnostic information during development.
     * Whether the message is output depends on the specific implementation.
     *
     * @param tag A short string identifying the source of the log message.
     * @param msg The log message (non-null).
     * @param tr An optional throwable; if provided, the implementation should format and append its stack trace.
     */
    fun d(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Logs a message at the [Level.INFO].
     *
     * Used for general operational status, key workflow steps, etc.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An optional throwable.
     */
    fun i(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Logs a [Level.WARN] message with only a throwable (no explicit message).
     *
     * Equivalent to calling `w(tag, "", tr)`. Useful when only an exception needs to be logged.
     *
     * @param tag The log tag.
     * @param tr The throwable (must be non-null).
     */
    fun w(tag: String, tr: Throwable) = w(tag, "", tr)

    /**
     * Logs a message at the [Level.WARN].
     *
     * Indicates potential issues or unexpected states that do not prevent continued execution.
     *
     * @param tag The log tag.
     * @param msg The warning message (may be an empty string).
     * @param tr An optional throwable.
     */
    fun w(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Logs a message at the [Level.ERROR].
     *
     * Indicates serious failures or exceptions that may impact functionality.
     * It is strongly recommended to pass [tr] when catching exceptions to preserve full context.
     *
     * @param tag The log tag.
     * @param msg The error description.
     * @param tr An optional throwable (recommended to provide).
     */
    fun e(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Log severity levels.
     *
     * These levels indicate the importance and intended audience of a log message.
     * They are ordered from least to most severe:
     * [DEBUG] < [INFO] < [WARN] < [ERROR].
     *
     * Example usage:
     * ```kotlin
     * Logger.d("App", "Initializing database connection...") // DEBUG
     * Logger.i("App", "Service started successfully.")       // INFO
     * Logger.w("App", "Config file missing; using defaults.") // WARN
     * Logger.e("App", "Failed to connect to database.")      // ERROR
     * ```
     *
     * @author Scarlet Pan
     * @since 1.0.0
     */
    enum class Level {

        /**
         * Detailed diagnostic information, typically useful only during development or troubleshooting.
         *
         * Use sparingly in production, as excessive debug logs can impact performance and readability.
         */
        DEBUG,

        /**
         * General operational messages that confirm expected behavior (e.g., service startup, state changes).
         *
         * Suitable for routine events that help trace application flow without overwhelming the log stream.
         */
        INFO,

        /**
         * Indicates an unexpected or unusual condition that might become a problem later,
         * but the system is still functioning normally.
         *
         * Examples: missing optional configuration, fallback to default behavior, deprecated API usage.
         */
        WARN,

        /**
         * Represents a serious failure that prevents a feature or operation from completing successfully.
         *
         * Always investigate errors—they typically indicate bugs, misconfigurations, or external failures.
         */
        ERROR
    }

}