package com.voyageone.batch.oms.job;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.dao.TaskDao;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.SynShipContants;
import com.voyageone.batch.oms.dao.OrderDao;
import com.voyageone.batch.oms.modelbean.Order;
import com.voyageone.batch.oms.modelbean.OrderDetails;
import com.voyageone.batch.oms.modelbean.PostData;
import com.voyageone.batch.oms.service.OrderInfoImportService;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;

public class SyncSynShipNewOrderJob {
	
	private static Log logger = LogFactory.getLog(SyncSynShipNewOrderJob.class);
	
	@Autowired
	TaskDao taskDao;
	
	@Autowired
	OrderDao orderDao;
	
	@Autowired
	OrderInfoImportService orderInfoImportService;
	
	@Autowired
	IssueLog issueLog;
	
	public final static String taskCheck = "SyncSynShipNewOrder";
	
	/**
	 * 将进入OMS且有reservationId的订单推送到SynShip系统
	 */
	public void run() {
		
		List<TaskControlBean> taskControlList = taskDao.getTaskControlList(taskCheck);
		// 是否可以运行的判断
		if (TaskControlUtils.isRunnable(taskControlList, taskCheck) == false) {
			return;
		}
		String taskID =  TaskControlUtils.getTaskId(taskControlList);
		logger.info(taskCheck + "任务开始");
		// 任务监控历史记录添加:启动
		taskDao.insertTaskHistory(taskID, TaskControlEnums.Status.START.getIs());
		
		boolean isSuccess = true;
		
		// 从OMS取出需要同步到synship的订单记录
		List<Map<String, Object>> newSynShipOrderList = orderInfoImportService.getSynShipNewOrderInfo();
		if (newSynShipOrderList != null && newSynShipOrderList.size() > 0) {
			
			int listSize = newSynShipOrderList.size();
			
			// 取满100条时要去掉最后一条订单的所有detail记录，防止这轮没取全
			if (listSize == OmsConstants.DATACOUNT_100) {
				Map<String, Object> mapOrderLast = newSynShipOrderList.get(listSize - 1);
				String orderNumberLast = StringUtils.null2Space2(String.valueOf(mapOrderLast.get("orderNumber")));
				
				int lastNumber = 0;
				for (int i = 0; i < listSize; i++) {
					Map<String, Object> mapOrder = newSynShipOrderList.get(i);
					String orderNumber = StringUtils.null2Space2(String.valueOf(mapOrder.get("orderNumber")));
					if (orderNumber.equalsIgnoreCase(orderNumberLast)) {
						lastNumber++;
					}
				}
				
				while (lastNumber > 0) {
					newSynShipOrderList.remove(newSynShipOrderList.size() - 1);
					lastNumber--;
				}
			}
			
			// 实际要推送的订单详细条数
			int realSize = newSynShipOrderList.size();
			if (realSize > 0) {
				try {
					postOmsOrders2SynShip(newSynShipOrderList);
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
					
					issueLog.log(ex, ErrorType.BatchJob, SubSystem.OMS);
				}
			} else {
				logger.info("去掉最后一条订单的所有detail记录后没有要推送的，请检查！");
				
				issueLog.log("OrderInfoImportService.importNewOrderFromJsonToHistory", 
						"同步synship去掉最后一条订单的所有detail记录后没有要推送的，请检查！", 
						ErrorType.BatchJob, SubSystem.OMS);
			}
			
		} else {
			logger.info("没有要同步到SynShip的新订单信息");
		}
		
		// 任务监控历史记录添加:结束
		String result = "";
		if (isSuccess) {
			result = TaskControlEnums.Status.SUCCESS.getIs();
		} else {
			result = TaskControlEnums.Status.ERROR.getIs();
		}
		taskDao.insertTaskHistory(taskID, result);

		logger.info(taskCheck + "任务结束");
	}
	
	/**
	 * 推送订单信息到synship
	 * 
	 * @param newSynShipOrderList
	 */
	private void postOmsOrders2SynShip(List<Map<String, Object>> newSynShipOrderList) throws Exception {
		// 上条记录订单号
		String orderNum = "";
		// 当前记录订单号
		String orderNumber = "";
		List<Order> listOrder = new ArrayList<Order>();
		
		int size = newSynShipOrderList.size();
		
		logger.info("postOmsOrders2SynShip 总处理行数：" + newSynShipOrderList.size());
		
		for (int i = 0; i < size; i++) {
			Map<String, Object> orderInfoMap = newSynShipOrderList.get(i);
			// 当前记录订单号
			orderNumber = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderNumber")));
			
			if (orderNum.equals(orderNumber) && i > 0) {
				List<OrderDetails> listDetail = listOrder.get(listOrder.size() - 1).getOrderDetails();
				
				OrderDetails od = new OrderDetails();
				od.setItemNumber(String.valueOf(listDetail.size() + 1));
				od.setStatus(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("status"))));
				od.setReservationId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("resId"))));
				od.setReservationStatus("DataNotReady");
				od.setShipChannel("");
				od.setPieces(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("quantityOrdered"))));
				od.setPriceUnit(SynShipContants.PRICE_USD);
				od.setSalePriceUnit(SynShipContants.PRICE_USD);
				od.setOriginalPriceUnit(SynShipContants.PRICE_USD);
				od.setDescriptionInner(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("product"))));
				od.setSku(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("sku"))));
				od.setBrand("");
				od.setPrice(new BigDecimal("0.00"));
				od.setSalePrice(new BigDecimal("0.00"));
				od.setOriginalPrice(new BigDecimal("0.00"));
				od.setWeightLb("0");
				od.setWeightKg("0");
				
				listDetail.add(od);
				
			} else {
				Order o = new Order();
				// 订单编号
				o.setOrderNumber(orderNumber);
				// 仓库ID
				o.setWarehouseId("00001");
				// 渠道页面订单编号
				o.setSourceOrderId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("sourceOrderId"))));
				// 订单状态（初始：订单数据准备中）
				o.setOrderStatus(SynShipContants.ORDER_STATUS_DATA_IN_PROCESSING);
				// 订单渠道来源
				o.setOrderChannelId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderChannelId"))));
				
				// 发货渠道
				o.setShipChannel("");
				// 收件人姓名
				o.setShipName(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipName"))).trim());
				// 收件人地址
				String ShipAddress = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipAddress")));
				String ShipAddress2 = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipAddress2")));
				o.setShipAddress(ShipAddress + ShipAddress2);
				// 收件人区域
				o.setShipDistrict(ShipAddress);
				// 收件人城市
				o.setShipCity(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipCity"))));
				// 收件人省份
				o.setShipState(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipState"))));
				// 收件人邮编
				o.setShipZip(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipZip"))));
				// 收件人国家
				o.setShipCountry(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipCountry"))));
				// 收件人电话
				o.setShipPhone(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipPhone"))).trim());
				// 快递公司
				o.setShipping(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("shipping"))));
				// 总价
				o.setPriceTotal(new BigDecimal("0.00"));
				o.setSalePriceTotal(new BigDecimal("0.00"));
				o.setOriginalPriceTotal(new BigDecimal("0.00"));
				o.setShippedWeight("0");
				o.setActualShippedWeightKg("0");
				o.setActualShippedWeightLb("0");
				// 锁定状态
				String lockShip = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("lockShip")));
				String isLocked = "0";
				if ("YES".equalsIgnoreCase(lockShip)) {
					isLocked = "1";
				}
				o.setIsLocked(isLocked);
				// 币别
				o.setPriceUnit(SynShipContants.PRICE_USD);
				o.setSalePriceUnit(SynShipContants.PRICE_USD);
				o.setOriginalPriceUnit(SynShipContants.PRICE_USD);
				// 备注
				o.setComments(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("customerComments"))));
				// 标签打印状态
				o.setLabelStatus(SynShipContants.LABEL_STATUS_NOT_PRINTED);
				// 业务类型
				o.setExpressType("");
				// 下单日期时间
				String orderDateTime = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderDateTime")));
				if (orderDateTime.length() > 19) {
					orderDateTime = orderDateTime.substring(0, 19);
				}
				o.setOrderDateTime(orderDateTime);
				// 渠道ID
				o.setCartID(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("cartId"))));
				// 品牌
				o.setBrand("");
				// JuicyCouture订单号
				//o.setRefOrderNumber(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("clientOrderId"))));
				o.setRefOrderNumber("0");
				// 支付宝交易号
				o.setAlipayTransNo(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("payNo"))));
				// 支付宝账号
				o.setAlipayAccount(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("payAccount"))));
				// 旺旺ID
				o.setWangwangId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("wwId"))));
				// SE订单状态
				o.setSeorderStatus(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderStatus"))));
				
				// 客服、客户备注
				o.setOrderInstructions(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderInst"))));
				o.setCustomerComments(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("customerComments"))));
				o.setGiftMessage(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("internalMessage"))));
				o.setNoteToCustomer(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("noteToCustomer"))));
				
				// 到付
				String freightCollect = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("freightCollect")));
				o.setFreightCollect(freightCollect);
				
				// 退换货订单
				String orderKind = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("orderKind")));
				// 拆分
				String orderTypeSplit = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_SPLIT, com.voyageone.common.Constants.LANGUAGE.EN);
				// 换货
				String orderTypeReturn = Type.getValue(MastType.orderType.getId(), Constants.ORDER_TYPE_EXCHANGE, com.voyageone.common.Constants.LANGUAGE.EN);
				if (orderKind.equals(orderTypeSplit) || orderKind.equals(orderTypeReturn)) {
					o.setExchangedWebId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("sourceOrderId"))));
					o.setIsExchanged("1");
				} else {
					o.setExchangedWebId("");
					o.setIsExchanged("0");
				}
				
				// 订单金额
				String GrandTotal = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("grandTotal")));
				if (StringUtils.isNullOrBlank2(GrandTotal)) {
					GrandTotal = "0.00";
				}
				o.setGrandTotal(new BigDecimal(GrandTotal));
				String FinalGrandTotal = StringUtils.null2Space2(String.valueOf(orderInfoMap.get("finalGrandTotal")));
				if (StringUtils.isNullOrBlank2(FinalGrandTotal)) {
					FinalGrandTotal = "0.00";
				}
				o.setFinalGrandTotal(new BigDecimal(FinalGrandTotal));
				
				OrderDetails od = new OrderDetails();
				od.setItemNumber("1");
				od.setStatus(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("status"))));
				od.setReservationId(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("resId"))));
				od.setReservationStatus("DataNotReady");
				od.setShipChannel("");
				od.setPieces(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("quantityOrdered"))));
				od.setPriceUnit(SynShipContants.PRICE_USD);
				od.setSalePriceUnit(SynShipContants.PRICE_USD);
				od.setOriginalPriceUnit(SynShipContants.PRICE_USD);
				od.setDescriptionInner(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("product"))));
				od.setSku(StringUtils.null2Space2(String.valueOf(orderInfoMap.get("sku"))));
				od.setBrand("");
				od.setPrice(new BigDecimal("0.00"));
				od.setSalePrice(new BigDecimal("0.00"));
				od.setOriginalPrice(new BigDecimal("0.00"));
				od.setWeightLb("0");
				od.setWeightKg("0");
			
				List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
				orderDetails.add(od);
				o.setOrderDetails(orderDetails);
				
				listOrder.add(o);
			}
			
			// 上条记录订单号
			orderNum = orderNumber;
		}
		
		if (listOrder != null && listOrder.size() > 0) {
			
			logger.info("postShNewOrders 转换后的Order数：" + listOrder.size());
			
			for (Order order : listOrder) {
				List<OrderDetails> orderDetailList = order.getOrderDetails();
				// 物品名称
				StringBuilder descriptionInnerSb = new StringBuilder();
				// sku
				StringBuilder skuSb = new StringBuilder();
				
				int productNum = 0;
				
				for (int i = 0; i < orderDetailList.size(); i++) {
					OrderDetails orderDetail = orderDetailList.get(i);
					String descriptionInner = orderDetail.getDescriptionInner();
					String sku = orderDetail.getSku();
					
					int detailProductNum = Integer.valueOf(orderDetail.getPieces());
					productNum += detailProductNum;
					
					if (i == 0) {
						if (descriptionInner != null && !"".equals(descriptionInner)) {
							descriptionInnerSb.append(descriptionInner);
						}
						if (sku != null && !"".equals(sku)) {
							skuSb.append(sku);
						}
					} else {
						if (descriptionInner != null && !"".equals(descriptionInner)) {
							if (!descriptionInnerSb.toString().contains(descriptionInner)) {
								descriptionInnerSb.append(Constants.BLANK_STR);
								descriptionInnerSb.append(descriptionInner);
							}
						}
						if (sku != null && !"".equals(sku)) {
							if (!skuSb.toString().contains(sku)) {
								skuSb.append(Constants.BLANK_STR);
								skuSb.append(sku);
							}
						}
					}
				}
				order.setDescriptionInner(descriptionInnerSb.toString());
				order.setSku(skuSb.toString());
//					order.setProductNum(String.valueOf(orderDetailList.size()));
				order.setProductNum(String.valueOf(productNum));
			}
			
			PostData postData = new PostData();
			// 临时过渡用
			postData.setChannelId(Properties.readValue("SYNC_SYNSHIP_CHANNEL_ID"));
			postData.setScrectKey(Properties.readValue("SYNC_SYNSHIP_SCRECT_KEY"));
			postData.setSessionKey(Properties.readValue("SYNC_SYNSHIP_SESSIO_NKEY"));
			postData.setOrderDatas(listOrder);
			
			String strParam = JsonUtil.getJsonString(postData);
			logger.info("call addNewOrdersFromSh webservice is start at:" + DateTimeUtil.getNow());
			logger.info("param is:" + strParam);
			logger.info("order size:" + listOrder.size());
			URI u = new URI(Properties.readValue("SHIP_WEBSERVICE_URL_NEWORDERS_SH"));
			ClientConfig cc = new DefaultClientConfig();
			Client client = Client.create(cc);
			client.setConnectTimeout(2000);
			client.setReadTimeout(60000);
			WebResource resource = client.resource(u);
			String strResponse = resource.post(String.class, strParam);
			logger.info("call addNewOrdersFromSh webservice is end at:" + DateTimeUtil.getNow());
			
			// 处理返回数据
			JSONObject paraJson = new JSONObject(strResponse);
			if (SynShipContants.RESULT_OK.equals(paraJson.get("result").toString())) {
				logger.info("订单全部推送成功");
				
			} else {
				if (!StringUtils.isNullOrBlank2(String.valueOf(paraJson.get("errorInfo")))) {
					JSONObject errorInfo = paraJson.getJSONObject("errorInfo");
					if (errorInfo != null) {
						logger.info(errorInfo.get("errorMessageCn").toString());
						
						issueLog.log("SyncSynShipNewOrderJob.postOmsOrders2SynShip", 
								errorInfo.get("errorMessageCn").toString(), 
								ErrorType.BatchJob, SubSystem.OMS);
					}
					listOrder.clear();
				} else {
					if (!StringUtils.isNullOrBlank2(String.valueOf(paraJson.get("errorOrderList")))) {
						JSONArray errorOrderList = paraJson.getJSONArray("errorOrderList");
						if (errorOrderList != null && errorOrderList.length() > 0) {
							int errorSize = errorOrderList.length();
							logger.info("有订单推送失败,失败条数：" + errorSize);
							issueLog.log("SyncSynShipNewOrderJob.postOmsOrders2SynShip", 
									"有订单推送失败,失败条数：" + errorSize, 
									ErrorType.BatchJob, SubSystem.OMS);
							
							for (int i = 0; i < errorSize; i++) {
								JSONObject obj = errorOrderList.getJSONObject(i);
								String orderNumberFailure = obj.get("key").toString();
								logger.info("失败订单号：" + orderNumberFailure);
								logger.info("失败原因：" + obj.get("errorMessageCn").toString());
								
								issueLog.log("SyncSynShipNewOrderJob.postOmsOrders2SynShip", 
										"失败订单号：" + orderNumberFailure + " 失败原因：" + obj.get("errorMessageCn").toString(), 
										ErrorType.BatchJob, SubSystem.OMS);
								
								for (Order order : listOrder) {
									if (order.getOrderNumber().equals(orderNumberFailure)) {
										listOrder.remove(order);
										break;
									}
								}
							}
						}
					}
				}
			}
			
			// 推送成功数据处理
			for (int i = 0; i < listOrder.size(); i++) {
				Order order = listOrder.get(i);
				String orderNumberSuccess = order.getOrderNumber();
				List<OrderDetails> detailList = order.getOrderDetails();
				// synship_flg-->1
				for (OrderDetails detail : detailList) {
					String reservationId = detail.getReservationId();
					
					Map<String, String> dataMap = new HashMap<String, String>();
					dataMap.put("orderNumber", orderNumberSuccess);
					dataMap.put("reservationId", reservationId);
					dataMap.put("taskName", taskCheck);
					int updateSize = orderDao.resetSynShipFlag(dataMap);
					if (updateSize > 0) {
						logger.info("Orders synship_flg --> 1 update success! orderNumber is:" + orderNumberSuccess + " reservationId is:" + reservationId);
					} else {
						logger.info("Orders synship_flg --> 1 update failure! orderNumber is:" + orderNumberSuccess + " reservationId is:" + reservationId);
						
						issueLog.log("SyncSynShipNewOrderJob.postOmsOrders2SynShip", 
								"Orders synship_flg --> 1 update failure! orderNumber is:" + orderNumberSuccess + " reservationId is:" + reservationId, 
								ErrorType.BatchJob, SubSystem.OMS);
					}
				}
				
			}
		}
	}
}

