package com.voyageone.components.intltarget.bean.cart;

/**
 * @author aooer 2016/4/13.
 * @version 2.0.0
 * @since 2.0.0
 */
public class TargetCartCheckoutRequest {

    private String tcPinNum;

    private String storeIp;

    private String physicalStoreId;

    /********* getter setter ********/

    public String getTcPinNum() {
        return tcPinNum;
    }

    public void setTcPinNum(String tcPinNum) {
        this.tcPinNum = tcPinNum;
    }

    public String getStoreIp() {
        return storeIp;
    }

    public void setStoreIp(String storeIp) {
        this.storeIp = storeIp;
    }

    public String getPhysicalStoreId() {
        return physicalStoreId;
    }

    public void setPhysicalStoreId(String physicalStoreId) {
        this.physicalStoreId = physicalStoreId;
    }
}
