package com.voyageone.service.bean.cms.enumcms;

/**
 * Created by dell on 2016/12/27.
 */
public enum OperationLog_Type {
    success(1),
    successIncludeFail(2),
    parameterException(3),
    configException(4),
    businessException(5),
    unknownException(6);
    short id;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    OperationLog_Type(int id) {
        this.id = (short) id;
    }
}
