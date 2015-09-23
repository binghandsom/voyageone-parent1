package com.voyageone.batch.bi.bean.configsbean;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

import com.voyageone.batch.bi.bean.modelbean.ShopChannelEcommBean;

/**
 * Created by Kylin on 2015/7/17.
 */
public class DriverConfigBean {
	private ShopChannelEcommBean shopBean;
    private SessionId session_id;
    private WebDriver initial_driver;
	public ShopChannelEcommBean getShopBean() {
		return shopBean;
	}
	public void setShopBean(ShopChannelEcommBean shopBean) {
		this.shopBean = shopBean;
	}
	public SessionId getSession_id() {
		return session_id;
	}
	public void setSession_id(SessionId session_id) {
		this.session_id = session_id;
	}
	public WebDriver getInitial_driver() {
		return initial_driver;
	}
	public void setInitial_driver(WebDriver initial_driver) {
		this.initial_driver = initial_driver;
	}

    
}
