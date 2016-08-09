package com.voyageone.common.asserts;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.StringUtils;

/**
 * 断言检查失败异常
 * <p>
 * 当使用断言检查某变量或参数失败时, 抛出该异常
 * <p>
 * Created by jonas on 8/2/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class AssertFailException extends BusinessException {

    /**
     * 创建一个包含具体错误信息的断言失败异常
     *
     * @param code    错误代码, 可在 ct_message_info 表中查找错误定义
     * @param message 具体的错误说明, 可以是格式化模板
     * @param args    附加信息, 同时也是 message 格式化的格式化参数
     */
    AssertFailException(String code, String message, Object... args) {
        super(code, StringUtils.isEmpty(message) ? message : String.format(message, args), null);
        setInfo(args);
    }
}
