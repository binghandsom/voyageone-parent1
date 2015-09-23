/**
 * 
 */
package com.voyageone.batch.wms.modelbean;

import java.util.List;

/**
 * @author jack
 * 同步到synship使用的Bean（为了和synship接口保持一致，请不要随意修改变量名）
 */
public class PostSynshipData {

	private String channelId;
	
	private String screctKey;
	
	private String sessionKey;

	private List<SyncReservationBean> orderDetailDatas;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getScrectKey() {
		return screctKey;
	}

	public void setScrectKey(String screctKey) {
		this.screctKey = screctKey;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	public List<SyncReservationBean> getOrderDetailDatas() {
		return orderDetailDatas;
	}

	public void setOrderDetailDatas(List<SyncReservationBean> orderDetailDatas) {
		this.orderDetailDatas = orderDetailDatas;
	}
}
