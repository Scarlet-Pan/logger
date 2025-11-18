@file:OptIn(ExperimentalObjCName::class)

package dev.scarlet.logger

import kotlin.experimental.ExperimentalObjCName

/**
 * Unified logging facade for Kotlin Multiplatform and iOS (Swift).
 *
 * This file provides a set of public top-level functions that delegate to [Logger.default].
 * They are designed to be called directly from:
 * - Kotlin Multiplatform code (common, Android, JVM, etc.)
 * - Swift on iOS/macOS via the generated `.xcframework`
 *
 * All functions are thread-safe and mirror the behavior of the underlying [Logger] implementation.
 *
 * ## Swift Usage
 *
 * ```swift
 * import loggerKit
 *
 * DefaultLogger.d(tag: "Network", message: "Request sent")
 * DefaultLogger.e(tag: "Database", message: "Failed to open", error: error)
 * ```
 *
 * > **Note**: The optional `throwable: Throwable?` parameter is mapped to Swift's `Error?`.
 * > If you pass an exception in Kotlin, it appears as an `Error` in Swift.
 */
@ObjCName("Logger", exact = true)
object DefaultLogger {

    /**
     * Logs a debug-level message.
     *
     * Typically used for detailed diagnostic information during development.
     * Output depends on the current [Logger] implementation (e.g., may be filtered out in release builds).
     *
     * @param tag A short string identifying the source (e.g., class or module name). Must not be null.
     * @param message The log content. Must not be null.
     * @param throwable An optional exception to include stack trace.
     *
     * ## Swift Example
     * ```swift
     * Logger.d(tag: "Auth", message: "User token refreshed")
     * Logger.d(tag: "Parser", message: "Fallback used", error: error)
     * ```
     */
    fun d(tag: String, message: String, throwable: Throwable? = null) =
        Logger.default.d(tag, message, throwable)

    /**
     * Logs an informational message.
     *
     * Indicates normal, important events in the application lifecycle
     * (e.g., app start, user login, network connected).
     *
     * @param tag Source tag.
     * @param message Informative message.
     * @param throwable An optional exception.
     *
     * ## Swift Example
     * ```swift
     * Logger.i(tag: "App", message: "Application launched")
     * Logger.i(tag: "Cache", message: "Recovered from corrupted cache", error: error)
     * ```
     */
    fun i(tag: String, message: String, throwable: Throwable? = null) =
        Logger.default.i(tag, message, throwable)

    /**
     * Logs a warning message.
     *
     * Indicates unexpected or unusual conditions that are not errors,
     * but may indicate future problems (e.g., deprecated API usage, fallback triggered).
     *
     * @param tag Source tag.
     * @param message Warning description. Defaults to an empty string if not provided.
     * @param throwable An optional exception.
     *
     * ## Swift Example
     * ```swift
     * Logger.w(tag: "UI", message: "Missing image placeholder used")
     * Logger.w(tag: "Network", error: error) // message defaults to ""
     * ```
     */
    fun w(tag: String, message: String = "", throwable: Throwable? = null) =
        Logger.default.w(tag, message, throwable)

    /**
     * Logs an error-level message.
     *
     * Represents serious issues that impact functionality.
     * Even without an exception, this level should be used for critical failures.
     *
     * @param tag Source tag.
     * @param message Error description.
     * @param throwable An optional exception (recommended to provide).
     *
     * ## Swift Example
     * ```swift
     * Logger.e(tag: "Payment", message: "Invalid card number")
     * Logger.e(tag: "Storage", message: "Failed to persist user data", error: error)
     * ```
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) =
        Logger.default.e(tag, message, throwable)

}