package com.voyageone.components.onestop.service;

/**
 * 用于Get方法404特有的异常.场景是返回某个资源,但是404,然后直接catch此异常返回null
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/22 15:44
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopResouceNotFoundException extends OneStopApiException{
    public OneStopResouceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OneStopResouceNotFoundException(String message) {
        super(message);
    }
}
