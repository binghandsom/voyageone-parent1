package com.voyageone.service.bean.com;

import com.voyageone.service.model.com.ComMtTrackingInfoConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/22
 */
public class ComMtTrackingInfoConfigBean extends ComMtTrackingInfoConfigModel {
	
	private String channelName;
	
	private String cartName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getCartName() {
		return cartName;
	}

	public void setCartName(String cartName) {
		this.cartName = cartName;
	}
	
}
