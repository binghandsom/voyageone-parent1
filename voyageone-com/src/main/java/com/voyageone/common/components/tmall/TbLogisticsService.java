package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.LogisticsOrdersGetRequest;
import com.taobao.api.request.WlbImportsOrderCancelRequest;
import com.taobao.api.request.WlbImportsOrderGetRequest;
import com.taobao.api.response.LogisticsOrdersGetResponse;
import com.taobao.api.response.WlbImportsOrderCancelResponse;
import com.taobao.api.response.WlbImportsOrderGetResponse;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by Jerry on 2016-01-11.
 */
@Component
public class TbLogisticsService extends TbBase {

	/**
	 * 批量查询物流订单
	 * @param shop 店铺信息
	 */
	public LogisticsOrdersGetResponse getLogisticsOrders(ShopBean shop, LogisticsOrdersGetRequest req) throws ApiException {

		LogisticsOrdersGetResponse response = null;

		//获取淘宝API连接
		TaobaoClient client = getDefaultTaobaoClient(shop);

//		req.setFields("tid,out_sid,company_name");
		response = reqTaobaoApi(shop,req);

		return response;
	}
}
