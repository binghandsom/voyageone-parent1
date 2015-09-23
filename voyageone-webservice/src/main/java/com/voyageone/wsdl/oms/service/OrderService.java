package com.voyageone.wsdl.oms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.voyageone.wsdl.core.Constants;
import com.voyageone.wsdl.core.util.JsonUtil;
import com.voyageone.wsdl.core.util.StringUtils;
import com.voyageone.wsdl.oms.OmsConstants;
import com.voyageone.wsdl.oms.dao.OrderDao;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OrderService {
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 平台Id
	 */
	private static String PLAT_FORM_ID = "platformId";
	
	/**
	 * 新订单信息
	 */
	private static String NEW_ORDER_INFO = "newOrder";
	
	/**
	 * 状态变化订单信息
	 */
	private static String CHANGED_ORDER_INFO = "changedOrder";
	
	@Autowired
	private OrderDao orderDao;
	
	/**
	 * 新订单信息接收
	 * @param map
	 * @return
	 */
	public List<String> newOrderInfoImport(Map<String, Object> map) {
		// 数据来源标志
		String platformId = String.valueOf(map.get(PLAT_FORM_ID));
		
		// 聚石塔（阿里系）来源数据
		if (OmsConstants.ORDER_PLATFORM_ID_ALIBABA.equals(platformId)) {
			
			logger.info("阿里系新订单json信息接受开始");
			
			// 记录成功记录到服务端的json订单信息id，反馈给客户端不让再次推送
			List<String> successJsonIdList = new ArrayList<String>();
			
			// 新付款订单json信息
			String orderInfo = Constants.EMPTY_STR;
			List<Map<String, Object>> mapDataList = null;
			try {
				orderInfo = JsonUtil.getJsonString(map.get(NEW_ORDER_INFO));
				mapDataList = JsonUtil.jsonToMapList(orderInfo);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			
			if (mapDataList != null && mapDataList.size() > 0) {
				for (Map<String, Object> mapData : mapDataList) {
					if (mapData != null && mapData.size() > 0) {
						
						// 成功ID
						String successJsonId = "";
						try {
							// 插入成功
							if (orderDao.insertNewOrdersJsonInfo(mapData)) {
								successJsonId = String.valueOf(mapData.get("id"));
								logger.info("新付款json订单插入成功。id=" + successJsonId);
							}
							
						// 已存在记录算成功
						} catch (DuplicateKeyException e) {
							successJsonId = String.valueOf(mapData.get("id"));
							
							logger.info("新付款json订单插入时主键重复。id=" + successJsonId);
						} catch (Exception e) {
							logger.error(e.getMessage(), e);
							
							String failedJsonId = String.valueOf(mapData.get("id"));
							
							logger.info("新付款json订单插入时失败。id=" + failedJsonId);
						}
						
						// 记录成功信息
						if (!StringUtils.isNullOrBlank2(successJsonId)) {
							successJsonIdList.add(successJsonId);
						}
					}
				}
			}
			
			logger.info("阿里系新订单json信息接受结束，返回成功jsonId：" + successJsonIdList.toString());
			
			return successJsonIdList;
			
		} else {
			return new ArrayList<String>();
		}
	}
	
	/**
	 * 状态变化订单信息接收
	 * @param map
	 * @return
	 */
	public List<String> changedOrderInfoImport(Map<String, Object> map) {
		// 数据来源标志
		String platformId = String.valueOf(map.get(PLAT_FORM_ID));
		
		// 聚石塔（阿里系）来源数据
		if (OmsConstants.ORDER_PLATFORM_ID_ALIBABA.equals(platformId)) {
			
			logger.info("阿里系状态变化订单json信息接受开始");
			
			// 记录成功记录到服务端的json订单信息id，反馈给客户端不让再次推送
			List<String> successJsonIdList = new ArrayList<String>();
			
			// 状态变化订单json信息
			String orderInfo = Constants.EMPTY_STR;
			List<Map<String, Object>> mapDataList = null;
			try {
				orderInfo = JsonUtil.getJsonString(map.get(CHANGED_ORDER_INFO));
				mapDataList = JsonUtil.jsonToMapList(orderInfo);
			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
			}
			
			if (mapDataList != null && mapDataList.size() > 0) {
				for (Map<String, Object> mapData : mapDataList) {
					
					// 成功ID
					String successJsonId = "";
					try {
						// 插入成功
						if (orderDao.insertChangedOrdersJsonInfo(mapData)) {
							successJsonId = String.valueOf(mapData.get("id"));
							
							logger.info("状态变化json订单插入成功。id=" + successJsonId);
						}
						
					// 已存在记录算成功
					} catch (DuplicateKeyException e) {
						successJsonId = String.valueOf(mapData.get("id"));
						
						logger.info("状态变化json订单插入时主键重复。id=" + successJsonId);
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						
						String failedJsonId = String.valueOf(mapData.get("id"));
						
						logger.info("状态变化json订单插入时失败。id=" + failedJsonId);
					}
					
					// 记录成功信息
					if (!StringUtils.isNullOrBlank2(successJsonId)) {
						successJsonIdList.add(successJsonId);
					}
				}
			}
			
			logger.info("阿里系状态变化订单json信息接受结束，返回成功jsonId：" + successJsonIdList.toString());
			
			return successJsonIdList;
			
		} else {
			return new ArrayList<String>();
		}
	}
}
