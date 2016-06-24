package com.voyageone.web2.base;

/**
 * base 部分需要使用的常量
 * Created by Jonas on 11/25/15.
 * @version 2.0.0
 */
public interface BaseConstants {
    /**
     * Session 内用户信息存放的 Key
     */
    String SESSION_USER = "voyageone.session.user";
    /**
     * Session 内语言存放的 Key
     */
    String SESSION_LANG = "voyageone.session.lang";
    /**
     * 会话超时,或没有登录
     */
    String MSG_TIMEOUT = "300001";
    /**
     * 没有权限, 拒绝访问
     */
    String MSG_DENIED = "400001";
    /**
     * 跳转渠道选择的特殊 Code
     */
    String CODE_SEL_CHANNEL = "SYS_0";
    /**
     * 告知前端跳转的特殊 Code
     */
    String CODE_SYS_REDIRECT = "SYS_REDIRECT";
    /**
     * 未知异常Code
     */
    String UNKNOWN_ERROR_CODE = "5";
    /**
     * 未知异常Message
     */
    String UNKNOWN_ERROR_MSG = "Unknown exception, please contact the administrator.";
}
