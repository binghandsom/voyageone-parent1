package com.voyageone.common.util;

import com.voyageone.common.exceptions.IllegalValueException;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import org.springframework.util.StringUtils;

import static com.voyageone.common.exceptions.IllegalValueTypes.*;

/**
 * 断言检查
 * <p>
 * 用来对变量或参数的值, 进行通用的检查
 * <p>
 * 当不符合预期要求时, 会抛出 {@link IllegalValueException}, 并携带固定 Message 和 ct_message_info 对应的 Code
 * <p>
 * 附加信息参数只对后台 Job 有用, 它会跟随异常记录到 Issue Log 表中
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public final class Assert {

    /**
     * 要求结果必须是 true
     *
     * @param expression 目标结果
     * @param args       附加信息
     */
    public static void isTrue(boolean expression, Object... args) {
        if (!expression) {
            throw new IllegalValueException(IS_TRUE, args);
        }
    }

    /**
     * 要求目标对象必须是 null
     *
     * @param object 目标对象
     * @param args   附加信息
     */
    public static void isNull(Object object, Object... args) {
        if (object != null) {
            throw new IllegalValueException(IS_NULL, args);
        }
    }

    /**
     * 要求目标对象必须不是 null
     *
     * @param object 目标对象
     * @param args   附加信息
     */
    public static void notNull(Object object, Object... args) {
        if (object == null) {
            throw new IllegalValueException(NOT_NULL, args);
        }
    }

    /**
     * 要求文本必须有内容, 可为空格
     *
     * @param text 目标文本
     * @param args 附加信息
     */
    public static void hasLength(String text, Object... args) {
        if (!StringUtils.hasLength(text)) {
            throw new IllegalValueException(HAS_LENGTH, args);
        }
    }

    /**
     * 要求文本必须有内容, 且不能为空格
     *
     * @param text 目标文本
     * @param args 附加信息
     */
    public static void hasText(String text, Object... args) {
        if (!StringUtils.hasText(text)) {
            throw new IllegalValueException(HAS_TEXT, args);
        }
    }

    /**
     * 要求目标文本必须包含 substring 指定的内容
     *
     * @param textToSearch 被搜索文本
     * @param substring    搜索内容
     * @param args         附加信息
     */
    public static void doesNotContain(String textToSearch, String substring, Object... args) {
        if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) && textToSearch.contains(substring)) {
            throw new IllegalValueException(DOES_NOT_CONTAIN, args);
        }
    }

    /**
     * 要求数组必须包含元素, 可以是 null
     *
     * @param array 被检查数组
     * @param args  附加信息
     */
    public static void notEmpty(Object[] array, Object... args) {
        if (ObjectUtils.isEmpty(array)) {
            throw new IllegalValueException(NOT_EMPTY_ARRAY, args);
        }
    }

    /**
     * 要求数组必须包含元素, 且元素不是 null
     *
     * @param array 被检查数组
     * @param args  附加信息
     */
    public static void noNullElements(Object[] array, Object... args) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalValueException(NO_NULL_ELEMENTS, args);
                }
            }
        }
    }

    /**
     * 要求集合必须包含元素, 可以是 null
     *
     * @param collection 被检查集合
     * @param args       附加信息
     */
    public static void notEmpty(Collection<?> collection, Object... args) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new IllegalValueException(NOT_EMPTY_COLLECTION, args);
        }
    }

    /**
     * 要求字典必须包含元素
     *
     * @param map  被检查字典
     * @param args 附加信息
     */
    public static void notEmpty(Map<?, ?> map, Object... args) {
        if (CollectionUtils.isEmpty(map)) {
            throw new IllegalValueException(NOT_EMPTY_MAP, args);
        }
    }

    /**
     * 要求目标被检查对象必须是指定类型的实例
     *
     * @param type 目标类型
     * @param obj  被检查对象
     * @param args 附加信息
     */
    public static void isInstanceOf(Class<?> type, Object obj, Object... args) {

        notNull(type);

        if (!type.isInstance(obj)) {

            Object[] formatArgs = {(obj != null ? obj.getClass().getName() : "null"), type};

            throw new IllegalValueException(IS_INSTANCE_OF, formatArgs, args);
        }
    }

    /**
     * 要求目标类型必须继承自指定类型
     *
     * @param superType 指定的父类型
     * @param subType   被检查的子类型
     * @param args      附加信息
     */
    public static void isAssignable(Class<?> superType, Class<?> subType, Object... args) {
        notNull(superType);
        if (subType == null || !superType.isAssignableFrom(subType)) {

            Object[] formatArgs = {subType, superType};

            throw new IllegalValueException(IS_ASSIGNABLE, formatArgs, args);
        }
    }
}
