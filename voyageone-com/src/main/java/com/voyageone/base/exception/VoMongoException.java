package com.voyageone.base.exception;

/**
 * Created by DELL on 2016/3/25.
 */
public class VoMongoException extends RuntimeException {

    public VoMongoException(String message) {
        super(message);
    }

    public VoMongoException(String message, Throwable cause) {
        super(message, cause);
    }
}
