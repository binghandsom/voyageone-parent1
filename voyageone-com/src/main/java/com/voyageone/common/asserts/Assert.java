package com.voyageone.common.asserts;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 断言检查
 * <p>
 * 用来对变量或参数的值, 进行通用的检查
 * <p>
 * 当不符合预期要求时, 会抛出 {@link AssertFailException}, 并携带固定 Message 和 ct_message_info 对应的 Code
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public final class Assert {

    /*
     * 默认的信息定义
     */

    private static final String IS_TRUE = "[Assertion failed] - {this expression} must be true";
    private static final String IS_NULL = "[Assertion failed] - {the object} value must be null";
    private static final String NOT_NULL = "[Assertion failed] - {this value} is required; it must not be null";
    private static final String HAS_LENGTH = "[Assertion failed] - {this String} value must have length; it must not be null or empty";
    private static final String HAS_TEXT = "[Assertion failed] - {this String} value must have text; it must not be null, empty, or blank";
    private static final String DOES_NOT_CONTAIN = "[Assertion failed] - {this String} value must not contain the substring [ %s ]";
    private static final String NOT_EMPTY_ARRAY = "[Assertion failed] - {this array} must not be empty: it must contain at least 1 element";
    private static final String NOT_EMPTY_MAP = "[Assertion failed] - {this map} must not be empty; it must contain at least one entry";
    private static final String NO_NULL_ELEMENTS = "[Assertion failed] - {this array} must not contain any null elements";
    private static final String NOT_EMPTY_COLLECTION = "[Assertion failed] - {this collection} must not be empty: it must contain at least 1 element";
    private static final String IS_INSTANCE_OF = "[Assertion failed] - {Object} of class [ %s ] must be an instance of %s";
    private static final String IS_ASSIGNABLE = "[Assertion failed] - %s is not assignable to %s";

    /*
     * 对外公开的静态入口方法, 即断言具体判断逻辑
     */

    /**
     * 要求结果必须是 true
     *
     * @param expression 目标结果
     */
    public static Assert isTrue(boolean expression) {
        return new Assert(expression, IS_TRUE);
    }

    /**
     * 要求目标对象必须是 null
     *
     * @param object 目标对象
     */
    public static Assert isNull(Object object) {
        return new Assert(object == null, IS_NULL);
    }

    /**
     * 要求目标对象必须不是 null
     *
     * @param object 目标对象
     */
    public static Assert notNull(Object object) {
        return new Assert(object != null, NOT_NULL);
    }

    /**
     * 要求文本必须有内容, 可为空格
     *
     * @param text 目标文本
     */
    public static Assert hasLength(String text) {
        return new Assert(StringUtils.hasLength(text), HAS_LENGTH);
    }

    /**
     * 要求文本必须有内容, 且不能为空格
     *
     * @param text 目标文本
     */
    public static Assert hasText(String text) {
        return new Assert(StringUtils.hasText(text), HAS_TEXT);
    }

    /**
     * 要求目标文本必须包含 substring 指定的内容
     *
     * @param textToSearch 被搜索文本
     * @param substring    搜索内容
     */
    public static Assert doesNotContain(String textToSearch, String substring) {
        return new Assert(!StringUtils.hasLength(textToSearch) || !StringUtils.hasLength(substring) ||
                !textToSearch.contains(substring), String.format(DOES_NOT_CONTAIN, substring));
    }

    /**
     * 要求数组必须包含元素, 可以是 null
     *
     * @param array 被检查数组
     */
    public static Assert notEmpty(Object[] array) {
        return new Assert(!ObjectUtils.isEmpty(array), NOT_EMPTY_ARRAY);
    }

    /**
     * 要求数组必须包含元素, 且元素都不是 null
     *
     * @param array 被检查数组
     */
    public static Assert noNullElements(Object[] array) {

        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    return new Assert(false, NO_NULL_ELEMENTS);
                }
            }
        }

        return new Assert(true, null);
    }

    /**
     * 要求集合必须包含元素, 可以是 null
     *
     * @param collection 被检查集合
     */
    public static Assert notEmpty(Collection<?> collection) {
        return new Assert(!CollectionUtils.isEmpty(collection), NOT_EMPTY_COLLECTION);
    }

    /**
     * 要求字典必须包含元素
     *
     * @param map 被检查字典
     */
    public static Assert notEmpty(Map<?, ?> map) {
        return new Assert(!CollectionUtils.isEmpty(map), NOT_EMPTY_MAP);
    }

    /**
     * 要求目标被检查对象必须是指定类型的实例
     *
     * @param type 目标类型
     * @param obj  被检查对象
     */
    public static Assert isInstanceOf(Class<?> type, Object obj) {

        notNull(type).elseThrowDefaultWithTitle("type (Assert.isInstanceOf)");

        String objClassName = (obj != null ? obj.getClass().getName() : "null");

        return new Assert(type.isInstance(obj), String.format(IS_INSTANCE_OF, objClassName, type));
    }

    /**
     * 要求目标类型必须继承自指定类型
     *
     * @param superType 指定的父类型
     * @param subType   被检查的子类型
     */
    public static Assert isAssignable(Class<?> superType, Class<?> subType) {

        notNull(superType).elseThrowDefaultWithTitle("superType (Assert.isAssignable)");

        return new Assert(subType != null && superType.isAssignableFrom(subType), String.format(IS_ASSIGNABLE, subType, superType));
    }

    /*
     * 实例内容定义部分, 定义一个断言的结果实例, 并提供后续的可选操作
     */

    private final static Pattern variablePattern = Pattern.compile("\\{(.+?)\\}");

    /**
     * 断言是否成功
     */
    private boolean assertResult;

    private String defaultMessage;

    /**
     * 创建一个包含结果的断言实例, 并为它指定一个默认的错误信息
     *
     * @param assertResult   断言是否成功
     * @param defaultMessage 当 elseThrow 不传入信息或使用 elseThrowDefault 系列方法时, 使用的默认信息
     */
    private Assert(boolean assertResult, String defaultMessage) {
        this.assertResult = assertResult;
        this.defaultMessage = defaultMessage;
    }

    /**
     * 获取格式化后的默认错误信息
     *
     * @param variableTitle 变量抬头, 用来优化默认信息, 让信息更明确
     * @param args          信息的格式化参数
     * @return 格式化后的字符串
     */
    private String getDefaultMessage(String variableTitle, Object... args) {

        String message = defaultMessage;

        // 先检查这个信息是否可以指定变量抬头
        Matcher matcher = variablePattern.matcher(message);

        // 如果不需要则直接格式化输出
        // 否则, 检查是否提供了变量的抬头
        // 如果提供了, 就替换到信息中, 否则消除掉替换标记
        // 最后格式化输出
        if (matcher.matches()) {
            if (StringUtils.isEmpty(variableTitle))
                message = matcher.replaceFirst("$1");
            else
                message = matcher.replaceFirst(variableTitle);
        }

        return String.format(message, args);
    }

    /**
     * 当断言失败时, 抛出 message 指定的错误信息, 并使用 args 格式化信息
     *
     * @param message 指定的错误信息
     * @param args    格式化信息, 同时作为异常的附加信息
     */
    public void elseThrow(String message, Object... args) {

        if (assertResult)
            return;

        if (StringUtils.isEmpty(message))
            message = getDefaultMessage(null, args);

        throw new AssertFailException(null, message, args);
    }

    /**
     * 当断言失败时, 抛出带有默认错误信息和指定的错误代码的异常, 并使用 args 格式化信息
     *
     * @param messageCode 错误代码, 在 ct_message_info 表中定义
     * @param args        格式化信息, 同时作为异常的附加信息
     */
    public void elseThrowCode(String messageCode, Object... args) {

        if (assertResult)
            return;

        String message = getDefaultMessage(null, args);

        throw new AssertFailException(messageCode, message, args);
    }

    /**
     * 当断言失败时, 抛出默认错误信息, 并使用 args 格式化信息
     *
     * @param args 格式化信息, 同时作为异常的附加信息
     */
    public void elseThrowDefault(Object... args) {
        elseThrow(null, args);
    }

    /**
     * 当断言失败时, 使用指定的变量名称和 args 格式化错误信息, 并抛出
     *
     * @param variableTitle 被检查的变量名称
     * @param args          格式化信息, 同时作为异常的附加信息
     */
    public void elseThrowDefaultWithTitle(String variableTitle, Object... args) {

        if (assertResult)
            return;

        String message = getDefaultMessage(variableTitle, args);

        elseThrow(message, args);
    }
}
