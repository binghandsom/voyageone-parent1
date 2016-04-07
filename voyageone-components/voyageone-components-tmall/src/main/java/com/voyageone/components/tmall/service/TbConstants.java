package com.voyageone.components.tmall.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author someone. 2015-08-14.
 * @version 1.1
 * @since 1.0
 */
class TbConstants {

    static Map<String, Integer> sizeSortMap;

    static {
        initSizeSortMap();
    }

    private static void initSizeSortMap() {

        sizeSortMap = new HashMap<>(500);

        for (int i = 0; i < 100; i++) {
            String v = String.valueOf(i);
            int sortIndex = i * 2;
            // 纯数字系列
            sizeSortMap.put(v, sortIndex);
            sizeSortMap.put(v + ".5", sortIndex + 1);
            // 纯数字系列(cm)
            sizeSortMap.put(v + "cm", sortIndex + 100);
            sizeSortMap.put(v + ".5cm", sortIndex + 101);
        }

        // SM系列
        sizeSortMap.put("XXX", 400);
        sizeSortMap.put("XXS", 401);
        sizeSortMap.put("XS", 402);
        sizeSortMap.put("XS/S", 403);
        sizeSortMap.put("XSS", 404);
        sizeSortMap.put("S", 405);
        sizeSortMap.put("S/M", 406);
        sizeSortMap.put("M", 407);
        sizeSortMap.put("M/L", 408);
        sizeSortMap.put("L", 409);
        sizeSortMap.put("XL", 410);
        sizeSortMap.put("XXL", 411);

        // 包的尺码不参与排序(放最后)
        sizeSortMap.put("N/S", 412);
        // OneSize尺码不参与排序(放最后)
        sizeSortMap.put("O/S", 413);
        sizeSortMap.put("OneSize", 414);
    }
}
