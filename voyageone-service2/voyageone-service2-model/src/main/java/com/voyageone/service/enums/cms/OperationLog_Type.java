package com.voyageone.service.enums.cms;

import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/12/27.
 */
public enum OperationLog_Type {
    success(1, "成功"),
    successIncludeFail(2,"失败异常"),
    parameterException(3,"参数异常"),
    configException(4,"配置异常"),
    businessException(5,"业务异常"),
    unknownException(6,"未知异常");

    short id;

    String name;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    OperationLog_Type(int id, String name) {
        this.id = (short) id;
        this.name = name;
    }

    OperationLog_Type(int id) {
        this.id = (short) id;
    }

    public static OperationLog_Type get(short id) {
        for (OperationLog_Type operationLog_type : OperationLog_Type.values()) {
            if (operationLog_type.getId() == id) {
                return operationLog_type;
            }
        }
        return null;
    }


    public static List<Map<String,Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map = null;
        for (OperationLog_Type operationLog_type : OperationLog_Type.values()) {
            map = new HashedMap();
            map.put("id", operationLog_type.getId());
            map.put("name", operationLog_type.getName());
            list.add(map);
        }
        return list;
    }
}
