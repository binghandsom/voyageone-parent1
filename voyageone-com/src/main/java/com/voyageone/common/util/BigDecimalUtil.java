package com.voyageone.common.util;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/4/12.
 */
public class BigDecimalUtil {
    public static BigDecimal divide(BigDecimal b1, BigDecimal b2,int scale) {
        if (b2.doubleValue() == 0) return new BigDecimal(0);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_EVEN);
    }
}
