package com.voyageone.task2.base;

import com.voyageone.common.components.issueLog.IssueLog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static java.lang.String.format;

/**
 * Created by jonasvlag on 16/3/7.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
public abstract class LoggedService {
    
    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    protected IssueLog issueLog;

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
        $info(format(template, args));
    }

    /**
     * logger.info 的辅助方法
     *
     * @param obj   属性的对象类型
     * @param name  属性的名称
     * @param value 属性的值
     */
    protected void $prop(String obj, String name, Object value) {
        $info("\"%s\".\"%s\": \"%s\"", obj, name, value);
    }
}
