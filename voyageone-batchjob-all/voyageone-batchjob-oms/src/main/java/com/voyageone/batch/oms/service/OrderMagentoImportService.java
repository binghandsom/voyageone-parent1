package com.voyageone.batch.oms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.modelbean.TaobaoTradeBean;

@Service
public class OrderMagentoImportService {
	private static Log logger = LogFactory.getLog(OrderMagentoImportService.class);
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	OrderInfoImportService orderInfoImportService;
	/**
	 * 
	 * @param orderInfoTotalList
	 * @param taskName
	 * @return
	 */
	public boolean importNewOrderToHistory(List<TaobaoTradeBean> orderInfoTotalList,  String taskName){
		
		String sqlBatch=orderInfoImportService.getBatchNewOrderHistorySqlData(orderInfoTotalList,taskName);
		return orderDao.insertNewOrdersInfo(sqlBatch, orderInfoTotalList.size());
		
	}
	
	/**
	 * 根据tid cart_id channel_id 从临时表中检索
	 * @param tid
	 * @param cart_id
	 * @param order_channel_id
	 * @return
	 */
	public List<Map<String, Object>> getOrderInfoByTid(String tid,String cart_id,String order_channel_id){
		
		Map<String,String>dataMap = new HashMap<String, String>();
		dataMap.put("tid", tid);
		dataMap.put("cart_id", cart_id);
		dataMap.put("order_channel_id", order_channel_id);
		return orderDao.getOrderInfoByTid(dataMap);
		
	}
}
