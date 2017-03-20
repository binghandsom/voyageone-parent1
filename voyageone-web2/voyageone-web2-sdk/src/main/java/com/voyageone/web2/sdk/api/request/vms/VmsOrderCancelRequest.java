package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderCancelResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 *
 * /vms/order/cancelOrder
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderCancelRequest extends VoApiRequest<VmsOrderCancelResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/cancelOrder";
	}

	/**
	 * channelId(必须)
	 */
	private String channelId;

	/**
	 * 需要取消的reservationId列表
	 */
	private List<String> reservationIdList;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId", channelId);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public List<String> getReservationIdList() {
		return reservationIdList;
	}

	public void setReservationIdList(List<String> reservationIdList) {
		this.reservationIdList = reservationIdList;
	}
}