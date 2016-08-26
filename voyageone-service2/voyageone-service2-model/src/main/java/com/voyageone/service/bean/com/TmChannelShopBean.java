package com.voyageone.service.bean.com;

import java.util.List;

import com.voyageone.service.model.com.TmChannelShopModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/19
 */
public class TmChannelShopBean extends TmChannelShopModel {
	
	private String channelName;
	
	private String cartName;
	
	private List<TmChannelShopConfigBean> cartShopConfig;

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

	public List<TmChannelShopConfigBean> getCartShopConfig() {
		return cartShopConfig;
	}

	public void setCartShopConfig(List<TmChannelShopConfigBean> cartShopConfig) {
		this.cartShopConfig = cartShopConfig;
	}
	
}
