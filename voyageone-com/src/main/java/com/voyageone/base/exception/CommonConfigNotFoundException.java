package com.voyageone.base.exception;

/**
 * 共通的配置项目没找到异常
 * <p>
 * 当任何共通且影响全局的配置项目没有找到时,抛出该异常
 * <p>
 * Created by desmond on 2016/08/09.
 *
 * @author desmond
 * @version 2.4.0
 * @since 2.4.0
 */
public class CommonConfigNotFoundException extends RuntimeException {

    public CommonConfigNotFoundException(String message) {
        super(message);
    }

    public CommonConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
