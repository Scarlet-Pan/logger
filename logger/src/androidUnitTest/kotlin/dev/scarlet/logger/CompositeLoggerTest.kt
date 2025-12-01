package dev.scarlet.logger

import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
@RunWith(RobolectricTestRunner::class)
class CompositeLoggerTest {

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        Logger.default = Logger.SYSTEM
    }

    @Test
    fun composite_logger_to_string_returns_expected_format() {
        Logger.default += ALogger

        assertEquals("[AndroidLog, ALogger]", Logger.default.toString())
    }

    @Test
    fun composite_2_loggers_to_string_returns_expected_format() {
        Logger.default += ALogger
        Logger.default += BLogger

        assertEquals("[AndroidLog, ALogger, BLogger]", Logger.default.toString())
    }

    @Test
    fun can_reset_default_logger_to_system() {
        Logger.default += ALogger
        assertTrue(Logger.default is CompositeLogger)

        // Reset
        Logger.default = Logger.SYSTEM
        assertEquals(Logger.SYSTEM, Logger.default)
        assertIs<PlatformLogger>(Logger.default)
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