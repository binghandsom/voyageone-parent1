package com.voyageone.web2.sdk.api.response.vms;

import com.voyageone.web2.sdk.api.VoApiResponse;


/**
 * /vms/order/synOrderShipment
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderShipmentSynResponse extends VoApiResponse {

	private Integer shipmentId;

	public Integer getShipmentId() {
		return shipmentId;
	}

	public void setShipmentId(Integer shipmentId) {
		this.shipmentId = shipmentId;
	}
}
