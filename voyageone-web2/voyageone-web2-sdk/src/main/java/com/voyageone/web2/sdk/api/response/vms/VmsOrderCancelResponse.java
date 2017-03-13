package com.voyageone.web2.sdk.api.response.vms;

import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;


/**
 * /vms/order/cancelOrder
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderCancelResponse extends VoApiResponse {

	/**
	 * 取消成功的reservationId列表
	 */
	private List<String> successReservationIdList;

	/**
	 * 取消失败的reservationId列表
	 */
	private List<String> failReservationIdList;

	public List<String> getSuccessReservationIdList() {
		return successReservationIdList;
	}

	public void setSuccessReservationIdList(List<String> successReservationIdList) {
		this.successReservationIdList = successReservationIdList;
	}

	public List<String> getFailReservationIdList() {
		return failReservationIdList;
	}

	public void setFailReservationIdList(List<String> failReservationIdList) {
		this.failReservationIdList = failReservationIdList;
	}
}
