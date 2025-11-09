package dev.scarlet.logger.slf4j

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 代理[Logger]，用于对接slf4j，代理转发。
 *
 * @author Scarlet Pan
 * @version 1.0.0
 */
object DelegateLogger : dev.scarlet.logger.Logger {

    override fun toString(): String = "DelegateLogger"

    override fun d(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isDebugEnabled }?.let {
            if (tr == null) it.debug(msg)
            else it.debug(msg, tr)
        }
    }

    override fun i(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isInfoEnabled }?.let {
            if (tr == null) it.info(msg)
            else it.info(msg, tr)
        }
    }

    override fun w(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isWarnEnabled }?.let {
            if (tr == null) it.warn(msg)
            else it.warn(msg, tr)
        }
    }

    override fun e(tag: String, msg: String, tr: Throwable?) {
        LoggerFactory.getLogger(tag).takeIf { it.isErrorEnabled }?.let {
            if (tr == null) it.error(msg)
            else it.error(msg, tr)
        }
    }

}