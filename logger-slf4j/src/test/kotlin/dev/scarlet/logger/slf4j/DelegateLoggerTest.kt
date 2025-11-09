package dev.scarlet.logger.slf4j

import dev.scarlet.logger.Logger
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class DelegateLoggerTest {

    companion object {
        private const val TAG = "DelegateLoggerTest"
    }

    @Before
    fun setup() {
        Logger.default = DelegateLogger
    }

    @Test
    fun defaultLogger_isDelegateLogger() {
        assertEquals(Logger.default, DelegateLogger)
    }

    @Test
    fun defaultLogger_isWorkable() {
        Logger.d(TAG, "Test a debug message.")
        Logger.i(TAG, "Test a info message.")
        Logger.w(TAG, "Test a warn message.")
        Logger.e(TAG, "Test a error message.")
    }
}