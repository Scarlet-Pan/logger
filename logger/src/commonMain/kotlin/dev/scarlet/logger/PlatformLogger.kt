package dev.scarlet.logger

/**
 * 宿主平台的[Logger]实现。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal expect object PlatformLogger : Logger {

    override fun toString(): String

    override fun d(tag: String, msg: String, tr: Throwable?)

    override fun i(tag: String, msg: String, tr: Throwable?)

    override fun w(tag: String, msg: String, tr: Throwable?)

    override fun e(tag: String, msg: String, tr: Throwable?)

}