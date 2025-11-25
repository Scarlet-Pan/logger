package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import kotlin.experimental.ExperimentalNativeApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual object PlatformLogger : AbsLogger(), Logger {

    actual override fun toString(): String = "iOSLogger"

    @OptIn(ExperimentalNativeApi::class)
    override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
        val fullMessage = buildString {
            append("[$level] $tag: $msg")
            if (tr != null) {
                val type = tr::class.simpleName ?: tr::class.qualifiedName ?: "Exception"
                append(" $type: ${tr.message}")
                tr.getStackTrace().forEach { frame ->
                    append("\n  at $frame")
                }
            }
        }
        println(fullMessage)
    }
}