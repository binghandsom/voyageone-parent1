package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.VmsOrderCancelResponse;
import com.voyageone.web2.sdk.api.response.VmsOrderInfoGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;

/**
 *
 * /vms/order/getOrderInfo
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderInfoGetRequest extends VoApiRequest<VmsOrderInfoGetResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/getOrderInfo";
	}

	/**
	 * channelId(必须)
	 */
	private String channelId;

	/**
	 * reservationId（reservationId和shipmentTime 2选1 必须）
	 */
	private String reservationId;

	/**
	 * shipmentTimeFrom（reservationId和shipmentTime 2选1 必须）
	 */
	private Long shipmentTimeFrom;

	/**
	 * shipmentTimeTo（reservationId和shipmentTime 2选1 必须）
	 */
	private Long shipmentTimeTo;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty(" channelId", channelId);
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

	public Long getShipmentTimeFrom() {
		return shipmentTimeFrom;
	}

	public void setShipmentTimeFrom(Long shipmentTimeFrom) {
		this.shipmentTimeFrom = shipmentTimeFrom;
	}

	public Long getShipmentTimeTo() {
		return shipmentTimeTo;
	}

	public void setShipmentTimeTo(Long shipmentTimeTo) {
		this.shipmentTimeTo = shipmentTimeTo;
	}
}