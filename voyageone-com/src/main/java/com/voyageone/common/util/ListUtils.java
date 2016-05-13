package com.voyageone.common.util;

import java.util.List;

/**
 * Created by dell on 2016/5/13.
 */
public class ListUtils {
    public static boolean notNull(List list) {
        return list != null && list.size() > 0;
    }
    public static boolean isNull(List list) {
        return list == null || list.size() == 0;
    }
//    public static <T extends Object> T[] toArray(List<T> list) {
//        return list.toArray(new T[list.size()]);
//    }
}
