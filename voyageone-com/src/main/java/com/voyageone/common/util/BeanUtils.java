package com.voyageone.common.util;

import org.springframework.cglib.beans.BeanCopier;

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
}
