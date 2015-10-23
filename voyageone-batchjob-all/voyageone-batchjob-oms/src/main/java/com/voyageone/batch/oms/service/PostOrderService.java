package com.voyageone.batch.oms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.voyageone.common.Constants;
import com.voyageone.common.util.JsonUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.batch.oms.dao.PostOrderDao;
import com.voyageone.batch.oms.utils.CommonUtil;

@Service
public class PostOrderService {

	private static Log logger = LogFactory.getLog(PostOrderService.class);

	/**
	 * timeout
	 */
	private static final int WEBSERVICE_TIMEOUT = 10000;

	@Autowired
	PostOrderDao postOrderDao;

	/**
	 * 获取没有推送的订单
	 * @param channel_id
	 * @return
	 */
	public List<Map<String, Object>> getOrdersToOneShop(String channel_id){
		return (List) postOrderDao.selectList(Constants.DAO_NAME_SPACE_OMS + "get_orders_to_oneShop", channel_id);

	}

	public List<Map<String, Object>> getOrdersToChanneladvisor(String channel_id){
		return (List) postOrderDao.selectList(Constants.DAO_NAME_SPACE_OMS + "get_orders_to_channeladvisor", channel_id);

	}

	public List<Map<String, Object>> getApproveOrdersToChanneladvisor(String channel_id){
		return (List) postOrderDao.selectList(Constants.DAO_NAME_SPACE_OMS + "get_approve_orders_to_channeladvisor", channel_id);

	}

	public int updateOrderSendflg(String orderNumber){
		return postOrderDao.updateTable(Constants.DAO_NAME_SPACE_OMS + "update_order_sendflg", orderNumber);

	}
	/**
	 * 获取order_details的数据
	 * @param OrderNumber
	 * @return
	 */
	public List<Map<String, Object>> getOrderItems(String OrderNumber) {

		return (List) postOrderDao.selectList(Constants.DAO_NAME_SPACE_OMS + "get_order_details_by_ordernumber", OrderNumber);


	}

	/**
	 * 调用238服务器的webservice获取upc和sizeid
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getReservationInfo(String url) throws Exception {

		String ret="";
		String response=CommonUtil.HttpPost("", null, url);
		return JsonUtil.jsonToMapList(response);

	}

	/**
	 * 把JCOrderNumber更新到数据库中
	 * @param jCOrderNumber
	 * @param orderNumber
	 * @return
	 */
	public int updateOrder(String jCOrderNumber, String orderNumber,String task_name) {
		Map<String,Object> parameter=new HashMap<String, Object>();
		parameter.put("jCOrderNumber", jCOrderNumber);
		parameter.put("orderNumber", orderNumber);
		parameter.put("modifier", task_name);
		return postOrderDao.updateTable(Constants.DAO_NAME_SPACE_OMS + "update_jc_order_number",parameter);

	}


}
