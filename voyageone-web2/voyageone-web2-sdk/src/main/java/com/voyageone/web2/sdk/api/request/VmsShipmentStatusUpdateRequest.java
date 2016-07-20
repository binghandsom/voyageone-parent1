package com.voyageone.web2.sdk.api.request;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.VmsOrderStatusUpdateResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

/**
 *
 * /vms/order/updateShipmentStatus
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsShipmentStatusUpdateRequest extends VoApiRequest<VmsOrderStatusUpdateResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/updateShipmentStatus";
	}

	/**
	 * channelId(必须)
	 */
	private String channelId;

	/**
	 * shipmentId（必须）
	 */
	private Integer shipmentId;

	/**
	 * status（4：Arrived；5：Received；6：Receive with Error）（必须）
	 */
	private String status;

	@Override
	public void requestCheck() throws ApiRuleException {
		RequestUtils.checkNotEmpty("channelId", channelId);
		RequestUtils.checkNotEmpty("shipmentId", shipmentId);
		RequestUtils.checkNotEmpty("status", status);
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Integer shipmentId) {
		this.shipmentId = shipmentId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}