package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.FileItem;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by Jerry on 2015-09-29.
 */
@Component
public class TbWLBService extends TbBase {


    /**
     * 物流宝订单取消
     * @param shop 店铺信息
     * @param lgOrderCode 物流订单编号
     */
    public WlbImportsOrderCancelResponse cancelOrder(ShopBean shop, String lgOrderCode) throws ApiException {
		//获取淘宝API连接
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		WlbImportsOrderCancelRequest req = new WlbImportsOrderCancelRequest();
		req.setLgorderCode(lgOrderCode);
//    		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop, req);
    }

	/**
	 * 物流宝订单信息取得
	 * @param shop 店铺信息
	 * @param sourceOrderId 订单号
	 */
	public WlbImportsOrderGetResponse orderGet(ShopBean shop, String sourceOrderId) throws ApiException {
		//获取淘宝API连接
		//TaobaoClient client = getDefaultTaobaoClient(shop);

		WlbImportsOrderGetRequest req = new WlbImportsOrderGetRequest();
		req.setTradeId(Long.valueOf(sourceOrderId));
//    		response = client.execute(req , shop.getSessionKey());
		return reqTaobaoApi(shop,req);
	}
}
