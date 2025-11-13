package dev.scarlet.logger

import kotlin.test.Test
import kotlin.test.assertTrue

class JsLoggerTest {

    @Test
    fun `debug logs to console with correct format`() {
        val captured = mutableListOf<String>()
        val consoleDynamic = console.asDynamic()

        val originalLog = consoleDynamic.log
        consoleDynamic.log = { msg: dynamic, _: dynamic ->
            captured.add(msg.toString())
        }

        try {
            Logger.d("TestTag", "Debug message")
            assertTrue(captured.any { it == "[TestTag] Debug message" })
        } finally {
            consoleDynamic.log = originalLog
        }
    }

    @Test
    fun `info logs to console with correct format`() {
        val captured = mutableListOf<String>()
        val consoleDynamic = console.asDynamic()

        val originalInfo = consoleDynamic.info
        consoleDynamic.info = { msg: dynamic, _: dynamic ->
            captured.add(msg.toString())
        }

        try {
            Logger.i("InfoTag", "Info message")
            assertTrue(captured.any { it == "[InfoTag] Info message" })
        } finally {
            consoleDynamic.info = originalInfo
        }
    }

    @Test
    fun `warn logs to console with correct format`() {
        val captured = mutableListOf<String>()
        val consoleDynamic = console.asDynamic()

        val originalWarn = consoleDynamic.warn
        consoleDynamic.warn = { msg: dynamic, _: dynamic ->
            captured.add(msg.toString())
        }

        try {
            Logger.w("WarnTag", "Warning message")
            assertTrue(captured.any { it == "[WarnTag] Warning message" })
        } finally {
            consoleDynamic.warn = originalWarn
        }
    }

    @Test
    fun `error logs to console with correct format`() {
        val captured = mutableListOf<String>()
        val consoleDynamic = console.asDynamic()

        val originalError = consoleDynamic.error
        consoleDynamic.error = { msg: dynamic, _: dynamic ->
            captured.add(msg.toString())
        }

        try {
            Logger.e("ErrorTag", "Error message")
            assertTrue(captured.any { it == "[ErrorTag] Error message" })
        } finally {
            consoleDynamic.error = originalError
        }
    }

    @Test
    fun `error with exception passes error object to console`() {
        val capturedErrors = mutableListOf<dynamic>()
        val consoleDynamic = console.asDynamic()

        val originalError = consoleDynamic.error
        consoleDynamic.error = { msg: dynamic, error: dynamic ->
            capturedErrors.add(error)
        }

        try {
            val exception = RuntimeException("JS test exception")
            Logger.e("ExTag", "With exception", exception)

            assertTrue(capturedErrors.isNotEmpty())
            val err = capturedErrors.first()
            assertTrue(err != undefined && err != null)
        } finally {
            consoleDynamic.error = originalError
        }
    }

    @Test
    fun `empty message logs tag only`() {
        val captured = mutableListOf<String>()
        val consoleDynamic = console.asDynamic()

        val originalLog = consoleDynamic.log
        consoleDynamic.log = { msg: dynamic, _: dynamic ->
            captured.add(msg.toString())
        }

        try {
            Logger.d("Empty", "")
            assertTrue(captured.any { it == "[Empty] " })
        } finally {
            consoleDynamic.log = originalLog
        }
    }
}