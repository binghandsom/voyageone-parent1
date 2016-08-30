package com.voyageone.common.util;

/**
 * Created by dell on 2016/8/30.
 */
public class ConvertUtil {
    public static long toLong(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Long.parseLong(value.toString());
    }
    public static int toInt(Object value) {
        if (value == null) return 0;
        if (StringUtils.isEmpty(value.toString())) return 0;
        return Integer.parseInt(value.toString());
    }
    public static boolean toBoolean(Object value) {
        if (value == null) return false;
        if (StringUtils.isEmpty(value.toString())) return false;
        return Boolean.parseBoolean(value.toString());
    }
    public static String toString(Object value) {
        if (value == null) return "";
        return value.toString();
    }
}
