package com.voyageone.service.enums.com;

/**
 * Created by dell on 2016/12/27.
 */
public enum EnumTaskLog_Status {
    success(1),
    failed(2);
    short id;

    EnumTaskLog_Status(int id) {
        this.id = (short) id;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }
}
