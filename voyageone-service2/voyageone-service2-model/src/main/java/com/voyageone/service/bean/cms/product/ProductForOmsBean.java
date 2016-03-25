package com.voyageone.service.bean.cms.product;

/**
 * @author Edward
 * @version 2.0.0, 16/2/2
 */
public class ProductForOmsBean {

    private String channelId;

    /**
     * sku
     */
    private String sku;

    /**
     * product name
     */
    private String product;

    /**
     * description
     */
    private String description;

    /**
     * price
     */
    private String pricePerUnit;

    /**
     * inventory
     */
    private String inventory;

    /**
     * imgPath
     */
    private String imgPath;

    /**
     * skuTmallUrl
     */
    private String skuTmallUrl;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(String pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getSkuTmallUrl() {
        return skuTmallUrl;
    }

    public void setSkuTmallUrl(String skuTmallUrl) {
        this.skuTmallUrl = skuTmallUrl;
    }
}
