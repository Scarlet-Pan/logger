package dev.scarlet.logger.slf4j

import dev.scarlet.logger.Logger
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class Slf4jLoggerTest {

    companion object {
        private const val TAG = "Slf4jLoggerTest"
    }

    @Before
    fun setup() {
        Logger.default = Slf4jLogger
    }

    @Test
    fun `default logger is slf4j logger`() {
        assertEquals(Slf4jLogger, Logger.default)
        assertEquals("Slf4jLogger", Logger.default.toString())
    }

    @Test
    fun `debug log without exception works`() {
        Logger.d(TAG, "Debug message without exception")
    }

    @Test
    fun `debug log with exception works`() {
        Logger.d(TAG, "Debug message with exception", RuntimeException("test"))
    }

    @Test
    fun `info log without exception works`() {
        Logger.i(TAG, "Info message without exception")
    }

    @Test
    fun `info log with exception works`() {
        Logger.i(TAG, "Info message with exception", IllegalStateException("info error"))
    }

    @Test
    fun `warn log without exception works`() {
        Logger.w(TAG, "Warn message without exception")
    }

    @Test
    fun `warn log with exception works`() {
        Logger.w(TAG, "Warn message with exception", IllegalArgumentException("warn error"))
    }

    @Test
    fun `error log without exception works`() {
        Logger.e(TAG, "Error message without exception")
    }

    @Test
    fun `error log with exception works`() {
        Logger.e(TAG, "Error message with exception", Exception("critical error"))
    }

    @Test
    fun `all log levels are callable without crashing`() {
        Logger.d(TAG, "Starting operation")
        Logger.i(TAG, "Operation in progress")
        Logger.w(TAG, "Non-critical issue occurred")
        Logger.e(TAG, "Operation failed", Throwable("simulated failure"))
    }
}