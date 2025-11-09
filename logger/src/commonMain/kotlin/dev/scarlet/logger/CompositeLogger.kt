package dev.scarlet.logger

/**
 * 组合式[Logger]。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
internal class CompositeLogger(
    private val head: Logger,
    private val tail: Logger,
) : Logger {

    private val properties: List<Logger> by lazy {
        val list = mutableListOf<Logger>()
        if (head is CompositeLogger) list.addAll(head.properties) else list.add(head)
        if (tail is CompositeLogger) list.addAll(tail.properties) else list.add(tail)
        list
    }

    override fun toString(): String = properties.toString()

    override fun d(tag: String, msg: String, tr: Throwable?) {
        head.d(tag, msg, tr)
        tail.d(tag, msg, tr)
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        head.i(tag, msg, tr)
        tail.i(tag, msg, tr)
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        head.w(tag, msg, tr)
        tail.w(tag, msg, tr)
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        head.w(tag, msg, tr)
        tail.w(tag, msg, tr)
    }
}

operator fun Logger.plus(other: Logger): Logger = CompositeLogger(this, other)