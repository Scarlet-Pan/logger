package dev.scarlet.logger.filter

import dev.scarlet.logger.Content.Companion.with
import dev.scarlet.logger.Logger
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse


class LazyLoggerTest {

    companion object {
        private const val TAG = "LazyLoggerTest"
    }

    private lateinit var defaultFilter: Filter

    @BeforeTest
    fun setUp() {
        defaultFilter = Filter.default
        Filter.default = Filter.NONE
    }

    @AfterTest
    fun tearDown() {
        Filter.default = defaultFilter
    }

    @Test
    fun logs_with_lazy_message() {
        val list = mutableListOf<Int>()
        repeat(Random.nextInt(1024)) {
            list.add(Random.nextInt())
        }
        try {
            check(list.all { it >= 0 })
            Logger.i(TAG) { "All of element is non-negative in $list." }
        } catch (e: Exception) {
            Logger.w(TAG) { "At least one element is negative in $list.".with(e) }
        }
    }

    @Test
    fun lazy_info_message_lambda_is_not_invoked_when_filtered_out() {
        var executed = false
        Logger.i(TAG) {
            executed = true
            "This should not be evaluated"
        }
        assertFalse(executed)
    }

    @Test
    fun lazy_warn_message_lambda_is_not_invoked_when_filtered_out() {
        var executed = false
        Logger.w(TAG) {
            executed = true
            "Warning message"
        }
        assertFalse(executed)
    }

    @Test
    fun lazy_error_message_lambda_is_not_invoked_when_filtered_out() {
        var executed = false
        Logger.e(TAG) {
            executed = true
            "Error message"
        }
        assertFalse(executed)
    }

    @Test
    fun lazy_debug_message_with_exception_is_not_evaluated() {
        var executed = false
        val exception = RuntimeException("test")
        Logger.d(TAG) {
            executed = true
            "Debug with exception".with(exception)
        }
        assertFalse(executed)
    }

    @Test
    fun expensive_operation_in_lambda_is_avoided() {
        var callCount = 0
        fun expensiveOperation(): String {
            callCount++
            return "Expensive result: ${Random.nextInt()}"
        }
        Logger.i(TAG) { expensiveOperation() }
        assertEquals(0, callCount)
    }
}