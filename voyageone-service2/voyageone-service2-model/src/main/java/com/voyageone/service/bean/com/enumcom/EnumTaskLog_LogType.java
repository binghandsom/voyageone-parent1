package com.voyageone.service.bean.com.enumcom;

/**
 * Created by dell on 2016/12/27.
 */
public enum  EnumTaskLog_LogType {
    success(1),
    failed(0);
    short id;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    EnumTaskLog_LogType(int id) {
        this.id = (short) id;
    }
}
