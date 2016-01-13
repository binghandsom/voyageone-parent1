package com.voyageone.cms.modelbean;

import java.util.Date;

import com.voyageone.cms.formbean.BaseBean;

public class SizeChart extends BaseBean {
	private Integer sizeChartId;

	private String channelId;
	
    private String sizeChartName;

    private Integer cartId;

    private Integer sizeChartModelId;

    private String isActive;

    private String sizeChartImageUrl;
    
    public Integer getSizeChartId() {
        return sizeChartId;
    }

    public void setSizeChartId(Integer sizeChartId) {
        this.sizeChartId = sizeChartId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    public String getSizeChartName() {
        return sizeChartName;
    }

    public void setSizeChartName(String sizeChartName) {
        this.sizeChartName = sizeChartName == null ? null : sizeChartName.trim();
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getSizeChartModelId() {
        return sizeChartModelId;
    }

    public void setSizeChartModelId(Integer sizeChartModelId) {
        this.sizeChartModelId = sizeChartModelId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive == null ? null : isActive.trim();
    }

    public String getSizeChartImageUrl() {
        return sizeChartImageUrl;
    }

    public void setSizeChartImageUrl(String sizeChartImageUrl) {
        this.sizeChartImageUrl = sizeChartImageUrl == null ? null : sizeChartImageUrl.trim();
    }
}