package com.voyageone.service.bean.cms.translation;

/**
 * Created by rex.wu on 2016/11/1.
 */
public class TranslationTaskBean_GroupProduct {

    private String code;
    private String iamge;
    private int inventory;

    public String getIamge() {
        return iamge;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIamge(String iamge) {
        this.iamge = iamge;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
