package dev.scarlet.logger

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class IosLoggerTest {

    companion object {
        private const val TAG = "LoggerTest"
        private const val TAG_CUSTOM = "CustomTag"
    }

    private lateinit var testLogger: TestLogger

    @BeforeTest
    fun setUp() {
        testLogger = TestLogger()
        Logger.default = testLogger
    }

    @AfterTest
    fun tearDown() {
        // Restore system logger to avoid side effects
        Logger.default = Logger.SYSTEM
    }

    @Test
    fun logs_debug_message() {
        Logger.d(TAG, "Debug message")
        assertLastLog(LogLevel.DEBUG, TAG, "Debug message")
    }

    @Test
    fun logs_info_message() {
        Logger.i(TAG, "Info message")
        assertLastLog(LogLevel.INFO, TAG, "Info message")
    }

    @Test
    fun logs_warn_message() {
        Logger.w(TAG, "Warn message")
        assertLastLog(LogLevel.WARN, TAG, "Warn message")
    }

    @Test
    fun logs_error_message() {
        Logger.e(TAG, "Error message")
        assertLastLog(LogLevel.ERROR, TAG, "Error message")
    }

    @Test
    fun handles_empty_string_message() {
        Logger.i(TAG, "")
        assertLastLog(LogLevel.INFO, TAG, "")
    }

    @Test
    fun handles_whitespace_only_message() {
        Logger.w(TAG, "   ")
        assertLastLog(LogLevel.WARN, TAG, "   ")
    }

    @Test
    fun uses_custom_tag_when_provided() {
        Logger.d(TAG_CUSTOM, "Message with custom tag")
        assertLastLog(LogLevel.DEBUG, TAG_CUSTOM, "Message with custom tag")
    }

    @Test
    fun logs_error_with_exception() {
        val exception = IllegalArgumentException("Test exception")
        Logger.e(TAG, "Error with exception", exception)

        assertEquals(1, testLogger.entries.size)
        with(testLogger.entries[0]) {
            assertEquals(LogLevel.ERROR, level)
            assertEquals(TAG, tag)
            assertEquals("Error with exception", message)
            assertNotNull(throwable)
            assertEquals("Test exception", throwable.message)
            assertTrue(throwable is IllegalArgumentException)
        }
    }

    @Test
    fun multiple_logs_are_recorded_separately() {
        Logger.d(TAG, "First")
        Logger.e(TAG, "Second")

        assertEquals(2, testLogger.entries.size)
        assertEquals("First", testLogger.entries[0].message)
        assertEquals("Second", testLogger.entries[1].message)
    }

    private fun assertLastLog(level: LogLevel, tag: String, message: String?) {
        assertEquals(1, testLogger.entries.size, "Expected exactly one log entry")
        with(testLogger.entries[0]) {
            assertEquals(level, this.level, "Log level mismatch")
            assertEquals(tag, this.tag, "Log tag mismatch")
            assertEquals(message, this.message, "Log message mismatch")
        }
    }
}

enum class LogLevel {
    DEBUG, INFO, WARN, ERROR
}

data class LogEntry(
    val level: LogLevel,
    val tag: String,
    val message: String?,
    val throwable: Throwable?
)

class TestLogger : Logger {

    val entries = mutableListOf<LogEntry>()

    override fun d(tag: String, msg: String, tr: Throwable?) {
        entries.add(LogEntry(LogLevel.DEBUG, tag, msg, tr))
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        entries.add(LogEntry(LogLevel.INFO, tag, msg, tr))
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        entries.add(LogEntry(LogLevel.WARN, tag, msg, tr))
    }

    // Optional overload for warn without message (if your Logger interface has it)
    override fun w(tag: String, tr: Throwable) {
        entries.add(LogEntry(LogLevel.WARN, tag, "", tr))
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        entries.add(LogEntry(LogLevel.ERROR, tag, msg, tr))
    }
}