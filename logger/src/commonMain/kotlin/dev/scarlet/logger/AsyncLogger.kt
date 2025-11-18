package dev.scarlet.logger

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 异步[Logger]。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
class AsyncLogger(
    private val context: CoroutineContext = EmptyCoroutineContext,
    private val logger: Logger
) : Logger {

    private val scope = GlobalScope

    override fun d(tag: String, msg: String, tr: Throwable?) {
        scope.launch(context) { logger.d(tag, msg, tr) }
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        scope.launch(context) { logger.i(tag, msg, tr) }
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        scope.launch(context) { logger.w(tag, msg, tr) }
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        scope.launch(context) { logger.e(tag, msg, tr) }
    }
}