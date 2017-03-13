package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderStatusUpdateResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 *
 * /vms/order/updateOrderStatus
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderStatusUpdateRequest extends VoApiRequest<VmsOrderStatusUpdateResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/updateOrderStatus";
	}

	/**
	 * channelId(必须)
	 */
	private String channelId;

	/**
	 * reservationId（必须）
	 */
	private String reservationId;

	/**
	 * status（5：Received；6：Receive Error）（必须）
	 */
	private String status;

	/**
	 * receiver（status = 5：Received时 必须）
	 */
	private String receiver;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId", channelId);
		RequestUtils.checkNotEmpty("reservationId", reservationId);
		RequestUtils.checkNotEmpty("status", status);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
}