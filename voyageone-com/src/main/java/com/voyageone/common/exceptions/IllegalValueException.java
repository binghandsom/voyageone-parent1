package com.voyageone.common.exceptions;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;

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
     * 创建一个非法值异常, 并使用格式化参数格式化自定义的信息
     * <p>
     * 如果自定义信息为空, 则默认使用错误类型提供的固定信息
     * <p>
     * 同时附带额外的参数
     *
     * @param illegalValueTypes 错误类型, 为异常提供预定义的 code
     * @param message           错误信息
     * @param args              额外的参数。同时作为异常信息的格式化参数。另外, 对前台时, 将用于字符串格式化。对后台时, 会跟随错误保存到 Issue Log 表中。
     */
    public IllegalValueException(IllegalValueTypes illegalValueTypes, String message, Object... args) {
        super(illegalValueTypes.getCode(), String.format(StringUtils.isEmpty(message) ? illegalValueTypes.getMessage() : message, args), null);
        setInfo(args);
    }
}
