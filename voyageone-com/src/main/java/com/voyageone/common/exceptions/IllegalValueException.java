package com.voyageone.common.exceptions;

import com.voyageone.base.exception.BusinessException;

/**
 * 非法值异常
 * <p>
 * 当使用断言检查某变量或参数时, 抛出该异常
 * <p>
 * Created by jonas on 8/2/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class IllegalValueException extends BusinessException {

    /**
     * 创建一个非法值异常, 并指定一个错误类型。同时附带额外的参数
     *
     * @param illegalValueTypes 错误类型, 为异常提供预定义的 code 和 message
     * @param args              额外的参数。在前台时, 将用于字符串格式化。在后台时, 会跟随错误保存到 Issue Log 表中。
     */
    public IllegalValueException(IllegalValueTypes illegalValueTypes, Object... args) {
        super(illegalValueTypes.getCode(), illegalValueTypes.getMessage(), null);
        setInfo(args);
    }

    /**
     * 创建一个非法值异常, 并使用格式化参数, 格式化错误类型所提供的 Message。同时附带额外的参数
     *
     * @param illegalValueTypes 错误类型, 为异常提供预定义的 code 和 message
     * @param formatArgs        错误信息格式化参数
     * @param args              额外的参数。在前台时, 将用于字符串格式化。在后台时, 会跟随错误保存到 Issue Log 表中。
     */
    public IllegalValueException(IllegalValueTypes illegalValueTypes, Object[] formatArgs, Object... args) {
        super(illegalValueTypes.getCode(), String.format(illegalValueTypes.getMessage(), formatArgs), null);
        setInfo(args);
    }
}
