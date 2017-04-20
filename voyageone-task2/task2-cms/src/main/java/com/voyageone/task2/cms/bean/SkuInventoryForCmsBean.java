package com.voyageone.task2.cms.bean;

/**
 * Created by rex on 2016/12/13.
 * SKU级别库存信息Bean
 */
public class SkuInventoryForCmsBean {

    private String channelId;
    private String code;
    private String sku;
    private Integer qty;

    public SkuInventoryForCmsBean() {
    }

    public SkuInventoryForCmsBean(String channelId, String code, String sku) {
        this.channelId = channelId;
        this.code = code;
        this.sku = sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SkuInventoryForCmsBean that = (SkuInventoryForCmsBean) o;
        if (channelId == null || code == null || sku == null)
            return false;
        if (channelId.equals(that.channelId) && code.equalsIgnoreCase(that.code) && sku.equalsIgnoreCase(that.sku))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int result = (channelId == null ? "" : channelId).hashCode();
        result = 31 * result + (code == null ? "" : code).toLowerCase().hashCode();
        result = 31 * result + (sku == null ? "" : sku).toLowerCase().hashCode();
        return result;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
