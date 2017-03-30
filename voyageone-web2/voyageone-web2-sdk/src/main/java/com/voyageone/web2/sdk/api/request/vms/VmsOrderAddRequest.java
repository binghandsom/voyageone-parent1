package com.voyageone.web2.sdk.api.request.vms;

import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiRuleException;
import com.voyageone.web2.sdk.api.response.vms.VmsOrderAddResponse;
import com.voyageone.web2.sdk.api.util.RequestUtils;

import java.util.List;
import java.util.Map;

/**
 *
 * /vms/order/AddOrderInfo
 *
 * Created on 16/07/18.
 * @author jeff.duan
 * @version 1.0
 */
public class VmsOrderAddRequest extends VoApiRequest<VmsOrderAddResponse> {

	@Override
	public String getApiURLPath() {
		return "/vms/order/addOrderInfo";
	}


	/**
	 * 物品列表
	 * Map包含以下内容
	 * "channelId", String型
	 * "reservationId", String型
	 * "consolidationOrderId", String型
	 * "orderId", String型
	 * "clientSku", String型
	 * "barcode", String型
	 * "name", String型
	 * "consolidationOrderTime", Long型(时间戳)
	 * "orderTime", Long型(时间戳)
	 * "cartId", Integer型
	 * "clientMsrp", Double型
	 * "clientNetPrice", Double型
	 * "clientRetailPrice", Double型
	 * "retailPrice", Double型
	 */
	private List<Map<String, Object>> itemList;

	@Override
	public void requestCheck() throws ApiRuleException {
		if (itemList != null) {
			for (Map<String, Object> item : itemList) {
				RequestUtils.checkNotEmpty("channelId", item.get("channelId"));
				RequestUtils.checkNotEmpty("reservationId", item.get("reservationId"));
				RequestUtils.checkNotEmpty("consolidationOrderId", item.get("consolidationOrderId"));
				RequestUtils.checkNotEmpty("consolidationOrderTime", item.get("consolidationOrderTime"));
				RequestUtils.checkNotEmpty("orderId", item.get("orderId"));
				RequestUtils.checkNotEmpty("orderTime", item.get("orderTime"));
				RequestUtils.checkNotEmpty("cartId", item.get("cartId"));
				RequestUtils.checkNotEmpty("clientSku", item.get("clientSku"));
				RequestUtils.checkNotEmpty("name", item.get("name"));
				RequestUtils.checkNotEmpty("barcode", item.get("barcode"));
				RequestUtils.checkNotEmpty("cartId", item.get("cartId"));
				RequestUtils.checkNotEmpty("clientMsrp", item.get("clientMsrp"));
				RequestUtils.checkNotEmpty("clientNetPrice", item.get("clientNetPrice"));
				RequestUtils.checkNotEmpty("clientRetailPrice", item.get("clientRetailPrice"));
				RequestUtils.checkNotEmpty("retailPrice", item.get("retailPrice"));
			}
		}
	}

	public List<Map<String, Object>> getItemList() {
		return itemList;
	}

	public void setItemList(List<Map<String, Object>> itemList) {
		this.itemList = itemList;
	}
}