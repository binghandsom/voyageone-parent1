package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Constants;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.dao.OrderDao;
import com.voyageone.task2.cms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.task2.cms.formbean.OutFormOrderdetailOrders;
import com.voyageone.common.components.gilt.GiltOrderService;
import com.voyageone.common.components.gilt.bean.*;
import com.voyageone.common.components.gilt.exceptions.GiltException;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OmsPostGiltNormalOrderService extends BaseTaskService {
	
	@Autowired
	private OrderDao orderDao;

	@Autowired
	private GiltOrderService giltOrderService;
	
	@Autowired
	IssueLog issueLog;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	// taskName
	// 正常订单推送（placed）ext_flg4为0
	private final static String POST_GILT_NOWMFAL_ORDER = "OmsPostGiltNormalOrderJob";
	// 正常订单推送（confirmed）send_flg为0
	private final static String POST_GILT_CONFIRMED_ORDER = "OmsPostGiltConfOrderJob";
	// 取消订单推送 ext_flg2为0
	private final static String POST_GILT_CANCEL_ORDER = "OmsPostGiltCancelOrderJob";

	// Gilt渠道ID
	private String orderChannelID = "015";

	@Override
	public SubSystem getSubSystem() {
		return SubSystem.CMS;
	}

	@Override
	public String getTaskName() {
		return "OmsPostGiltNormalOrderJob";
	}

	@Override
	protected void onStartup(List<TaskControlBean> taskControlList) throws Exception {
		// 允许运行的订单渠道取得
		List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

		// 线程
		List<Runnable> threads = new ArrayList<>();

		// 根据订单渠道运行
		for (final String orderChannelID : orderChannelIdList) {

			threads.add(new Runnable() {
				@Override
				public void run() {
					postGiltNormalOrder(orderChannelID);
				}
			});

		}

		runWithThreadPool(threads, taskControlList);
	}


	/**
	 * 正常订单推送（placed）ClientOrderID为空
	 *
	 */
	public boolean postGiltNormalOrder(String orderChannelIDParam) {
		boolean isSuccess = true;

		if (!orderChannelID.equals(orderChannelIDParam)) {
			return true;
		}

		// 待推送订单信息取得
		List<OutFormOrderdetailOrders> pushOrderList = getPushOrderList(orderChannelID);

		// 有数据的场合
		if (pushOrderList.size() > 0) {

			for (OutFormOrderdetailOrders orderInfo : pushOrderList) {
				// 向Gilt推送（程序异常时处理结束）
				isSuccess = postGiltNormalOrderSub(orderInfo);
				if (!isSuccess) {
					break;
				}
			}
		} else {
			$info("postGiltNormalOrder push order rec = 0");
		}

		return isSuccess;
	}

	/**
	 * 正常订单向Gilt推送(placed)
	 *
	 */
	private boolean postGiltNormalOrderSub(OutFormOrderdetailOrders orderInfo) {
		// 异常判定
		boolean ret = true;
		// 处理判定
		boolean isSuccess = true;

		// UUID 取得
		String uuidStr = getUUID(orderInfo);
		if (StringUtils.isEmpty(uuidStr)) {
			isSuccess = false;
			issueLog.log("postGiltNormalOrder.postGiltNormalOrderSub",
					"postGiltNormalOrder uuid get error;Push order number = " + orderInfo.getOrderNumber(),
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		if (isSuccess) {
			try {
				// Gilt 推送
				List<Object> retArr = postGiltNormalOrderByService(uuidStr, orderInfo);

				// 推送标志更新
//				List<String> orderNumberList = new ArrayList<String>();
//				orderNumberList.add(orderInfo.getOrderNumber());
//				isSuccess = orderDao.updateOrderExtFlg4(POST_GILT_NOWMFAL_ORDER, orderNumberList, (String)retArr.get(1));
				isSuccess = updateOrdersInfo(retArr, orderInfo);
				if (!isSuccess) {
					// DB 更新失败
					ret = false;
					$info("postGiltNormalOrder ext_flg4(placed) update error;Push order number = " + orderInfo.getOrderNumber());
					issueLog.log("postGiltNormalOrder.postGiltNormalOrderSub",
							"postGiltNormalOrder ext_flg4(placed) update error;Push order number = " + orderInfo.getOrderNumber(),
							ErrorType.BatchJob,
							SubSystem.OMS);
				}

			} catch (NumberFormatException e) {
				ret = false;
				$error("postGiltNormalOrderSub", e);
				issueLog.log("postGiltNormalOrder.postGiltNormalOrderSub",
						"postGiltNormalOrder sku error;Push order number = " + orderInfo.getOrderNumber(),

						ErrorType.BatchJob,
						SubSystem.OMS);

			} catch (Exception e) {
				ret = false;
				$error("postGiltNormalOrderSub", e);
				issueLog.log("postGiltNormalOrder.postGiltNormalOrderSub",
						"postGiltNormalOrder error;Push order number = " + orderInfo.getOrderNumber(),
						ErrorType.BatchJob,
						SubSystem.OMS);
			}
		}

		return ret;
	}

	/**
	 * 正常订单向Gilt推送（调用Service 接口）
	 *
	 * @return List[0] 执行结果
	 * 			List[1] 错误返回
	 *
	 */
	private List<Object> postGiltNormalOrderByService(String uuidStr, OutFormOrderdetailOrders orderInfo) throws Exception {
		List<Object> retArr = new ArrayList<>();
		boolean isSuccess = true;
		String errorContent = "";
		try {
			// Gilt 推送
			GiltPutOrderRequest request = new GiltPutOrderRequest();
			// 		ID
			request.setId(UUID.fromString(uuidStr));

			// 		Items
			List<GiltOrderItem> orderItems = new ArrayList<>();
			List<OutFormOrderDetailOrderDetail> orderDetailsList = orderInfo.getOrderDetailsList();
			for (OutFormOrderDetailOrderDetail orderDetailInfo : orderDetailsList) {
				// 物品的场合
				if (!orderDetailInfo.isAdjustment()) {
					if (StringUtils.isEmpty(orderDetailInfo.getClientSku())) {
						// 异常SKU
						isSuccess = false;
						errorContent = "Has empty clientSku Sku = " + orderDetailInfo.getSku();
						break;
					} else {
						GiltOrderItem orderItem = new GiltOrderItem();
						orderItem.setSku_id(Long.valueOf(orderDetailInfo.getClientSku()));
						orderItem.setQuantity(Long.valueOf(orderDetailInfo.getQuantityOrdered()));
						orderItems.add(orderItem);
					}
				}
			}
			request.setOrder_items(orderItems);

			if (isSuccess) {
				// 正常SKU的场合
				if (orderItems.size() > 0) {
					GiltOrder orders = giltOrderService.putOrder(request);
					$info("postGiltNormalOrder Success ; Push order uuid = " + orders.getId());
				} else {
					$info("postGiltNormalOrder order has no items ; Push order number = " + orderInfo.getOrderNumber());
					issueLog.log("postGiltNormalOrder.postGiltNormalOrderByService",
							"postGiltNormalOrder order has no items ; Push order number = " + orderInfo.getOrderNumber(),
							ErrorType.BatchJob,
							SubSystem.OMS);
					// 没有物品的场合
					errorContent = "Has no items";
				}
			} else {
				// 有异常SKU的场合
				$info("postGiltNormalOrder order has error sku ; Push order number = " + orderInfo.getOrderNumber());
				issueLog.log("postGiltNormalOrder.postGiltNormalOrderByService",
						"postGiltNormalOrder order has error sku ; Push order number = " + orderInfo.getOrderNumber(),
						ErrorType.BatchJob,
						SubSystem.OMS);
			}
		} catch (GiltException e) {
			// TODO 通知客服 超卖 其他订单处理继续
			$info("postGiltNormalOrder order Gilt error " + e.getMessage() + " ; Push order number = " + orderInfo.getOrderNumber());
			// Gilt 返回异常信息
			errorContent = e.getMessage();
		}

		retArr.add(true);
		retArr.add(errorContent);

		return retArr;
	}

	/**
	 * UUID取得
	 *
	 */
	private String getUUID(OutFormOrderdetailOrders orderInfo) {
		// DB中，ClientOrderId 取得
		String uuidStr = orderInfo.getClientOrderId();

		if (StringUtils.isEmpty(uuidStr)) {
			UUID uuidObj = UUID.randomUUID();
			uuidStr = uuidObj.toString();

			boolean ret = orderDao.updateOrdersClientOrderIdInfo(orderInfo.getOrderNumber(), uuidStr, POST_GILT_NOWMFAL_ORDER);
			if (!ret) {
				uuidStr = "";
			}
		}

		return uuidStr;
	}

	/**
	 * 推送订单数据抽取
	 *
	 */
	private List<OutFormOrderdetailOrders> getPushOrderList(String orderChannelId) {
		// 订单信息取得
		List<OutFormOrderdetailOrders> ordersList = orderDao.getOrdersListByOrderChannelIdForNotSend(orderChannelId);
		if (ordersList.size() == 0) {
			return ordersList;
		}

		// 订单详情取得
		List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList = orderDao.getOrderDetailsInfo(orderChannelId, getOrderNumberList(ordersList));

		// 订单详细再设定（绑定折扣）
		for (OutFormOrderdetailOrders orderInfo : ordersList) {
			//	订单明细信息取得
			List<OutFormOrderDetailOrderDetail> orderDetailsList = getOrderDetailsList(orderDetailsListForOrdersList, orderInfo.getOrderNumber());
			//	订单明细设定
			orderInfo.setOrderDetailsList(orderDetailsList);
		}

		return ordersList;
	}

	private List<String> getOrderNumberList(List<OutFormOrderdetailOrders> ordersList) {
		ArrayList<String> orderNumberList = new ArrayList<>();

		for (OutFormOrderdetailOrders anOrdersList : ordersList) {
			orderNumberList.add(anOrdersList.getOrderNumber());
		}

		return orderNumberList;
	}

	/**
	 * 获得订单明细信息，根据订单号
	 *
	 * @param orderDetailsListForOrdersList 根据原订单号获得的一组订单明细信息
	 * @param orderNumber 订单号
	 *
	 * @return List<OutFormOrderDetailOrderDetail>
	 */
	private List<OutFormOrderDetailOrderDetail> getOrderDetailsList(List<OutFormOrderDetailOrderDetail> orderDetailsListForOrdersList, String orderNumber) {
		List<OutFormOrderDetailOrderDetail> orderDetailsList = new ArrayList<>();

		for (OutFormOrderDetailOrderDetail orderDetailsInfo : orderDetailsListForOrdersList) {
			if (orderNumber.equals(orderDetailsInfo.getOrderNumber())) {
				orderDetailsList.add(orderDetailsInfo);
			}
		}

		return orderDetailsList;
	}

	/**
	 * 本地数据更新
	 *
	 */
	private boolean updateOrdersInfo(List<Object> giltRet, OutFormOrderdetailOrders orderInfo) {
		boolean isSuccess = true;

		TransactionStatus status=transactionManager.getTransaction(def);
		try {

			List<String> orderNumberList = new ArrayList<>();
			orderNumberList.add(orderInfo.getOrderNumber());

			String retContent = "";
			// 正常订单推送
			if (POST_GILT_NOWMFAL_ORDER.equals(getTaskName())) {
				retContent = (String)giltRet.get(1);
				isSuccess = orderDao.updateOrderExtFlg4(POST_GILT_NOWMFAL_ORDER, orderNumberList, (String)giltRet.get(1));
				// 取消订单推送
			} else if (POST_GILT_CONFIRMED_ORDER.equals(getTaskName())) {
				retContent = (String)giltRet.get(1);
				isSuccess = orderDao.updateOrdersSendInfoAndExtTxt1(POST_GILT_CONFIRMED_ORDER, orderNumberList, (String) giltRet.get(1));
				// 退货订单推送
			} else if (POST_GILT_CANCEL_ORDER.equals(getTaskName())) {
				retContent = (String)giltRet.get(2);
				isSuccess = orderDao.updateOrderExtFlg2and3andExtTxt1(POST_GILT_CANCEL_ORDER, orderNumberList, (Boolean)giltRet.get(0), (Boolean)giltRet.get(1), (String)giltRet.get(2));
			}

			if (isSuccess) {
				List<OutFormOrderdetailOrders> pushOrderList = new ArrayList<>();
				pushOrderList.add(orderInfo);

				String notesStr = getBatchNoteSqlData(pushOrderList, getTaskName(), retContent);

				isSuccess = orderDao.insertNotesBatchData(notesStr, pushOrderList.size());
			}

			// 执行结果判定
			if (isSuccess) {
				transactionManager.commit(status);

			} else {
				transactionManager.rollback(status);

				issueLog.log("updateOrdersInfo",
						"updateOrdersInfo error;task name = " + getTaskName(),
						ErrorType.BatchJob,
						SubSystem.OMS);
			}
		} catch (Exception ex) {
			$error("updateOrdersSendInfo", ex);

			isSuccess = false;

			transactionManager.rollback(status);

			issueLog.log("updateOrdersInfo",
					"updateOrdersInfo error;task name = " + getTaskName(),
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		return isSuccess;
	}

	/**
	 * 批处理Notes信息所需数据拼装
	 *
	 * @param orderInfoList 订单列表（含明细）
	 * @param taskName String
	 * @return String
	 */
	private String getBatchNoteSqlData(List<OutFormOrderdetailOrders> orderInfoList, String taskName, String retContent) {
		StringBuilder sqlBuffer = new StringBuilder();
		ArrayList<String> orderNumberList = new ArrayList<>();

		for (OutFormOrderdetailOrders orderInfo : orderInfoList) {
			// 订单信息
			//	相同订单号只出一条Notes
			if (orderNumberList.contains(orderInfo.getOrderNumber())) {
				continue;
			} else {
				orderNumberList.add(orderInfo.getOrderNumber());
			}

			// 拼装SQL values 部分
			String entryDateTime = DateTimeUtil.getNow(DateTimeUtil.DEFAULT_DATETIME_FORMAT);

			if (StringUtils.isEmpty(sqlBuffer.toString())) {
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName, retContent));
			} else {
				sqlBuffer.append(Constants.COMMA_CHAR);
				sqlBuffer.append(prepareNotesData(orderInfo, entryDateTime, entryDateTime, taskName, retContent));
			}
		}

		return sqlBuffer.toString();

	}

	/**
	 * 一条订单的插入notes表语句values部分
	 */
	private String prepareNotesData(OutFormOrderdetailOrders orderInfo, String entryDate, String entryTime, String taskName, String retContent) {
		StringBuilder sqlValueBuffer = new StringBuilder();

		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);

		// type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.NOTES_SYSTEM);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// numeric_key
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// item_number
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.ZERO_CHAR);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entry_date
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(entryDate);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entry_time
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(entryTime);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// notes
		String note = "";
		// 正常订单推送（placed）
		String errNote = "";
		if (!StringUtils.isEmpty(retContent)) {
			errNote = "Error Content : " + transferStr(retContent);
		}
		if (POST_GILT_NOWMFAL_ORDER.equals(taskName)) {
			note = "Push order to Gilt（placed）" + errNote;
			// 正常订单推送（confirmed）
		} else if (POST_GILT_CONFIRMED_ORDER.equals(taskName)) {
			note = "Push order to Gilt（confirmed）" + errNote;
			// 取消订单推送
		} else if (POST_GILT_CANCEL_ORDER.equals(taskName)) {
			note = "Push cancel order to Gilt" + errNote;
		}

		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(note);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// entered_by
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// parent_type
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.NOTES_SYSTEM);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// parent_key
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getOrderNumber());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// source_order_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(orderInfo.getSourceOrderId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// creater
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// created
		sqlValueBuffer.append(Constants.NOW_MYSQL);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// modifier
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(taskName);
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// modified
		sqlValueBuffer.append(Constants.NOW_MYSQL);

		sqlValueBuffer.append(Constants.RIGHT_BRACKET_CHAR);

		return sqlValueBuffer.toString();
	}

	/**
	 * 转换数据中的特殊字符
	 */
	private String transferStr(String data) {
		if (StringUtils.isNullOrBlank2(data)) {
			return Constants.EMPTY_STR;
		} else {
			return data.replaceAll("'", "''");
		}
	}
}
