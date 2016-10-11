package com.voyageone.common.util;

import com.voyageone.common.util.excel.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/27.
 *
 * @author dell
 * @version 2.2.0
 * @since 2.0.0
 */
public final class MapUtil {

    public static Map<String, Object> toMap(Object... args) {

        if (args.length % 2 != 0)
            throw new IllegalArgumentException("参数数组长度错误！键值匹配的参数数组应该是偶数长度！");

        Map<String, Object> map = new HashMap<>();

        for (int k = 0, v = 1; k < args.length; k += 2, v += 2)
            map.put(String.valueOf(args[k]), args[v]);

        return map;
    }
}
