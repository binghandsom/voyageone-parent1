package com.voyageone.service.enums.com;

/**
 * Created by dell on 2016/12/27.
 */
public enum EnumTaskLog_LogType {
    MQJob(1),
    Job(0);
    short id;
    String name;

    EnumTaskLog_LogType(int id) {
        this.id = (short) id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }
}
