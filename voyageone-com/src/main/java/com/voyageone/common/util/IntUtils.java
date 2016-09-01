package com.voyageone.common.util;

/**
 * Created by dell on 2016/8/26.
 */
public class IntUtils {
    public static int parseInt(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Integer.parseInt(value.toString());
    }
}
