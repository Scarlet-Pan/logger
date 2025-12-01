package dev.scarlet.logger.filter

import dev.scarlet.logger.AbsLogger
import dev.scarlet.logger.Key
import dev.scarlet.logger.Key.Companion.key
import dev.scarlet.logger.Logger.Level
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class FilterLoggerTest {

    private lateinit var originalDefaultFilter: Filter

    @BeforeTest
    fun setUp() {
        originalDefaultFilter = Filter.default
        Filter._default = Filter.ALL
    }

    @AfterTest
    fun tearDown() {
        Filter._default = originalDefaultFilter
    }

    @Test
    fun with_filter_blocks_debug_but_allows_warn() {
        val testLogger = TestLogger()
        val filteredLogger = testLogger.withFilter(Filter.atLeast(Level.WARN))

        var debugLambdaExecuted = false
        var warnLambdaExecuted = false

        filteredLogger.d("TAG") {
            debugLambdaExecuted = true
            "debug message"
        }

        filteredLogger.w("TAG") {
            warnLambdaExecuted = true
            "warn message"
        }

        assertFalse(debugLambdaExecuted)
        assertTrue(warnLambdaExecuted)
        assertEquals(1, testLogger.logs.size)
        assertEquals("TAG: warn message", testLogger.logs[0])
    }

    @Test
    fun without_filter_restores_full_logging() {
        val testLogger = TestLogger()
        val filtered = testLogger.withFilter(Filter.NONE)
        val restored = filtered.withoutFilter()

        var debugExecuted = false
        restored.d("TAG") {
            debugExecuted = true
            "debug after restore"
        }

        assertTrue(debugExecuted)
        assertEquals(1, testLogger.logs.size)
        assertEquals("TAG: debug after restore", testLogger.logs[0])
    }

    @Test
    fun without_filter_on_non_wrapped_logger_is_no_op() {
        val testLogger = TestLogger()
        val result = testLogger.withoutFilter()
        assertSame(testLogger, result)
    }

    @Test
    fun multiple_filters_chain_correctly() {
        val testLogger = TestLogger()
        val doubleFiltered = testLogger
            .withFilter(Filter.atLeast(Level.INFO))   // blocks DEBUG
            .withFilter(Filter.atLeast(Level.ERROR))  // blocks INFO/WARN

        var debugExec = false
        var infoExec = false
        var errorExec = false

        doubleFiltered.d("T") { debugExec = true; "d" }
        doubleFiltered.i("T") { infoExec = true; "i" }
        doubleFiltered.e("T") { errorExec = true; "e" }

        assertFalse(debugExec)
        assertFalse(infoExec)
        assertTrue(errorExec)
        assertEquals(1, testLogger.logs.size)
        assertEquals("T: e", testLogger.logs[0])
    }

    @Test
    fun with_filter_does_not_respect_global_default_when_overridden() {
        // Set global default to block DEBUG
        Filter._default = Filter.atLeast(Level.INFO)

        val testLogger = TestLogger()
        // Apply a permissive local filter that allows DEBUG
        val loggerWithPermissiveFilter = testLogger.withFilter(Filter.ALL)

        var debugExec = false
        loggerWithPermissiveFilter.d("TAG") {
            debugExec = true
            "this should be logged because local filter overrides global"
        }

        // Since the wrapped logger uses its own filter (not global),
        // the lambda should execute and log.
        assertTrue(debugExec)
        assertEquals(1, testLogger.logs.size)
        assertEquals(
            "TAG: this should be logged because local filter overrides global",
            testLogger.logs[0]
        )
    }

    private class TestLogger : AbsLogger() {

        companion object : Key<TestLogger> by key()

        val logs = mutableListOf<String>()

        override fun log(level: Level, tag: String, msg: String, tr: Throwable?) {
            logs.add("$tag: $msg")
        }
    }
}