package com.voyageone.common.util;

import com.voyageone.common.util.excel.ReflectUtil;
import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/27.
 */
public class MapUtil {
    public static Map<String, Object> toMap(T entity) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        List<Field> listField = ReflectUtil.getListField(entity.getClass());
        return toMap(entity, listField);
    }
    public static Map<String, Object> toMap(T entity, List<Field> listField) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Field field : listField) {
            map.put(field.getName(), field.get(entity));
        }
        return map;
    }
    public static List<Map<String, Object>> toListMap(List<T> list) throws IllegalAccessException {
        List<Field> listField = ReflectUtil.getListField(list.get(0).getClass());
        List<Map<String, Object>> listMap = new ArrayList();
        for (T entity : list) {
            listMap.add(toMap(entity, listField));
        }
        return listMap;
    }
}
