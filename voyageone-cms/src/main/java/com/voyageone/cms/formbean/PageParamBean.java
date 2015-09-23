package com.voyageone.cms.formbean;

import com.voyageone.core.ajax.AjaxRequestBean;

public class PageParamBean extends AjaxRequestBean{
	
	private int categoryId;
	
	private String channelId;
	
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	protected String[] getValidateSorts() {
		// TODO Auto-generated method stub
		return null;
	}

}
