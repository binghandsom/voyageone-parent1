package com.voyageone.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * ListUtils
 */
public class ListUtils {

    public static boolean notNull(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isNull(List list) {
        return list == null || list.isEmpty();
    }

    public static <T> List<List<T>> getPageList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<List<T>>();
        List<T> page = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            if (i % size == 0) {
                page = new ArrayList<T>();
                result.add(page);
            }
            page.add(list.get(i));
        }
        return result;
    }
}
