package com.voyageone.service.bean.admin;

import com.voyageone.service.model.admin.TmSmsConfigModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/15
 */
public class TmSmsConfigBean extends TmSmsConfigModel {
	
	private String channelName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
}
