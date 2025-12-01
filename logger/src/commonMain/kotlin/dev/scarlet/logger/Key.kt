package dev.scarlet.logger

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

/**
 * A logical identity for a family of [Logger] instances.
 *
 * This interface is typically implemented by a logger's `companion object` to enable
 * type-based operations (e.g., removing all instances of that logger type).
 *
 * Example:
 * ```kotlin
 * class FilterLogger : Logger {
 *     companion object : Key<FilterLogger> by Key.key()
 * }
 *
 * // Removes all FilterLogger instances from the composite logger
 * val logger = Logger.default - FilterLogger
 * ```
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
internal interface Key<T : Logger> {

    /**
     * The Kotlin class associated with the logger type identified by this key.
     */
    val `class`: KClass<T>

    companion object {

        /**
         * Creates a [Key] for the reified logger type [T].
         * Intended for use in `companion object` delegates.
         */
        @JvmStatic
        /* protected */ inline fun <reified T : Logger> key(): Key<T> = Impl(T::class)

        @JvmInline
        protected value class Impl<T : Logger>(override val `class`: KClass<T>) : Key<T>

    }

}