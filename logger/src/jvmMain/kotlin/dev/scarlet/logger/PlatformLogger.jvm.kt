package dev.scarlet.logger

import java.io.PrintStream

@Suppress(names = ["EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING"])
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "SystemPrinter"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        Printer.DEFAULT.log("DEBUG", tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        Printer.DEFAULT.log("INFO ", tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        Printer.ERROR.log("WARN ", tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        Printer.ERROR.log("ERROR", tag, msg, tr)
    }

    private enum class Printer(private val output: PrintStream) {

        DEFAULT(System.out),
        ERROR(System.err), ;

        fun log(level: String, tag: String, message: String, throwable: Throwable?) {
            output.println("[$level/$tag] $message")
            throwable?.printStackTrace(output)
        }
    }
}