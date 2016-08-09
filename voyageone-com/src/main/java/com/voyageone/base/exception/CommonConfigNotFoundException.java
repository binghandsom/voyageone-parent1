package com.voyageone.base.exception;

/**
 * Created by DELL on 2016/3/25.
 */
public class CommonConfigNotFoundException extends RuntimeException {

    public CommonConfigNotFoundException(String message) {
        super(message);
    }

    public CommonConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
