package com.voyageone.cms.formbean;

import java.util.List;

import com.voyageone.cms.modelbean.SizeChartDetail;

public class SizeChartInfo extends BaseBean{
    
	private String channelId;
	
	private String sizeChartId;
	
	private String sizeChartName;
	
	private String size;
	
	private String sizeCn;
	
	private String sizeChartImageUrl;
	
	private String cartId;
	
	private String sizeChartModelId;
	
	private List<SizeChartDetail> sizeList;


	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSizeChartId() {
		return sizeChartId;
	}

	public void setSizeChartId(String sizeChartId) {
		this.sizeChartId = sizeChartId;
	}

	public String getSizeChartName() {
		return sizeChartName;
	}

	public void setSizeChartName(String sizeChartName) {
		this.sizeChartName = sizeChartName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSizeCn() {
		return sizeCn;
	}

	public void setSizeCn(String sizeCn) {
		this.sizeCn = sizeCn;
	}

	public String getSizeChartImageUrl() {
		return sizeChartImageUrl;
	}

	public void setSizeChartImageUrl(String sizeChartImageUrl) {
		this.sizeChartImageUrl = sizeChartImageUrl;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getSizeChartModelId() {
		return sizeChartModelId;
	}

	public void setSizeChartModelId(String sizeChartModelId) {
		this.sizeChartModelId = sizeChartModelId;
	}

	public List<SizeChartDetail> getSizeList() {
		return sizeList;
	}

	public void setSizeList(List<SizeChartDetail> sizeList) {
		this.sizeList = sizeList;
	}
	
	
}
