package dev.scarlet.logger

import kotlin.test.Test
import kotlin.test.assertTrue

class JsLoggerTest {

    @Test
    fun debug_logs_to_console_with_correct_format() {
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
    fun info_logs_to_console_with_correct_format() {
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
    fun warn_logs_to_console_with_correct_format() {
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
    fun error_logs_to_console_with_correct_format() {
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
    fun error_with_exception_passes_error_object_to_console() {
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
    fun empty_message_logs_tag_only() {
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