package com.voyageone.common.exceptions;

/**
 * 非法值错误类型, 为非法值异常提供固定的错误信息和错误代码
 * <p>
 * Created by jonas on 8/2/16.
 *
 * @author jonas
 * @version 2.4.0
 * @since 2.4.0
 */
public enum IllegalValueTypes {

    /**
     * 要求给定的值必须为 true
     */
    IS_TRUE("1000007", "[Assertion failed] - this expression must be true"),
    /**
     * 要求给定的值不能为 null
     */
    IS_NULL("1000008", "[Assertion failed] - the object value must be null"),
    /**
     * 要求给定的值必须为 null
     */
    NOT_NULL("1000009", "[Assertion failed] - this value is required; it must not be null"),
    /**
     * 要求文本中必须有内容(包括空格, 即有空格也算有内容)
     */
    HAS_LENGTH("1000010", "[Assertion failed] - this String value must have length; it must not be null or empty"),
    /**
     * 要求文本中必须有实际内容(即全是空格则认为无内容)
     */
    HAS_TEXT("1000011", "[Assertion failed] - this String value must have text; it must not be null, empty, or blank"),
    /**
     * 要求文本必须不包含指定字符串
     */
    DOES_NOT_CONTAIN("1000012", "[Assertion failed] - this String value must not contain the substring [ %s ]"),
    /**
     * 要求数组必须不是空数组, 必须有元素
     */
    NOT_EMPTY_ARRAY("1000018", "[Assertion failed] - this array must not be empty: it must contain at least 1 element"),
    /**
     * 要 Map 必须不是空 Map, 必须有元素
     */
    NOT_EMPTY_MAP("1000019", "[Assertion failed] - this map must not be empty; it must contain at least one entry"),
    /**
     * 要求数组内的有实际内容, 如果已存在的元素都是 null, 也不行
     */
    NO_NULL_ELEMENTS("1000013", "[Assertion failed] - this array must not contain any null elements"),
    /**
     * 要求集合必须不是空集合
     */
    NOT_EMPTY_COLLECTION("1000014", "[Assertion failed] - this collection must not be empty: it must contain at least 1 element"),
    /**
     * 要求对象必须是指定类型
     */
    IS_INSTANCE_OF("1000015", "Object of class [ %s ] must be an instance of %s"),
    /**
     * 要求指定类型必须继承目标类型
     */
    IS_ASSIGNABLE("1000016", "%s is not assignable to %s");

    private String code;

    private String message;

    IllegalValueTypes(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
