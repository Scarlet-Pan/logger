package dev.scarlet.logger

import android.util.Log
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
@RunWith(RobolectricTestRunner::class)
class AndroidLoggerTest {

    companion object {
        private const val TAG = "LoggerTest"
        private const val TAG_CUSTOM = "CustomTag"
    }

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        ShadowLog.clear()
    }

    @Test
    fun logs_debug_message() {
        Logger.d(TAG, "Debug message")
        assertLastLog(Log.DEBUG, TAG, "Debug message")
    }

    @Test
    fun logs_info_message() {
        Logger.i(TAG, "Info message")
        assertLastLog(Log.INFO, TAG, "Info message")
    }

    @Test
    fun logs_warn_message() {
        Logger.w(TAG, "Warn message")
        assertLastLog(Log.WARN, TAG, "Warn message")
    }

    @Test
    fun logs_error_message() {
        Logger.e(TAG, "Error message")
        assertLastLog(Log.ERROR, TAG, "Error message")
    }

    @Test
    fun handles_empty_string_message() {
        Logger.i(TAG, "")
        assertLastLog(Log.INFO, TAG, "")
    }

    @Test
    fun handles_whitespace_only_message() {
        Logger.w(TAG, "   ")
        assertLastLog(Log.WARN, TAG, "   ")
    }

    @Test
    fun uses_custom_tag_when_provided() {
        Logger.d(TAG_CUSTOM, "Message with custom tag")
        assertLastLog(Log.DEBUG, TAG_CUSTOM, "Message with custom tag")
    }

    @Test
    fun logs_error_with_exception() {
        val exception = IllegalArgumentException("Test exception")
        Logger.e(TAG, "Error with exception", exception)

        val logs = ShadowLog.getLogs()
        assertEquals(1, logs.size)
        with(logs[0]) {
            assertEquals(Log.ERROR, type)
            assertEquals(TAG, tag)
            assertEquals("Error with exception", msg)
            assertNotNull(throwable.message)
            assertEquals("Test exception", throwable.message)
            assertTrue { throwable is IllegalArgumentException }
        }
    }

    @Test
    fun multiple_logs_are_recorded_separately() {
        Logger.d(TAG, "First")
        Logger.e(TAG, "Second")

        val logs = ShadowLog.getLogs()
        assertEquals(2, logs.size)
        assertEquals("First", logs[0].msg)
        assertEquals("Second", logs[1].msg)
    }

    private fun assertLastLog(level: Int, tag: String, message: String?) {
        val logs = ShadowLog.getLogs()
        assertEquals(1, logs.size, "Expected exactly one log entry")
        with(logs[0]) {
            assertEquals(level, type, "Log level mismatch")
            assertEquals(tag, this.tag, "Log tag mismatch")
            assertEquals(message, msg, "Log message mismatch")
        }
    }

}

