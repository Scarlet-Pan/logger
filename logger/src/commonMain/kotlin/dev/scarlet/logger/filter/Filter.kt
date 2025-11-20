@file:JvmName("Filters")
@file:JvmMultifileClass

package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * 日志过滤器。
 */
fun interface Filter {

    companion object {

        val ALL = Filter { true }
        val NONE = Filter { false }

        var default: Filter = ALL

        // TODO: LevelFilter

    }

    fun filter(): Boolean

}

private object DefaultFilter : Filter { // TODO: expect
    override fun filter(): Boolean {
        return true
    }
}

@JvmName("any")
operator fun Filter.plus(other: Filter): Filter = { this.filter() || other.filter() } as Filter

@JvmName("both")
operator fun Filter.minus(other: Filter): Filter = { this.filter() && other.filter() } as Filter

@JvmName("of")
fun Logger.withFilter(filter: Filter): Logger = FilterLogger.of(this, filter)