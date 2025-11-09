package dev.scarlet.logger

import kotlin.jvm.JvmStatic

@Suppress("ObjectPropertyName")
private var _default: Logger = PlatformLogger

/**
 * 日志记录器。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
interface Logger {

    companion object : Logger by _default {

        private const val TAG = "Logger"

        @JvmStatic
        val SYSTEM: Logger = PlatformLogger

        var default: Logger
            get() = _default
            set(value) {
                _default = value.also {
                    it.i(TAG, "Default logger changed to $value from $_default.")
                }
            }

    }

    fun d(tag: String, msg: String, tr: Throwable? = null)

    fun i(tag: String, msg: String, tr: Throwable? = null)

    fun w(tag: String, tr: Throwable) = w(tag, "", tr)

    fun w(tag: String, msg: String, tr: Throwable? = null)

    fun e(tag: String, msg: String, tr: Throwable? = null)

}