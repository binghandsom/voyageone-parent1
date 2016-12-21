package com.voyageone.service.bean.com;

import com.voyageone.service.model.com.ComMtValueChannelModel;

/**
 * @author Wangtd
 * @since 2.0.0 2016/8/17
 */
public class ComMtValueChannelBean extends ComMtValueChannelModel {
	
	private String channelName;
	
	private String typeName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
