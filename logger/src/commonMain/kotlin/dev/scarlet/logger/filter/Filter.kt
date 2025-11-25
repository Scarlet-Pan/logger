@file:JvmName("Filters")
@file:JvmMultifileClass

package dev.scarlet.logger.filter

import dev.scarlet.logger.Logger
import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.filter.CompositeFilter.Companion.filter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * 日志过滤器。
 */
sealed interface Filter {

    companion object {

        val ALL: Filter = AnyFilter { true }
        val NONE: Filter = AnyFilter { false }

        @JvmStatic
        var default: Filter = ALL

        @JvmStatic
        fun atLeast(level: Level): Filter = when (level) {
            Level.DEBUG -> ALL
            else -> LevelFilter { it >= level } // TODO: 可以考虑做对象池
        }

    }

}

fun interface AnyFilter : Filter {

    fun filter(): Boolean

}

fun interface LevelFilter : Filter {

    fun filter(level: Level): Boolean

}

fun interface TagFilter : Filter {

    fun filter(tag: String): Boolean

}

//private object DefaultFilter : Filter { // TODO: expect
//    override fun filter(): Boolean {
//        return true
//    }
//}

internal fun interface CompositeFilter : Filter {

    companion object {

        fun Filter.filter(level: Level, tag: String) = when (this) {
            is AnyFilter -> filter()
            is LevelFilter -> filter(level)
            is TagFilter -> filter(tag)
            is CompositeFilter -> filter(level, tag)
        }

    }

    fun filter(level: Level, tag: String): Boolean

}

@JvmName("any")
operator fun Filter.plus(other: Filter): Filter =
    CompositeFilter { level, tag -> this.filter(level, tag) || other.filter(level, tag) }

@JvmName("both")
operator fun Filter.minus(other: Filter): Filter =
    CompositeFilter { level, tag -> this.filter(level, tag) && other.filter(level, tag) }

@JvmName("of")
fun Logger.withFilter(filter: Filter): Logger = FilterLogger.of(this, filter)