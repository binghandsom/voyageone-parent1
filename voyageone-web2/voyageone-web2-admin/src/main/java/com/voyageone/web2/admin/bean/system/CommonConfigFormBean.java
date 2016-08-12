package com.voyageone.web2.admin.bean.system;

import com.voyageone.web2.admin.AdminConstants;
import com.voyageone.web2.admin.bean.AdminFormBean;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/10
 */
public class CommonConfigFormBean extends AdminFormBean {

	private AdminConstants.ConfigType configType;
	
	private String channelId;
	
	private String cartId;
	
	private String port;
	
	private String taskId;
	
	private Long storeId;
	
	private String cfgName;
	
	private String cfgVal1;
	
	private String cfgVal2;
	
	private String cfgVal;
	
	private String comment;

	public AdminConstants.ConfigType getConfigType() {
		return configType;
	}

	public void setConfigType(AdminConstants.ConfigType configType) {
		this.configType = configType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getCfgName() {
		return cfgName;
	}

	public void setCfgName(String cfgName) {
		this.cfgName = cfgName;
	}

	public String getCfgVal1() {
		return cfgVal1;
	}

	public void setCfgVal1(String cfgVal1) {
		this.cfgVal1 = cfgVal1;
	}

	public String getCfgVal2() {
		return cfgVal2;
	}

	public void setCfgVal2(String cfgVal2) {
		this.cfgVal2 = cfgVal2;
	}
	
	public String getCfgVal() {
		return cfgVal;
	}

	public void setCfgVal(String cfgVal) {
		this.cfgVal = cfgVal;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}