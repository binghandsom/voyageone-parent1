package com.voyageone.base.exception;

import com.voyageone.common.util.StringUtils;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 业务逻辑异常
 * <p>
 * 当业务逻辑执行过程出现错误, 需要打断时, 可以手动抛出该异常。
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.0.0
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -470808102948204904L;

    private String code;

    private Object[] info;

    public Object[] getInfo() {
        return info;
    }

    public void setInfo(Object[] info) {
        this.info = info;
    }

    /**
     * 创建一个业务异常, 并指定一个错误信息或错误信息对应的代码 ( 属性 Message 和 Code 相同 )。同时附带额外的参数
     *
     * @param messageOrCode 错误信息, 或错误对应的信息代码。
     * @param info          额外的参数。在前台时, 将用于字符串格式化。在后台时, 会跟随错误保存到 Issue Log 表中。
     */
    public BusinessException(String messageOrCode, Object... info) {
        this(messageOrCode, null, info);
    }

    /**
     * 创建一个业务异常, 并指定一个错误信息或错误信息对应的代码 ( 属性 Message 和 Code 相同 )。同时附带内部异常和额外的参数
     *
     * @param messageOrCode 错误信息, 或错误对应的信息代码。
     * @param cause         内部异常
     * @param info          额外的参数。在前台时, 将用于字符串格式化。在后台时, 会跟随错误保存到 Issue Log 表中。
     */
    public BusinessException(String messageOrCode, Throwable cause, Object... info) {
        super(messageOrCode, cause);
        this.code = messageOrCode;
        this.info = info;
    }

    /**
     * 创建一个业务异常, 并指定错误信息, 和错误信息对应的代码。同时附带内部异常
     *
     * @param code    错误信息代码
     * @param message 错误信息
     * @param cause   内部异常
     */
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * 获取错误信息代码
     * @return 对应 ct_message_info 中的代码
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 获取简单格式化的信息
     * @return 传入的异常信息以及 Code, 如果还有内部异常, 则同时包含内部异常的信息
     */
    @Override
    public String getMessage() {

        String message = super.getMessage();

        String code = getCode();

        if (!StringUtils.isEmpty(code))
            message += "(" + code + ")";

        Throwable cause = getCause();

        if (cause != null) {
            if (!StringUtils.isEmpty(message))
                message += "; ";
            message += "cause is: " + cause.getMessage();
        }

        return message;
    }
}
