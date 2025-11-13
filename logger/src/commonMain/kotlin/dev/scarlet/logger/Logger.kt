package dev.scarlet.logger

import dev.scarlet.logger.Logger.Companion.SYSTEM
import dev.scarlet.logger.Logger.Companion.default
import kotlin.jvm.JvmStatic

@Suppress("ObjectPropertyName")
private var _default: Logger = PlatformLogger

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
 * @version 1.0.0
 */
interface Logger {

    /**
     * The default logger, equivalent to a delegate of [default], providing convenient direct calls:
     *
     * ```
     * Logger.d("Network", "Request sent.")
     * Logger.e("Database", "Failed to open it.", exception)
     * ```
     */
    companion object : Logger by _default {

        private const val TAG = "Logger"

        /**
         * The system-provided default logger implementation.
         *
         * This value is supplied per platform via Kotlin's `expect/actual` mechanism,
         * representing the most basic logging capability of that platform:
         * - **Android**: Uses `android.util.Log`.
         * - **JVM**: Outputs to `System.out` / `System.err` by default; can be bridged to SLF4J, etc., by replacing [default].
         * - **iOS/macOS**: Uses `NSLog`.
         * - **JS**: Outputs to `console.debug` / `console.info` / `console.warn` / `console.error`.
         *
         * @see default
         */
        @JvmStatic
        val SYSTEM: Logger = PlatformLogger

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
        var default: Logger
            get() = _default
            set(value) {
                _default = value.also {
                    it.i(TAG, "Default logger changed to $value.")
                }
            }
    }

    /**
     * Logs a message at the DEBUG level.
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
     * Logs a message at the INFO level.
     *
     * Used for general operational status, key workflow steps, etc.
     *
     * @param tag The log tag.
     * @param msg The log message.
     * @param tr An optional throwable.
     */
    fun i(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Logs a WARN-level message with only a throwable (no explicit message).
     *
     * Equivalent to calling `w(tag, "", tr)`. Useful when only an exception needs to be logged.
     *
     * @param tag The log tag.
     * @param tr The throwable (must be non-null).
     */
    fun w(tag: String, tr: Throwable) = w(tag, "", tr)

    /**
     * Logs a message at the WARN level.
     *
     * Indicates potential issues or unexpected states that do not prevent continued execution.
     *
     * @param tag The log tag.
     * @param msg The warning message (may be an empty string).
     * @param tr An optional throwable.
     */
    fun w(tag: String, msg: String, tr: Throwable? = null)

    /**
     * Logs a message at the ERROR level.
     *
     * Indicates serious failures or exceptions that may impact functionality.
     * It is strongly recommended to pass [tr] when catching exceptions to preserve full context.
     *
     * @param tag The log tag.
     * @param msg The error description.
     * @param tr An optional throwable (recommended to provide).
     */
    fun e(tag: String, msg: String, tr: Throwable? = null)
}