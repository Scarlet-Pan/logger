package dev.scarlet.logger

import dev.scarlet.logger.Logger.Level
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
        assertLastLog(Level.DEBUG, TAG, "Debug message")
    }

    @Test
    fun logs_info_message() {
        Logger.i(TAG, "Info message")
        assertLastLog(Level.INFO, TAG, "Info message")
    }

    @Test
    fun logs_warn_message() {
        Logger.w(TAG, "Warn message")
        assertLastLog(Level.WARN, TAG, "Warn message")
    }

    @Test
    fun logs_error_message() {
        Logger.e(TAG, "Error message")
        assertLastLog(Level.ERROR, TAG, "Error message")
    }

    @Test
    fun handles_empty_string_message() {
        Logger.i(TAG, "")
        assertLastLog(Level.INFO, TAG, "")
    }

    @Test
    fun handles_whitespace_only_message() {
        Logger.w(TAG, "   ")
        assertLastLog(Level.WARN, TAG, "   ")
    }

    @Test
    fun uses_custom_tag_when_provided() {
        Logger.d(TAG_CUSTOM, "Message with custom tag")
        assertLastLog(Level.DEBUG, TAG_CUSTOM, "Message with custom tag")
    }

    @Test
    fun logs_error_with_exception() {
        val exception = IllegalArgumentException("Test exception")
        Logger.e(TAG, "Error with exception", exception)

        assertEquals(1, testLogger.entries.size)
        with(testLogger.entries[0]) {
            assertEquals(Level.ERROR, level)
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

    private fun assertLastLog(level: Level, tag: String, message: String?) {
        assertEquals(1, testLogger.entries.size, "Expected exactly one log entry")
        with(testLogger.entries[0]) {
            assertEquals(level, this.level, "Log level mismatch")
            assertEquals(tag, this.tag, "Log tag mismatch")
            assertEquals(message, this.message, "Log message mismatch")
        }
    }
}

private data class LogEntry(
    val level: Level,
    val tag: String,
    val message: String?,
    val throwable: Throwable?
)

private class TestLogger : AbsLogger() {

    val entries = mutableListOf<LogEntry>()

    override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
        entries.add(LogEntry(level, tag, msg, tr))
    }
}