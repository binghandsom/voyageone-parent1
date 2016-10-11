package com.voyageone.web2.vms.bean;

import java.util.List;

/**
 * 渠道配置
 * Created by vantis on 16-7-7.
 */
public class VmsChannelSettingBean {
    private String channelId;
    private String vendorOperateType;
    private String salePriceShow = "0";
    private String defaultDeliveryCompany;
    private String namingConverter;
    private String emailAddress;
    private List<String> additionalAttributes;
    private List<String> additionalAttributesClass;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

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

    public String getDefaultDeliveryCompany() {
        return defaultDeliveryCompany;
    }

    public void setDefaultDeliveryCompany(String defaultDeliveryCompany) {
        this.defaultDeliveryCompany = defaultDeliveryCompany;
    }

    public String getNamingConverter() {
        return namingConverter;
    }

    public void setNamingConverter(String namingConverter) {
        this.namingConverter = namingConverter;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<String> getAdditionalAttributes() {
        return additionalAttributes;
    }

    public void setAdditionalAttributes(List<String> additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    public List<String> getAdditionalAttributesClass() {
        return additionalAttributesClass;
    }

    public void setAdditionalAttributesClass(List<String> additionalAttributesClass) {
        this.additionalAttributesClass = additionalAttributesClass;
    }
}
