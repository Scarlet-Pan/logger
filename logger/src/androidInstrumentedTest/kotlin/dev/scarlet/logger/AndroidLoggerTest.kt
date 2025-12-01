package dev.scarlet.logger

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class AndroidLoggerTest {

    companion object {
        private const val TAG = "LoggerTest"
        private const val TEST_MSG = "Hello logcat!"
    }

    @Before
    fun clearLog() {
        try {
            Runtime.getRuntime().exec("logcat -c").waitFor()
        } catch (e: Exception) {
            Log.w(TAG, e)
        }
    }

    private fun getLogcat(): String {
        val pid = android.os.Process.myPid()
        val process = Runtime.getRuntime().exec("logcat -d --pid=$pid")
        return BufferedReader(InputStreamReader(process.inputStream)).readText()
    }

    @Test
    fun logs_debug_message_to_logcat() {
        Logger.d(TAG, TEST_MSG)
        val log = getLogcat()
        assertTrue(log.contains("$TAG: $TEST_MSG"))
    }

    @Test
    fun logs_info_message_to_logcat() {
        Logger.i(TAG, TEST_MSG)
        val log = getLogcat()
        assertTrue(log.contains("$TAG: $TEST_MSG"))
    }

    @Test
    fun logs_warn_message_to_logcat() {
        Logger.w(TAG, TEST_MSG)
        val log = getLogcat()
        assertTrue(log.contains("$TAG: $TEST_MSG"))
    }

    @Test
    fun logs_warn_message_with_exception_stack_trace_to_logcat() {
        val exception = RuntimeException("Warn test exception")
        Logger.w(TAG, "Warning with exception", exception)

        val log = getLogcat()

        assertTrue(log.contains("$TAG: Warning with exception"))
        assertTrue(log.contains("java.lang.RuntimeException: Warn test exception"))
        assertTrue(log.contains("at dev.scarlet.logger.AndroidLoggerTest"))
    }

    @Test
    fun logs_error_message_to_logcat() {
        Logger.e(TAG, TEST_MSG)
        val log = getLogcat()
        assertTrue(log.contains("$TAG: $TEST_MSG"))
    }

    @Test
    fun logs_error_message_with_exception_stack_trace_to_logcat() {
        Logger.e(TAG, "Something broke", RuntimeException("Error test exception"))
        val log = getLogcat()

        assertTrue(log.contains("$TAG: Something broke"))
        assertTrue(log.contains("java.lang.RuntimeException: Error test exception"))
        assertTrue(log.contains("at dev.scarlet.logger.AndroidLoggerTest"))
    }

    @Test
    fun logs_empty_message_correctly_to_logcat() {
        Logger.d(TAG, "")
        val log = getLogcat()
        assertTrue(log.contains("$TAG: "))
    }

    @Test
    fun logs_long_message_partially_when_exceeding_logcat_limit() {
        val longMsg = "A".repeat(5000) // Exceeds Logcat's per-line limit (~4000 characters)
        Logger.d(TAG, longMsg)
        val log = getLogcat()
        assertTrue(log.contains("AAAA")) // At least a portion of the message should appear
        // Note: Android automatically truncates long log lines, so we can't assert the full content,
        // but we verify that logging doesn't fail silently and some part is recorded.
    }
}