package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "ConsoleLogger"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        log(DEBUG, tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        log(INFO, tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        log(WARN, tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        log(ERROR, tag, msg, tr)
    }

    private fun log(level: Level, tag: String, message: String, throwable: Throwable?) {
        val fullMessage = "[$tag] $message"
        when (level) {
            DEBUG -> console.log(fullMessage, throwable?.toJsError())
            INFO -> console.info(fullMessage, throwable?.toJsError())
            WARN -> console.warn(fullMessage, throwable?.toJsError())
            ERROR -> console.error(fullMessage, throwable?.toJsError())
        }
    }

    private fun Throwable.toJsError(): dynamic {
        val error = Error(message ?: "")
        val dynamicError: dynamic = error
        dynamicError.stack = stackTraceToString()
        return dynamicError
    }

}