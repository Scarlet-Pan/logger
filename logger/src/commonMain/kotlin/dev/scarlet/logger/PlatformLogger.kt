package dev.scarlet.logger

/**
 * The host platform's implementation of [Logger].
 *
 * @author Scarlet Pan
 * @version 1.0.1
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect object PlatformLogger : Logger {

    override fun toString(): String

    override fun d(tag: String, msg: String, tr: Throwable?)

    override fun i(tag: String, msg: String, tr: Throwable?)

    override fun w(tag: String, msg: String, tr: Throwable?)

    override fun e(tag: String, msg: String, tr: Throwable?)

}