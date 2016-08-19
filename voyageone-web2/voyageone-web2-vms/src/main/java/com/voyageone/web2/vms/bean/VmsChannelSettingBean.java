package com.voyageone.web2.vms.bean;

/**
 * 渠道配置
 * Created by vantis on 16-7-7.
 */
public class VmsChannelSettingBean {
    private String vendorOperateType;
    private String salePriceShow = "0";

    public String getVendorOperateType() {
        return vendorOperateType;
    }

    public void setVendorOperateType(String vendorOperateType) {
        this.vendorOperateType = vendorOperateType;
    }

    public String getSalePriceShow() {
        return salePriceShow;
    }

    public void setSalePriceShow(String salePriceShow) {
        this.salePriceShow = salePriceShow;
    }
}
