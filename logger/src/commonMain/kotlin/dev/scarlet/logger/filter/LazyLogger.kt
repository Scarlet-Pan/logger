@file:JvmName("Filters")
@file:JvmMultifileClass
@file:OptIn(ExperimentalTypeInference::class)

package dev.scarlet.logger.filter

import dev.scarlet.logger.Content
import dev.scarlet.logger.Content.Companion.with
import dev.scarlet.logger.Logger
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * Logs a debug message lazily using a lambda that returns a [String].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * This avoids unnecessary computation (e.g., string formatting or object serialization) when the log would be discarded.
 *
 * Example:
 * ```kotlin
 * Logger.d("Network") { "Request body: $largePayload" }
 * ```
 *
 * @param tag A label identifying the source of the log message.
 * @param lazy A lambda producing the log message. Not invoked if the effective filter rejects the log.
 */
@JvmName("debugLazyMessage")
fun Logger.d(tag: String, lazy: () -> String) = d(tag, lazy.asContent())

/**
 * Logs a debug message lazily using a lambda that returns a [dev.scarlet.logger.Content].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * This overload supports structured logging with exceptions or metadata.
 *
 * Example:
 * ```kotlin
 * val e = IOException("Timeout")
 * Logger.d("Network") { "Failed to connect" with e }
 * ```
 *
 * @param tag A label identifying the source of the log message.
 * @param lazy A lambda returning a [dev.scarlet.logger.Content] instance, typically built using [dev.scarlet.logger.Content.Companion.with].
 */
@OverloadResolutionByLambdaReturnType
@JvmName("debugLazyContent")
fun Logger.d(tag: String, lazy: () -> Content) = asFilter().d(tag, lazy)

/**
 * Logs an info message lazily using a lambda that returns a [String].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * Example:
 * ```kotlin
 * Logger.i("App") { "User ${user.id} logged in from ${user.ip}" }
 * ```
 *
 * @param tag A label identifying the source of the log message.
 * @param lazy A lambda producing the log message. Not invoked if the effective filter rejects the log.
 */
@JvmName("infoLazyMessage")
fun Logger.i(tag: String, lazy: () -> String) = i(tag, lazy.asContent())

/**
 * Logs an info message lazily using a lambda that returns a [Content].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * Useful for attaching contextual data or diagnostics at info level.
 *
 * Example:
 * ```kotlin
 * Logger.i("Migration") { "Database upgraded to v3" with e }
 * ```
 *
 * @param tag A label identifying the source of the log message.
 * @param lazy A lambda returning a [Content] instance, often built with [Content.Companion.with].
 */
@OverloadResolutionByLambdaReturnType
@JvmName("infoLazyContent")
fun Logger.i(tag: String, lazy: () -> Content) = asFilter().i(tag, lazy)

/**
 * Logs a warning message lazily using a lambda that returns a [String].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * Example:
 * ```kotlin
 * Logger.w("Cache") { "Evicted ${cache.size} entries due to memory pressure" }
 * ```
 *
 * @param tag A label identifying the component issuing the warning.
 * @param lazy A lambda producing the warning message. Skipped if the effective filter rejects the log.
 */
@JvmName("warnLazyMessage")
fun Logger.w(tag: String, lazy: () -> String) = w(tag, lazy.asContent())

/**
 * Logs a warning message lazily using a lambda that returns a [Content].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * Ideal for warnings that include recoverable errors or diagnostic context.
 *
 * Example:
 * ```kotlin
 * val fallback = loadFallbackConfig()
 * Logger.w("Config") { "Primary config missing, using fallback" with e }
 * ```
 *
 * @param tag A label identifying the source of the warning.
 * @param lazy A lambda returning a [Content] object containing message and optional exception.
 */
@OverloadResolutionByLambdaReturnType
@JvmName("warnLazyContent")
fun Logger.w(tag: String, lazy: () -> Content) = asFilter().w(tag, lazy)

/**
 * Logs an error message lazily using a lambda that returns a [String].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * Example:
 * ```kotlin
 * Logger.e("Payment") { "Transaction failed for user ${userId}: ${transaction.details}" }
 * ```
 *
 * @param tag A label identifying the component where the error occurred.
 * @param lazy A lambda producing the error description. Evaluated only if the effective filter permits the log.
 */
@JvmName("errorLazyMessage")
fun Logger.e(tag: String, lazy: () -> String) = e(tag, lazy.asContent())

/**
 * Logs an error message lazily using a lambda that returns a [Content].
 *
 * The [lazy] lambda is evaluated **only if the effective [Filter] permits this log entry**.
 *
 * The effective filter is determined as follows:
 * - If the current [Logger] holds a dedicated [Filter] (e.g., created via [Logger.withFilter]), that filter is used;
 * - Otherwise, [Filter.default] is used.
 *
 * This is the recommended way to log errors with associated exceptions,
 * as it defers message construction and exception processing until necessary.
 *
 * Example:
 * ```kotlin
 * try {
 *     process(data)
 * } catch (e: Exception) {
 *     Logger.e("Processor") { "Failed to handle input" with e }
 * }
 * ```
 *
 * @param tag A label identifying the error source.
 * @param lazy A lambda returning a [Content] encapsulating the message and exception.
 */
@OverloadResolutionByLambdaReturnType
@JvmName("errorLazyContent")
fun Logger.e(tag: String, lazy: () -> Content) = asFilter().e(tag, lazy)

private fun Logger.asFilter() = this as? FilterLogger ?: FilterLogger.of(this)

private fun (() -> String).asContent() = { Content.of(msg = this()) }