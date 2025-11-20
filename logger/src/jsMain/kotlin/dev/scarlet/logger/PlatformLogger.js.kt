package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual object PlatformLogger : AbsLogger() {

    actual override fun toString(): String = "ConsoleLogger"

    override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
        val fullMessage = "[$tag] $msg"
        when (level) {
            DEBUG -> console.log(fullMessage, tr?.toJsError())
            INFO -> console.info(fullMessage, tr?.toJsError())
            WARN -> console.warn(fullMessage, tr?.toJsError())
            ERROR -> console.error(fullMessage, tr?.toJsError())
        }
    }

    private fun Throwable.toJsError(): dynamic {
        val error = Error(message ?: "")
        val dynamicError: dynamic = error
        dynamicError.stack = stackTraceToString()
        return dynamicError
    }

}