package com.voyageone.common.util;

/**
 * Created by dell on 2016/8/26.
 */
public class LongUtils {
    public static long parseLong(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Long.parseLong(value.toString());
    }

    public static String toString(Long value) {
        return toString(value, "");
    }

    public static String toString(Long value, String defaultValue) {
        if(value == null) {
            return defaultValue;
        }
        return Long.toString(value);
    }
}
