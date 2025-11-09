package dev.scarlet.logger

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "ConsoleLogger"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        log("debug", tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        log("info", tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        log("warn", tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        log("error", tag, msg, tr)
    }

    private fun log(level: String, tag: String, message: String, throwable: Throwable?) {
        val fullMessage = "[$tag] $message"
        when (level) {
            "debug" -> console.log(fullMessage, throwable?.toJsError())
            "info" -> console.info(fullMessage, throwable?.toJsError())
            "warn" -> console.warn(fullMessage, throwable?.toJsError())
            "error" -> console.error(fullMessage, throwable?.toJsError())
            else -> console.log(fullMessage, throwable?.toJsError())
        }
    }

    private fun Throwable.toJsError(): dynamic = js("new Error(message)").apply {
        asDynamic().stack = stackTraceToString()
    }
}