package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderInfoGetResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

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
	 * reservationId（reservationId和time 2选1 必须）
	 */
	private String reservationId;

	/**
	 * timeFrom（reservationId和time 2选1 必须）
	 */
	private Long timeFrom;

	/**
	 * timeTo（reservationId和time 2选1 必须）
	 */
	private Long timeTo;

	/**
	 * type（1:按发货时间区间取得状态是shipped的物品列表，2：按取消时间区间取得状态是cancel的物品列表；通过time取数据的情况下，这个字段必须）
	 */
	private String type;

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

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public Long getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(Long timeFrom) {
		this.timeFrom = timeFrom;
	}

	public Long getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(Long timeTo) {
		this.timeTo = timeTo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}