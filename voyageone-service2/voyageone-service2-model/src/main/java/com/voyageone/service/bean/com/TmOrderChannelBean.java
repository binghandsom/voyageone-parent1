package com.voyageone.service.bean.com;

import java.util.List;

import com.voyageone.service.model.com.CtCartModel;
import com.voyageone.service.model.com.TmOrderChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/12
 */
public class TmOrderChannelBean extends TmOrderChannelModel {
	
	private List<CtCartModel> carts;
	
	private List<TmOrderChannelConfigBean> channelConfig;

	public List<CtCartModel> getCarts() {
		return carts;
	}

	public void setCarts(List<CtCartModel> carts) {
		this.carts = carts;
	}

	public List<TmOrderChannelConfigBean> getChannelConfig() {
		return channelConfig;
	}

	public void setChannelConfig(List<TmOrderChannelConfigBean> channelConfig) {
		this.channelConfig = channelConfig;
	}
	
}
