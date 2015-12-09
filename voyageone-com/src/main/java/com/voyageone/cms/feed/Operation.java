package com.voyageone.cms.feed;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 第三方品牌数据条件可用的比较操作
 * <p>
 * Created by Jonas on 9/2/15.
 */
public enum Operation {

    /**
     * 是空
     */
    IS_NULL("为空", true),

    /**
     * 不是空
     */
    IS_NOT_NULL("不为空", true),

    /**
     * 等于
     */
    EQUALS("等于"),

    /**
     * 不等于
     */
    NOT_EQUALS("不等于");

    private final OperationBean bean;

    private final String desc;

    private final boolean single;

    public String desc() {
        return this.desc;
    }

    public boolean isSingle() {
        return single;
    }

    @JsonValue
    public OperationBean bean() {
        return bean;
    }

    Operation(String desc) {
        this(desc, false);
    }

    Operation(String desc, boolean single) {
        this.desc = desc;
        this.single = single;
        this.bean = new OperationBean(this);
    }
}
