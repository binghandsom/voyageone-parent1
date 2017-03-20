package com.voyageone.web2.sdk.api.response.vms;

import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;
import java.util.Map;


/**
 * /vms/order/getOrderInfo
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderInfoGetResponse extends VoApiResponse {

	/**
	 * 返回物品列表
	 * Map包含以下内容
	 * "channelId", String型
	 * "reservationId", String型
	 * "status", String型(1:Open；2：Package；3：Shipped；5：Received；6：Receive Error；7：Cancel )
	 * "shipmentId", String型(可能为null)
	 * "shipmentTime", Long型(可能为null)
	 * "expressCompany", String型(1:UPS；2:FedEx；3:US Postal Service)(可能为null)
	 * "trackingNo, String型(可能为null)
	 */
	private List<Map<String, Object>> itemList;

	public List<Map<String, Object>> getItemList() {
		return itemList;
	}

	public void setItemList(List<Map<String, Object>> itemList) {
		this.itemList = itemList;
	}
}
