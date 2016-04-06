package com.voyageone.common.components.tmall;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.api.domain.TradeRate;
import com.taobao.api.request.TraderatesGetRequest;
import com.taobao.api.response.TraderatesGetResponse;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.components.tmall.base.TbBase;
import com.voyageone.common.util.DateTimeUtil;

/**
 * 天猫评价API服务类
 * 
 * @author james
 *
 */
@Component
public class TbTraderatesService extends TbBase {


	/**
	 * 获取用户评价
	 * 
	 * @param startdate
	 *            开始日期
	 * @param enddate
	 *            结束日期
	 * @param shop
	 *            渠道
	 * @return List<TradeRate>
	 */
	public List<TradeRate> getTradeRateList(String startdate, String enddate, ShopBean shop) {

		Long pageNo = 1L;
		List<TradeRate> tradeRates = new ArrayList<TradeRate>();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			TraderatesGetRequest req = new TraderatesGetRequest();
			
			Date dateS = format.parse(DateTimeUtil.getGMTTimeFrom(startdate,8));
			req.setStartDate(dateS);
			Date dateE = format.parse(DateTimeUtil.getGMTTimeTo(enddate,8));
			req.setEndDate(dateE);
			
			
			req.setFields("tid,oid,role,nick,result,created,rated_nick,item_title,item_price,content,reply,num_iid");
			req.setRateType("get");
			req.setRole("buyer");
			req.setUseHasNext(true);

			while (true) {
				req.setPageNo(pageNo);
				TraderatesGetResponse response = reqTaobaoApi(shop, req);

				if (response != null && response.getErrorCode() == null) {
					if(response.getTradeRates() != null) {
						tradeRates.addAll(response.getTradeRates());
					}
					// 没有下一页的场合
					if (!response.getHasNext()) {
						return tradeRates;
						// 有下一页的场合
					} else {
						// 页数追加
						pageNo++;
					}
				} else {
					if(response != null) {
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
