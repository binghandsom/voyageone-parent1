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
    public static T toModel(Map<String, Object> map, Class<T> classInfo) throws IllegalAccessException, InstantiationException {
        T model = classInfo.newInstance();
        List<Field> listField = ReflectUtil.getListField(model.getClass());
        return toModel(map, classInfo, listField);
    }
    public static T toModel(Map<String, Object> map, Class<T> classInfo, List<Field> listField) throws IllegalAccessException, InstantiationException {
        T model = classInfo.newInstance();
        for (Field field : listField) {
            Object value = map.get(field.getName());
            if (value != null) {
                field.set(model, value);
            }
        }
        return model;
    }
    public static List<T> toModelList(List<Map<String, Object>> listMap, Class<T> classInfo) throws InstantiationException, IllegalAccessException {
        List<T> listModel = new ArrayList<>();
        List<Field> listField = ReflectUtil.getListField(classInfo);
        for (Map<String, Object> map : listMap) {
            listModel.add(toModel(map, classInfo, listField));
        }
        return listModel;
    }
}
