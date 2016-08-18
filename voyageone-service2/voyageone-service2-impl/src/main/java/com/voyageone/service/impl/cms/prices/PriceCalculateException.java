package com.voyageone.service.impl.cms.prices;

/**
 * 价格计算异常
 * <p>
 * 调用 {@link PriceService} 的 {@code setRetailPrice} 时, 当内部在计算过程中出现错误, 或部分计算结果非法时, 抛出该错误
 * <p>
 * Created by jonas on 8/8/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class PriceCalculateException extends Exception {

    PriceCalculateException(String message) {
        super(message);
    }

    PriceCalculateException(String message, Throwable cause) {
        super(message, cause);
    }

    PriceCalculateException(String format, Object... args) {
        super(String.format(format, args));
    }
}
