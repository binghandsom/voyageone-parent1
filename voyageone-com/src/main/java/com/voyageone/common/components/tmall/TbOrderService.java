package com.voyageone.common.components.tmall;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.api.domain.Trade;
import com.taobao.api.request.TradesSoldGetRequest;
import com.taobao.api.response.TradesSoldGetResponse;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;

/**
 * 天猫订单API服务类
 * 
 * @author james
 *
 */
@Component
public class TbOrderService extends TbBase {

	// 淘宝交易类型
	public static final String TRADE_TYPE_TG = "tmall_i18n";

	/**
	 * 获取天猫的订单
	 * 
	 * @param startdate
	 *            开始日期
	 * @param enddate
	 *            结束日期
	 * @param shop
	 *            渠道
	 */
	public List<Trade> getOrderPage(String startdate, String enddate, ShopBean shop) {

		Long pageNo = 1L;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Trade> trades = new ArrayList<Trade>();
		try {
			// 获取淘宝API连接
			TradesSoldGetRequest req = new TradesSoldGetRequest();
//			startdate = startdate + " 00:00:00";
			Date dateS = format.parse(startdate);
//			Date dateS = format.parse(DateTimeUtil.getGMTTimeFrom(startdate,8));
			req.setStartCreated(dateS);
//			enddate = enddate + " 23:59:59";
			
			Date dateE = format.parse(enddate);
//			Date dateE = format.parse(DateTimeUtil.getGMTTimeTo(enddate,8));
			req.setEndCreated(dateE);
			req.setFields("tid,status,buyer_nick,receiver_name,receiver_state,receiver_city,receiver_district,receiver_address,"
					+ "receiver_zip,receiver_mobile,receiver_phone,buyer_email,buyer_message,created,pay_time,alipay_no,"
					+ "orders.num,orders.num_iid,orders.price,orders.title,orders.outer_iid,orders.outer_sku_id,orders.refund_status");
			req.setUseHasNext(true);
			if ("23".equalsIgnoreCase(shop.getCart_id())) {
				// 天猫国际的场合，比较特殊要专门设置交易类型
				req.setType(TRADE_TYPE_TG);
			}

			while (true) {
				req.setPageNo(pageNo);
				TradesSoldGetResponse response = reqTaobaoApi(shop, req);

				if (response != null && response.getErrorCode() == null) {
					if (response.getTrades() != null) {
						for (Trade trade : response.getTrades()) {
							Date payTime = trade.getPayTime();
							if (payTime == null) {
								continue;
							}
							trades.add(trade);
						}
					}
					// 没有下一页的场合
					if (!response.getHasNext()) {
						return trades;
						// 有下一页的场合
					} else {
						// 页数追加
						pageNo++;
					}
				} else {
					if(response != null)
					{
						logger.error("调用API失败:" + response.getSubCode() + ":" + response.getSubMsg());
					}else{
						logger.error("调用API超时");
					}
					return null;
				}
			}

		} catch (Exception e) {
			// 异常发生
			setErrorLog(e);
			return null;
		}
	}
}
