package com.voyageone.service.bean.cms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.service.model.cms.enums.Operation;

import java.io.Serializable;

/**
 * 枚举 Operation 的 Bean 定义
 * @author Jonas 9/2/15.
 * @version 2.0.0
 * @since 1.0.0
 */
public class OperationBean implements Serializable {

    private final Operation operation;

    public OperationBean(Operation operation) {
        this.operation = operation;
    }

    public String getDesc() {
        return getOperation().desc();
    }

    public boolean isSingle() {
        return getOperation().isSingle();
    }

    public String getName() {
        return getOperation().name();
    }

    @JsonIgnore
    public Operation getOperation() {
        return operation;
    }
}
