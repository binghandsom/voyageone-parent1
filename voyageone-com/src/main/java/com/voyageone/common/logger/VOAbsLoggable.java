package com.voyageone.common.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * VOAbsLoggable
 *
 * @author chuanyu.liang, 12/04/16.
 * @version 2.0.1
 * @since 2.0.0
 */
public abstract class VOAbsLoggable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取打印的日志是否需要包含线程
     */
    public boolean getLogWithThread() {
        return false;
    }

    /**
     * logger.info 的辅助方法
     *
     * @param arg 日志信息
     */
    public void $info(String arg) {
        if (!getLogWithThread()) {
            logger.info(arg);
            return;
        }
        logger.info(String.format("Thread-%s\t| %s", Thread.currentThread().getId(), arg));
    }

    /**
     * logger.info 的辅助方法
     *
     * @param template 格式化字符串
     * @param args     格式化参数
     */
    public void $info(String template, Object... args) {
        if (!getLogWithThread()) {
            logger.info(format(template, args));
            return;
        }
        logger.info(format("Thread-%s\t| %s", Thread.currentThread().getId(), format(template, args)));
    }

    /**
     * logger.warn 的辅助方法
     *
     * @param arg 日志信息
     */
    public void $warn(String arg) {
        if (!getLogWithThread()) {
            logger.warn(arg);
            return;
        }
        logger.warn(String.format("Thread-%s\t| %s", Thread.currentThread().getId(), arg));
    }

    /**
     * logger.warn 的辅助方法
     *
     * @param template 格式化字符串
     * @param args     格式化参数
     */
    public void $warn(String template, Object... args) {
        if (!getLogWithThread()) {
            logger.warn(format(template, args));
            return;
        }
        logger.warn(format("Thread-%s\t| %s", Thread.currentThread().getId(), format(template, args)));
    }

    /**
     * logger.error 的辅助方法
     *
     * @param arg 日志信息
     */
    public void $error(String arg) {
        if (!getLogWithThread()) {
            logger.error(arg);
            return;
        }
        logger.error(String.format("Thread-%s\t| %s", Thread.currentThread().getId(), arg));
    }

    /**
     * logger.error 的辅助方法
     *
     * @param t   Throwable
     */
    public void $error(Throwable t) {
        if (!getLogWithThread()) {
            logger.error(t.getMessage(), t);
            return;
        }
        logger.error(String.format("Thread-%s\t| %s", Thread.currentThread().getId(), t.getMessage()), t);
    }

    /**
     * logger.error 的辅助方法
     *
     * @param msg String
     * @param t   Throwable
     */
    public void $error(String msg, Throwable t) {
        if (!getLogWithThread()) {
            logger.error(msg, t);
            return;
        }
        logger.error(format("Thread-%s\t| %s", Thread.currentThread().getId(), msg), t);
    }

    /**
     * logger.debug 的辅助方法
     *
     * @param template 格式化字符串
     * @param args     格式化参数
     */
    public void $error(String template, Object... args) {
        if (!getLogWithThread()) {
            logger.error(format(template, args));
            return;
        }
        logger.error(format("Thread-%s\t| %s", Thread.currentThread().getId(), format(template, args)));
    }

    /**
     * logger.debug 的辅助方法
     *
     * @param arg 日志信息
     */
    public void $debug(String arg) {
        if (!getLogWithThread()) {
            logger.debug(arg);
            return;
        }
        logger.debug(String.format("Thread-%s\t| %s", Thread.currentThread().getId(), arg));
    }

    /**
     * logger.debug 的辅助方法
     *
     * @param template 格式化字符串
     * @param args     格式化参数
     */
    public void $debug(String template, Object... args) {
        if (!getLogWithThread()) {
            logger.debug(format(template, args));
            return;
        }
        logger.debug(format("Thread-%s\t| %s", Thread.currentThread().getId(), format(template, args)));
    }
}