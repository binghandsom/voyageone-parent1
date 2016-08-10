package com.voyageone.service.impl.cms.prices;

/**
 * 表示在调用 {@link PriceService} 的 {@code setPrice} 时, 内部获取的配置是非法值。计算不能正常进行。
 * <p>
 * Created by jonas on 8/9/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class IllegalPriceConfigException extends Exception {

    IllegalPriceConfigException(String message) {
        super(message);
    }

    IllegalPriceConfigException(String format, Object... args) {
        super(String.format(format, args));
    }
}
