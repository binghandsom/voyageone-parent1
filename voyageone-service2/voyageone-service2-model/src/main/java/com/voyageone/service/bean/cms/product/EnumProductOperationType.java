package com.voyageone.service.bean.cms.product;

/**
 * Created by dell on 2016/7/15.
 */
public enum  EnumProductOperationType {
    Add(0, "新增");
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    EnumProductOperationType(int id, String name) {
        this.setId(id);
        this.setName(name);
    }

    public static EnumProductOperationType get(Object id) {
        int value=Integer.parseInt(id.toString());
        EnumProductOperationType[] list = EnumProductOperationType.values();
        for (EnumProductOperationType operationType : list) {
            if (operationType.getId() == value) {
                return operationType;
            }
        }
        return null;
    }
    public static String getNameById(Object id)
    {
        EnumProductOperationType operationType=get(id);
        if(operationType!=null) return operationType.getName();
        return "";
    }
}
