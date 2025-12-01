package dev.scarlet.logger.slf4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * An implementation based on SLF4J (Simple Logging Facade for Java) that bridges the [dev.scarlet.logger.Logger] interface.
 *
 * This object delegates all log calls to SLF4J's [Logger], using the provided `tag` as the logger name.
 * Before logging, it checks whether the corresponding log level is enabled, ensuring that messages
 * are only processed when necessary—avoiding unnecessary string concatenation or computation.
 *
 * Example usage:
 * ```kotlin
 * Slf4jLogger.d("MyTag", "Debug message")
 * Slf4jLogger.e("Network", "Failed to connect", exception)
 * ```
 *
 * Note: This implementation requires the SLF4J API and a compatible backend binding (e.g., Logback, slf4j-simple)
 * to be present at runtime. Ensure your project includes an appropriate SLF4J provider.
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
object Slf4jLogger : dev.scarlet.logger.Logger {

    override fun toString(): String = "Slf4jLogger"

    override fun d(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isDebugEnabled }?.let {
            if (tr == null) it.debug(msg)
            else it.debug(msg, tr)
        }
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isInfoEnabled }?.let {
            if (tr == null) it.info(msg)
            else it.info(msg, tr)
        }
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isWarnEnabled }?.let {
            if (tr == null) it.warn(msg)
            else it.warn(msg, tr)
        }
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isErrorEnabled }?.let {
            if (tr == null) it.error(msg)
            else it.error(msg, tr)
        }
    }

}