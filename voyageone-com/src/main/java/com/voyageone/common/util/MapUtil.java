package com.voyageone.common.util;

import com.voyageone.common.util.excel.ReflectUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/5/27.
 */
public class MapUtil {

    public static <T> Map<String, Object> toMap(T entity) throws IllegalAccessException {
        List<Field> listField = ReflectUtil.getListField(entity.getClass());
        return toMap(entity, listField);
    }

    public static <T> Map<String, Object> toMap(T entity, List<Field> listField) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        for (Field field : listField) {
            field.setAccessible(true);
           Object result= field.get(entity);
            if(result!=null) {
                map.put(field.getName(), result);
            }
        }
        return map;
    }

    public static <T> List<Map<String, Object>> toMapList(List<T> list) throws IllegalAccessException {
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        List<Field> listField = ReflectUtil.getListField(list.get(0).getClass());
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (T entity : list) {
            listMap.add(toMap(entity, listField));
        }
        return listMap;
    }

    public static <T> T toModel(Map<String, Object> map, Class<T> classInfo) throws IllegalAccessException, InstantiationException {
        T model = classInfo.newInstance();
        List<Field> listField = ReflectUtil.getListField(model.getClass());
        return toModel(map, classInfo, listField);
    }

    public static <T> T toModel(Map<String, Object> map, Class<T> classInfo, List<Field> listField) throws IllegalAccessException, InstantiationException {
        T model = classInfo.newInstance();
        for (Field field : listField) {
            Object value = map.get(field.getName());
            if (value != null) {
                field.set(model, value);// 可以用 ReflectUtil.setFieldValueByName(Field field, Object fieldValue, Object o) 替换
            }
        }
        return model;
    }

    public static <T> List<T> toModelList(List<Map<String, Object>> listMap, Class<T> classInfo) throws InstantiationException, IllegalAccessException {
        List<T> listModel = new ArrayList<>();
        List<Field> listField = ReflectUtil.getListField(classInfo);
        for (Map<String, Object> map : listMap) {
            listModel.add(toModel(map, classInfo, listField));
        }
        return listModel;
    }
}
