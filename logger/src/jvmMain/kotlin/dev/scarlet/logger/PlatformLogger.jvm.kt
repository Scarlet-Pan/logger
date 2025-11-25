package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
import dev.scarlet.logger.Logger.Level.DEBUG
import dev.scarlet.logger.Logger.Level.ERROR
import dev.scarlet.logger.Logger.Level.INFO
import dev.scarlet.logger.Logger.Level.WARN
import java.io.PrintStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "SystemPrinter"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        Printer.DEFAULT.log(DEBUG, tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        Printer.DEFAULT.log(INFO, tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        Printer.ERROR.log(WARN, tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        Printer.ERROR.log(ERROR, tag, msg, tr)
    }

    internal enum class Printer(output: PrintStream) {

        DEFAULT(System.out),
        ERROR(System.err), ;

        companion object {
            private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        }

        internal var output = output

        fun log(level: Level, tag: String, message: String, throwable: Throwable?) {
            val timestamp = LocalDateTime.now().format(FORMATTER)

            this@Printer.output.println("[$timestamp] [${level.format}/$tag] $message")
            throwable?.printStackTrace(this@Printer.output)
        }


    }
}

private val Level.format
    get() = when (this) {
        DEBUG -> name
        INFO -> "$name "
        WARN -> "$name "
        ERROR -> name
    }