package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderStatusUpdateResponse;
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

	/**
	 * receivedTime（status = 4：Arrived；5：Received时 必须）
	 */
	private Long operateTime;

	/**
	 * receiver（status = 4：Arrived；5：Received时 必须）
	 */
	private String operator;

	/**
	 * comment(6：Receive with Error时 必须)
	 */
	private String comment;

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

	public Long getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Long operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}