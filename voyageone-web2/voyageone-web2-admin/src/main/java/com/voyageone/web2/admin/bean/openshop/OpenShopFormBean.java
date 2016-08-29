package com.voyageone.web2.admin.bean.openshop;

import java.util.List;

import com.voyageone.service.bean.com.ComMtTaskBean;
import com.voyageone.service.bean.com.ComMtThirdPartyConfigBean;
import com.voyageone.service.bean.com.ComMtTrackingInfoConfigBean;
import com.voyageone.service.bean.com.ComMtValueChannelBean;
import com.voyageone.service.bean.com.TmCarrierChannelBean;
import com.voyageone.service.bean.com.TmChannelShopBean;
import com.voyageone.service.bean.com.TmOrderChannelBean;
import com.voyageone.service.bean.com.TmSmsConfigBean;
import com.voyageone.service.bean.com.WmsMtStoreBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/29
 */
public class OpenShopFormBean {
	
	private TmOrderChannelBean channel;
	
	private List<TmSmsConfigBean> sms;
	
	private List<ComMtThirdPartyConfigBean> thirdParty;
	
	private List<TmCarrierChannelBean> carrier;
	
	private List<ComMtValueChannelBean> channelAttr;
	
	private List<WmsMtStoreBean> store;
	
	private List<TmChannelShopBean> cartShop;
	
	private List<ComMtTrackingInfoConfigBean> cartTracking;
	
	private List<ComMtTaskBean> task;

	public TmOrderChannelBean getChannel() {
		return channel;
	}

	public void setChannel(TmOrderChannelBean channel) {
		this.channel = channel;
	}

	public List<TmSmsConfigBean> getSms() {
		return sms;
	}

	public void setSms(List<TmSmsConfigBean> sms) {
		this.sms = sms;
	}

	public List<ComMtThirdPartyConfigBean> getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(List<ComMtThirdPartyConfigBean> thirdParty) {
		this.thirdParty = thirdParty;
	}

	public List<TmCarrierChannelBean> getCarrier() {
		return carrier;
	}

	public void setCarrier(List<TmCarrierChannelBean> carrier) {
		this.carrier = carrier;
	}

	public List<ComMtValueChannelBean> getChannelAttr() {
		return channelAttr;
	}

	public void setChannelAttr(List<ComMtValueChannelBean> channelAttr) {
		this.channelAttr = channelAttr;
	}

	public List<WmsMtStoreBean> getStore() {
		return store;
	}

	public void setStore(List<WmsMtStoreBean> store) {
		this.store = store;
	}

	public List<TmChannelShopBean> getCartShop() {
		return cartShop;
	}

	public void setCartShop(List<TmChannelShopBean> cartShop) {
		this.cartShop = cartShop;
	}

	public List<ComMtTrackingInfoConfigBean> getCartTracking() {
		return cartTracking;
	}

	public void setCartTracking(List<ComMtTrackingInfoConfigBean> cartTracking) {
		this.cartTracking = cartTracking;
	}

	public List<ComMtTaskBean> getTask() {
		return task;
	}

	public void setTask(List<ComMtTaskBean> task) {
		this.task = task;
	}
	
}
