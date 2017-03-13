package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderShipmentSynResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 *
 * /vms/order/synOrderShipment
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderShipmentSynRequest extends VoApiRequest<VmsOrderShipmentSynResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/synOrderShipment";
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
	 * expressCompany（1:UPS；2:FedEx；3:US Postal Service 必须）
	 */
	private String expressCompany;

	/**
	 * trackingNo（必须）
	 */
	private String trackingNo;

	/**
	 * shippedTime（必须）
	 */
	private Long shippedTime;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId", channelId);
		RequestUtils.checkNotEmpty("reservationId", reservationId);
		RequestUtils.checkNotEmpty("expressCompany", expressCompany);
		RequestUtils.checkNotEmpty("trackingNo", trackingNo);
		RequestUtils.checkNotEmpty("shippedTime", shippedTime);
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

	public String getExpressCompany() {
		return expressCompany;
	}

	public void setExpressCompany(String expressCompany) {
		this.expressCompany = expressCompany;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public Long getShippedTime() {
		return shippedTime;
	}

	public void setShippedTime(Long shippedTime) {
		this.shippedTime = shippedTime;
	}
}