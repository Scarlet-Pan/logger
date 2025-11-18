package dev.scarlet.logger.filter

import dev.scarlet.logger.Content
import dev.scarlet.logger.Logger
import kotlin.jvm.JvmInline

/**
 * 过滤式[dev.scarlet.logger.Logger]。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
internal sealed interface FilterLogger : Logger {

    companion object {

        fun of(logger: Logger, filter: Filter? = null) =
            when (filter) {
                null -> Default(logger)
                else -> Impl(filter, logger)
            }

    }

    val filter: Filter

    val logger: Logger

    override fun d(tag: String, msg: String, tr: Throwable?) {
        if (filter.filter()) logger.d(tag, msg, tr)
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        if (filter.filter()) logger.i(tag, msg, tr)
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        if (filter.filter()) logger.w(tag, msg, tr)
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        if (filter.filter()) logger.e(tag, msg, tr)
    }

    fun d(tag: String, lazy: () -> Content) {
        if (filter.filter()) {
            val content = lazy()
            logger.d(tag, content.message, content.throwable)
        }
    }

    fun i(tag: String, lazy: () -> Content) {
        if (filter.filter()) {
            val content = lazy()
            logger.i(tag, content.message, content.throwable)
        }
    }

    fun w(tag: String, lazy: () -> Content) {
        if (filter.filter()) {
            val content = lazy()
            logger.w(tag, content.message, content.throwable)
        }
    }

    fun e(tag: String, lazy: () -> Content) {
        if (filter.filter()) {
            val content = lazy()
            logger.e(tag, content.message, content.throwable)
        }
    }

    @JvmInline
    private value class Default(override val logger: Logger) : FilterLogger {

        override val filter: Filter
            get() = Filter.default

        override fun toString(): String = "DefaultFilterLogger(logger=$logger)"

    }

    private class Impl(override val filter: Filter, override val logger: Logger) : FilterLogger {

        override fun toString(): String = "FilterLogger(filter=$filter, logger=$logger)"

    }

}