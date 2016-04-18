package com.voyageone.service.bean.cms;

import com.google.gson.Gson;
import com.voyageone.service.model.cms.enums.Operation;
import org.apache.commons.lang3.StringUtils;

/**
 * 第三方品牌数据比对的条件
 * Created by Jonas on 9/2/15.
 */
public class Condition {

    private String property;

    private Operation operation;

    private String value;

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static boolean valid(String condition) {

        if (StringUtils.isEmpty(condition))
            return true;

        Gson gson = new Gson();

        Condition obj = gson.fromJson(condition, Condition.class);

        return obj != null
                && !StringUtils.isEmpty(obj.getProperty()) // 比较的目标属性不能为空
                && obj.getOperation() != null // 比较的操作不能为空
                && !(!obj.getOperation().isSingle() && StringUtils.isEmpty(obj.getValue())); // 如果比较操作不是单值的话，则值也不能为空
    }
}
