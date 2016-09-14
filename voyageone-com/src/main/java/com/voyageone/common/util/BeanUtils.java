package com.voyageone.common.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.cglib.beans.BeanCopier;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 帮助类, 提供实例属性复制, 实例克隆等帮助方法
 *
 * @author liangchuanyu
 * @author jonas
 * @version 2.4.0
 * @since 2.0.0
 */
public final class BeanUtils {

    /**
     * 将 source 的属性值复制到 target 上
     *
     * @param source 被复制的实例
     * @param target 复制到该实例
     */
    public static void copy(Object source, Object target) {
        BeanCopier copier = BeanCopier.create(source.getClass(), target.getClass(), false);
        copier.copy(source, target, null);
    }

    /**
     * 克隆一个实例
     *
     * @param source 被克隆的实例
     * @param <T>    实例的类型
     * @return 包含被克隆实例内容的新实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T clone(T source) {

        Class<T> sourceClass = (Class<T>) source.getClass();

        BeanCopier copier = BeanCopier.create(sourceClass, sourceClass, false);
        T target;
        try {
            target = sourceClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copier.copy(source, target, null);
        return target;
    }

    /**
     * toModelList
     */
    public static <T> List<T> toModelList(List<Map<String, Object>> listMap, Class<T> classInfo) {
        List<T> listModel = new ArrayList<>();
        for (Map<String, Object> map : listMap) {
            listModel.add(toModel(map, classInfo));
        }
        return listModel;
    }

    /**
     * toModel
     */
    public static <T> T toModel(Map<String, Object> map, Class<T> classInfo) {
        T model;
        try {
            model = classInfo.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        copyProperties(map, model);
        return model;
    }


    /**
     * copyProperties
     *
     * @param source map
     * @param target bean
     */
    private static void copyProperties(Map<String, Object> source, Object target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(target);
        beanWrapper.setPropertyValues(source);
    }


    /**
     * toMapList
     */
    public static <T> List<Map<String, Object>> toMapList(List<T> modelList) {
        if (modelList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> listMap = new ArrayList<>();
        for (T entity : modelList) {
            listMap.add(toMap(entity));
        }
        return listMap;
    }

    /**
     * toMap
     */
    public static <T> Map<String, Object> toMap(T entity) {
        Map<String, Object> map = new HashMap<>();
        copyProperties(entity, map);
        return map;
    }


    /**
     * copyProperties
     *
     * @param source bean
     * @param target map
     */
    public static void copyProperties(Object source, Map<String, Object> target) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor aDescriptor : descriptor) {
            String name = aDescriptor.getName();
            target.put(name, beanWrapper.getPropertyValue(name));
        }
    }
}
