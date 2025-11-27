package dev.scarlet.logger.filter

import dev.scarlet.logger.Content
import dev.scarlet.logger.Key
import dev.scarlet.logger.Key.Companion.key
import dev.scarlet.logger.Logger
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN
import dev.scarlet.logger.log
import kotlin.jvm.JvmInline

/**
 * A filtering [Logger] that wraps another logger and applies a filter to log messages.
 * This interface allows for the creation of filtered loggers, where log messages can be conditionally processed or ignored based on a provided filter.
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
internal sealed interface FilterLogger : Logger {

    companion object : Key<FilterLogger> by key() {

        fun of(logger: Logger, filter: Filter? = null) =
            when (filter) {
                null -> Default(logger)
                else -> Impl(filter, logger)
            }

    }

    /**
     * The filter applied to log messages.
     */
    val filter: Filter

    /**
     * The [Logger] that this filter is applied to.
     */
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

        override val filter: Filter get() = Filter.default

        override fun toString(): String = "FilterLogger(filter=$filter, logger=$logger)"

    }

    private class Impl(override val filter: Filter, override val logger: Logger) : FilterLogger {

        override fun toString(): String = "FilterLogger(filter=$filter, logger=$logger)"

    }

}