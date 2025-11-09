package dev.scarlet.logger

import android.util.Log

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual object PlatformLogger : Logger {

    actual override fun toString(): String = "AndroidLogger"

    actual override fun d(tag: String, msg: String, tr: Throwable?) {
        Log.d(tag, msg, tr)
    }

    actual override fun i(tag: String, msg: String, tr: Throwable?) {
        Log.i(tag, msg, tr)
    }

    actual override fun w(tag: String, msg: String, tr: Throwable?) {
        Log.w(tag, msg, tr)
    }

    actual override fun e(tag: String, msg: String, tr: Throwable?) {
        Log.e(tag, msg, tr)
    }
}