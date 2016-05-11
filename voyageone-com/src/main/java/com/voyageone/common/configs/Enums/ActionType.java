package com.voyageone.common.configs.Enums;

/**
 * Created by lewis on 15-12-9.
 */
public enum ActionType {

    //忽略
    IGNORE(-1),

    //添加属性
    ADD(0),

    //根据属性id删除
    REMOVE(1),

    //更新id和name
    UPDATE(2);

    private int value;

    public int getValue() {
        return value;
    }

    ActionType(int value) {
        this.value = value;
    }

    public static ActionType valueOf(int value){
        switch (value){
            case -1:
                return IGNORE;
            case 0:
                return ADD;
            case 1:
                return REMOVE;
            case 2:
                return UPDATE;
            default:
                return null;
        }
    }



}
