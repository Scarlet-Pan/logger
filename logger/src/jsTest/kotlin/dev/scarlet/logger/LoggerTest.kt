package dev.scarlet.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class LoggerTest {

    companion object {
        private const val TAG = "LoggerTest"
    }

    @Test
    fun systemLogger_isPlatformLogger() {
        assertIs<PlatformLogger>(Logger.SYSTEM)
    }

    @Test
    fun defaultLogger_isSystem() {
        assertEquals(Logger.default, Logger.SYSTEM)
    }

    @Test
    fun defaultLogger_isWorkable() {
        Logger.d(TAG, "Test a debug message.")
        Logger.i(TAG, "Test a info message.")
        Logger.w(TAG, "Test a warn message.")
        Logger.e(TAG, "Test a error message.")
    }

    @Test
    fun defaultLoggerChanged_isCorrect() {
        assertIs<PlatformLogger>(Logger.default)

        Logger.default += ALogger
        assertIs<CompositeLogger>(Logger.default)
        assertEquals("[ConsoleLogger, ALogger]", Logger.default.toString())

        Logger.default += BLogger
        assertIs<CompositeLogger>(Logger.default)
        assertEquals("[ConsoleLogger, ALogger, BLogger]", Logger.default.toString())
    }
}

private abstract class NameLogger(
    private val name: String,
    private val delegate: Logger = Logger.SYSTEM
) : Logger {

    override fun toString(): String = name

    override fun d(tag: String, msg: String, tr: Throwable?) {
        delegate.d("$tag@$name", msg, tr)
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        delegate.i("$tag@$name", msg, tr)
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        delegate.w("$tag@$name", msg, tr)
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        delegate.e("$tag@$name", msg, tr)
    }
}

private object ALogger : NameLogger("ALogger")

private object BLogger : NameLogger("BLogger")