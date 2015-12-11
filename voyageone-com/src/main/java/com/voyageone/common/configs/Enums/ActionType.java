package com.voyageone.common.configs.Enums;

/**
 * Created by lewis on 15-12-9.
 */
public enum ActionType {

    //添加属性
    Add(0),

    //根据属性id删除
    RemoveById(1),

    //先根据id删除，再根据name删除
    RemoveByIdAndName(2),

    //更新id和name
    Update(3);

    private int value;

    public int getValue() {
        return value;
    }

    ActionType(int value) {
        this.value = value;
    }

    public static ActionType valueOf(int value){
        switch (value){
            case 0:
                return Add;
            case 1:
                return RemoveById;
            case 2:
                return RemoveByIdAndName;
            case 3:
                return Update;

        }
        return null;
    }



}
