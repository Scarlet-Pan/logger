package dev.scarlet.logger

import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic

/**
 * 日志内容
 */
sealed interface Content {

    companion object {

        @JvmStatic
        fun of(msg: String): Content = Message(msg)

        @JvmStatic
        fun of(msg: String, tr: Throwable): Content = Error(msg, tr)

        fun String.with(exception: Throwable) = of(this, exception)

    }

    val message: String

    val throwable: Throwable? get() = null

}

@JvmInline
internal value class Message(override val message: String) : Content {
    override fun toString(): String = message
}

internal data class Error(override val message: String, override val throwable: Throwable) : Content
