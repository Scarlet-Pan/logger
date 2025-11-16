package dev.scarlet.logger

import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.assertFalse

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class JvmLoggerTest {

    private lateinit var originalStandardOutput: PrintStream
    private lateinit var originalStandardError: PrintStream
    private lateinit var standardOutputCapture: ByteArrayOutputStream
    private lateinit var standardErrorCapture: ByteArrayOutputStream

    @Before
    fun setUp() {
        originalStandardOutput = System.out
        originalStandardError = System.err
        standardOutputCapture = ByteArrayOutputStream()
        standardErrorCapture = ByteArrayOutputStream()
        PlatformLogger.Printer.DEFAULT.output = PrintStream(standardOutputCapture)
        PlatformLogger.Printer.ERROR.output = PrintStream(standardErrorCapture)
    }

    @After
    fun tearDown() {
        System.setOut(originalStandardOutput)
        System.setErr(originalStandardError)
    }

    @Test
    fun `logs debug message to standard output without exception`() {
        Logger.d("TestTag", "Debug message", null)
        val output = standardOutputCapture.toString()
        assertTrue(output.contains("[DEBUG/TestTag] Debug message"))
        assertFalse(output.contains("Exception"))
    }

    @Test
    fun `logs info message to standard output`() {
        Logger.i("App", "Application started", null)
        val output = standardOutputCapture.toString()
        assertTrue(output.contains("[INFO /App] Application started"))
    }

    @Test
    fun `logs warn message to standard error without exception`() {
        Logger.w("Network", "Slow response", null)
        val output = standardErrorCapture.toString()
        assertTrue(output.contains("[WARN /Network] Slow response"))
        assertFalse(output.contains("Exception"))
    }

    @Test
    fun `logs error message with exception stack trace to standard error`() {
        val exception = IllegalArgumentException("Invalid input")
        Logger.e("Validation", "Failed to validate", exception)

        val output = standardErrorCapture.toString()
        assertTrue(output.contains("[ERROR/Validation] Failed to validate"))
        assertTrue(output.contains("java.lang.IllegalArgumentException: Invalid input"))
        assertTrue(output.lines().any { it.contains("at dev.scarlet.logger.JvmLoggerTest") })
    }

    @Test
    fun `logs warning with exception to standard error`() {
        val exception = IllegalStateException("State error")
        Logger.w("State", "Unexpected state", exception)

        val output = standardErrorCapture.toString()
        assertTrue(output.contains("[WARN /State] Unexpected state"))
        assertTrue(output.contains("java.lang.IllegalStateException: State error"))
    }

    @Test
    fun `multiple log levels are routed to correct output streams`() {
        Logger.d("TAG1", "debug msg", null)
        Logger.i("TAG2", "info msg", null)
        Logger.w("TAG3", "warn msg", null)

        val standardOutput = standardOutputCapture.toString()
        val standardError = standardErrorCapture.toString()

        assertTrue(standardOutput.contains("[DEBUG/TAG1] debug msg"))
        assertTrue(standardOutput.contains("[INFO /TAG2] info msg"))
        assertTrue(standardError.contains("[WARN /TAG3] warn msg"))
    }
}