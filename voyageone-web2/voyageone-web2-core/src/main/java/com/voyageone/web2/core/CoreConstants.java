package com.voyageone.web2.core;

/**
 * Core 模块需要使用的变量
 * Created on 11/27/15.
 *
 * @author Jonas
 * @version 2.0.0
 */
public interface CoreConstants {
    // 密码加密固定盐值
    String MD5_FIX_SALT = "crypto.voyageone.la";
    // 密码加密散列加密次数
    int MD5_HASHITERATIONS = 4;
}
