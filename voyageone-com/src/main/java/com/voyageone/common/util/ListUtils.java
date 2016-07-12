package com.voyageone.common.util;

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
}
