package com.voyageone.components.onestop.service;


/**
 * 调用API层面所抛出的一样
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/21 13:52
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopApiException extends RuntimeException{

    public OneStopApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OneStopApiException(String message) {
        super(message);
    }
}
