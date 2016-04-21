package com.voyageone.common.spring;

/**
 * Created by xyyz150 on 2015/6/5.
 */
public class RollbackTransactionException extends RuntimeException {
    public RollbackTransactionException(String message) {
        super(message);
    }

    public RollbackTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackTransactionException(Throwable cause) {
        super(cause);
    }

    protected RollbackTransactionException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
