package dev.scarlet.logger

import platform.Foundation.NSLog
import kotlin.experimental.ExperimentalNativeApi

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "iOSLogger"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        log("DEBUG", tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        log("INFO", tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        log("WARN", tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        log("ERROR", tag, msg, tr)
    }

    @OptIn(ExperimentalNativeApi::class)
    private fun log(level: String, tag: String, msg: String, tr: Throwable?) {
        val fullMessage = buildString {
            append("[$level] $tag: $msg")
            if (tr != null) {
                append(" Exception: ${tr.message}")
                // 可选：打印堆栈跟踪（在 Kotlin/Native 中堆栈信息有限）
                tr.getStackTrace().forEach { frame ->
                    append("\n  at $frame")
                }
            }
        }
        NSLog("%@", fullMessage)
    }
}