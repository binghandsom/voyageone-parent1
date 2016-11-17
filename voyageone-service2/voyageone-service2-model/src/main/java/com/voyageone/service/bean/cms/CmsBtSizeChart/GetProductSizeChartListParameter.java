package com.voyageone.service.bean.cms.CmsBtSizeChart;

/**
 * Created by dell on 2016/11/15.
 */
public class GetProductSizeChartListParameter {

    String channelId;
    String brandName;
    String productType;
    String sizeType;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }
}
