package com.voyageone.components.onestop.service;

/**
 * 业务层面的错误,返回此结果,包含有业务异常结构体,比如post订单出错.会有具体的出错信息.那么在捕获此异常
 * 然后获取其getMessage()获取其body的json信息.然后转成对应的业务response,
 * 因为其post的业务错误是通过错误码反应的
 * 所以需要用此异常来转化.
 * @description
 * @author: holysky.zhao
 * @date: 2016/11/23 19:23
 * @version:1.0.0 COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OneStopBizException extends OneStopApiException{
    public OneStopBizException(String message, Throwable cause) {
        super(message, cause);
    }

    public OneStopBizException(String message) {
        super(message);
    }
}
