package com.voyageone.service.impl.cms.prices;

/**
 * 价格计算异常
 * <p>
 * 当调用 {@link PriceService} 的 {@code setRetailPrice()} 失败时, 抛出该异常
 * <p>
 * Created by minejjk on 8/8/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public class PriceCalculateException extends Exception {

    PriceCalculateException(String message) {
        super(message);
    }

    PriceCalculateException(String format, Object... args) {
        super(String.format(format, args));
    }
}
