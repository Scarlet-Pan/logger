package dev.scarlet.logger

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class LoggerTest {

    @Test
    fun system_logger_is_platform_logger() {
        assertIs<PlatformLogger>(Logger.SYSTEM)
    }

    @Test
    fun default_logger_equals_system_logger() {
        assertEquals(Logger.SYSTEM, Logger.default)
    }
}