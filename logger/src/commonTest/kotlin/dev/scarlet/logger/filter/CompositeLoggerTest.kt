package dev.scarlet.logger.filter

import dev.scarlet.logger.AbsLogger
import dev.scarlet.logger.EmptyLogger
import dev.scarlet.logger.Key
import dev.scarlet.logger.Key.Companion.key
import dev.scarlet.logger.Logger
import dev.scarlet.logger.minus
import dev.scarlet.logger.plus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * @author Scarlet Pan
 * @version 1.0.0
 */
class CompositeLoggerTest {

    @Test
    fun should_log_to_both_loggers_when_combined_with_plus() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b

        logger.i("TEST", "hello")

        assertEquals(listOf("A: hello"), a.logs)
        assertEquals(listOf("B: hello"), b.logs)
    }

    @Test
    fun should_support_chaining_multiple_loggers_with_plus() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val c = TestLogger("C")
        val logger = a + b + c

        logger.d("TAG", "world")

        assertEquals(listOf("A: world"), a.logs)
        assertEquals(listOf("B: world"), b.logs)
        assertEquals(listOf("C: world"), c.logs)
    }

    @Test
    fun should_preserve_order_of_execution_in_composite_logger() {
        val order = mutableListOf<String>()
        val first = object : AbsLogger() {
            override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
                order.add("first")
            }
        }
        val second = object : AbsLogger() {
            override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
                order.add("second")
            }
        }

        val logger = first + second
        logger.i("ORDER", "test")

        assertEquals(listOf("first", "second"), order)
    }

    @Test
    fun should_remove_single_logger_from_two_logger_composite() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b

        val result = logger - b
        result.w("RM", "msg")

        assertEquals(listOf("A: msg"), a.logs)
        assertTrue(b.logs.isEmpty())
    }

    @Test
    fun should_remove_head_logger_and_return_tail_one() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b

        val result = logger - a
        result.e("ERR", "test")

        assertTrue(a.logs.isEmpty())
        assertEquals(listOf("B: test"), b.logs)
    }

    @Test
    fun should_remove_logger_from_three_way_chain() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val c = TestLogger("C")
        val logger = a + b + c

        val result = logger - b
        result.i("CHAIN", "hi")

        assertEquals(listOf("A: hi"), a.logs)
        assertTrue(b.logs.isEmpty())
        assertEquals(listOf("C: hi"), c.logs)
    }

    @Test
    fun should_do_nothing_when_removing_non_existent_logger() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val fake = TestLogger("Fake")
        val logger = a + b

        val result = logger - fake
        result.d("KEEP", "keep")

        assertEquals(listOf("A: keep"), a.logs)
        assertEquals(listOf("B: keep"), b.logs)
    }

    @Test
    fun should_return_empty_logger_when_all_loggers_are_removed() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val composite = a + b

        val empty = composite - a - b

        assertSame(EmptyLogger, empty)

        empty.d("TAG", "should be silent")
        empty.i("TAG", "still silent")
        empty.w("TAG", "no effect")
        empty.e("TAG", "nothing logged")

        assertTrue(a.logs.isEmpty())
        assertTrue(b.logs.isEmpty())
    }

    @Test
    fun should_return_the_remaining_logger_directly_when_only_one_head() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b

        val result = logger - b

        assertSame(a, result)
    }

    @Test
    fun should_not_modify_original_composite_when_removing_loggers() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val original = a + b

        val modified = original - b

        original.i("ORIG", "original")
        modified.i("MOD", "modified")

        assertEquals(listOf("A: original", "A: modified"), a.logs)
        assertEquals(listOf("B: original"), b.logs) // B not called by 'modified'
    }

    @Test
    fun should_remove_all_instances_of_a_logger_if_duplicated() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b + a  // A → B → A

        val result = logger - a  // remove all A
        result.w("DUP", "only_b")

        assertTrue(a.logs.isEmpty())
        assertEquals(listOf("B: only_b"), b.logs)
    }

    @Test
    fun should_remove_multiple_loggers_by_subtracting_a_composite_logger() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val c = TestLogger("C")
        val logger = a + b + c

        val result = logger - (b + c)
        result.i("GROUP", "final")

        assertEquals(listOf("A: final"), a.logs)
        assertTrue(b.logs.isEmpty())
        assertTrue(c.logs.isEmpty())
    }

    @Test
    fun should_remove_same_loggers_regardless_of_composite_order() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val c = TestLogger("C")
        val logger = a + b + c

        // Remove in reverse order: c + b
        val result = logger - (c + b)
        result.d("REV", "msg")

        assertEquals(listOf("A: msg"), a.logs)
        assertTrue(b.logs.isEmpty())
        assertTrue(c.logs.isEmpty())
    }

    @Test
    fun should_treat_duplicate_loggers_in_removal_set_as_single_entry() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val c = TestLogger("C")
        val logger = a + b + c + b  // two B's

        val result = logger - (b + b + c)

        result.e("DEDUP", "end")

        assertEquals(listOf("A: end"), a.logs)
        assertTrue(b.logs.isEmpty())  // both B instances gone
        assertTrue(c.logs.isEmpty())
    }

    @Test
    fun should_ignore_removal_of_composite_that_has_no_overlap() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val x = TestLogger("X")
        val y = TestLogger("Y")

        val logger = a + b

        val result = logger - (x + y)
        result.w("UNREL", "unchanged")

        assertEquals(listOf("A: unchanged"), a.logs)
        assertEquals(listOf("B: unchanged"), b.logs)
    }

    @Test
    fun should_remove_logger_by_key_using_companion_as_identity() {
        val a = TestLogger("A")
        val file1 = FileLogger()
        val file2 = FileLogger()
        val logger = a + file1 + file2

        // Remove all loggers whose companion == FileLogger.Companion (i.e., all FileLogger instances)
        val result = logger - FileLogger

        // Only 'a' remains
        result.i("KEY", "test")

        assertEquals(listOf("A: test"), a.logs)
        assertTrue(file1.logs.isEmpty())
        assertTrue(file2.logs.isEmpty())
    }

    @Test
    fun should_remove_single_instance_logger_by_key() {
        val console = TestLogger("Console")
        val file = FileLogger()
        val logger = console + file

        val result = logger - FileLogger

        assertSame(console, result) // unwrapped to single logger
        result.d("SINGLE", "ok")

        assertEquals(listOf("Console: ok"), console.logs)
        assertTrue(file.logs.isEmpty())
    }

    @Test
    fun should_return_empty_logger_when_removing_all_by_key() {
        val f1 = FileLogger()
        val f2 = FileLogger()
        val logger = f1 + f2

        val result = logger - FileLogger

        assertSame(EmptyLogger, result)

        result.w("EMPTY", "silent")
        assertTrue(f1.logs.isEmpty())
        assertTrue(f2.logs.isEmpty())
    }

    @Test
    fun should_not_remove_if_key_does_not_match() {
        val a = TestLogger("A")
        val b = TestLogger("B")
        val logger = a + b

        // Try to remove FileLogger, but none exists
        val result = logger - FileLogger

        assertSame(logger, result) // unchanged composite

        result.e("NOOP", "msg")
        assertEquals(listOf("A: msg"), a.logs)
        assertEquals(listOf("B: msg"), b.logs)
    }

    @Test
    fun should_remove_multiple_types_by_chaining_key_subtractions() {
        val a = TestLogger("A")
        val f = FileLogger()
        val logger = a + f

        val result = logger - FileLogger - TestLogger

        assertSame(EmptyLogger, result)

        result.i("CHAIN_KEY", "gone")
        assertTrue(a.logs.isEmpty())
        assertTrue(f.logs.isEmpty())
    }

    @Test
    fun should_work_with_mixed_removal_styles() {
        val console = TestLogger("Console")
        val file = FileLogger()
        val logger = console + file

        // Remove by instance and by key in same chain
        val result = logger - console - FileLogger

        assertSame(EmptyLogger, result)
    }

    private class TestLogger(val name: String) : AbsLogger() {

        companion object : Key<TestLogger> by key()

        val logs = mutableListOf<String>()

        override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
            logs.add("$name: $msg")
        }
    }

    private class FileLogger : AbsLogger() {

        companion object : Key<FileLogger> by key()

        val logs = mutableListOf<String>()

        override fun log(level: Logger.Level, tag: String, msg: String, tr: Throwable?) {
            logs.add("$tag: $msg")
        }
    }
}



