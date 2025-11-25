package dev.scarlet.logger.filter

import dev.scarlet.logger.Content
import dev.scarlet.logger.Logger
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN
import dev.scarlet.logger.filter.CompositeFilter.Companion.filter
import dev.scarlet.logger.log
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

    override fun d(tag: String, msg: String, tr: Throwable?) = log(DEBUG, tag, msg, tr)

    fun d(tag: String, lazy: () -> Content) = log(DEBUG, tag, lazy)

    override fun i(tag: String, msg: String, tr: Throwable?) = log(INFO, tag, msg, tr)

    fun i(tag: String, lazy: () -> Content) = log(INFO, tag, lazy)

    override fun w(tag: String, msg: String, tr: Throwable?) = log(WARN, tag, msg, tr)

    fun w(tag: String, lazy: () -> Content) = log(WARN, tag, lazy)

    override fun e(tag: String, msg: String, tr: Throwable?) = log(ERROR, tag, msg, tr)

    fun e(tag: String, lazy: () -> Content) = log(ERROR, tag, lazy)

    fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
        if (filter.filter(level, tag)) logger.log(level, tag, msg, tr)
    }

    fun log(level: Logger.Level, tag: String, lazy: () -> Content) {
        if (filter.filter(level, tag)) lazy().let {
            logger.log(level, tag, it.message, it.throwable)
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