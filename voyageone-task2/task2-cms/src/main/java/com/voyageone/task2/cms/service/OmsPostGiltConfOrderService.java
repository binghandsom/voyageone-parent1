package com.voyageone.task2.cms.service;

import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.task2.base.BaseTaskService;
import com.voyageone.task2.base.Constants;
import com.voyageone.task2.base.Enums.TaskControlEnums;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.base.util.TaskControlUtils;
import com.voyageone.task2.cms.dao.OrderDao;
import com.voyageone.task2.cms.formbean.OutFormOrderdetailOrders;
import com.voyageone.common.components.gilt.GiltOrderService;
import com.voyageone.common.components.gilt.bean.*;
import com.voyageone.common.components.gilt.exceptions.GiltException;
import com.voyageone.common.components.issueLog.IssueLog;
import com.voyageone.common.components.issueLog.enums.ErrorType;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OmsPostGiltConfOrderService extends BaseTaskService {
	
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
		return "OmsPostGiltConfOrderJob";
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
					postGiltConfirmedOrder(orderChannelID);
				}
			});

		}

		runWithThreadPool(threads, taskControlList);
	}

	/**
	 * 正常订单推送（confirmed）send_flg为0
	 *
	 */
	public boolean postGiltConfirmedOrder(String orderChannelIDParam) {
		boolean isSuccess = true;

		if (!orderChannelID.equals(orderChannelIDParam)) {
			return true;
		}

		// 待推送订单信息取得
		List<OutFormOrderdetailOrders> pushOrderList = getPushOrderListForNotConfirmed(orderChannelID);

		// 有数据的场合
		if (pushOrderList.size() > 0) {

			for (OutFormOrderdetailOrders orderInfo : pushOrderList) {
				// 向Gilt推送
				isSuccess = postGiltConfirmedOrderSub(orderInfo);
				if (!isSuccess) {
					break;
				}
			}
		} else {
			$info("postGiltConfirmedOrder push order rec = 0");
		}

		return isSuccess;
	}

	/**
	 * 正常订单向Gilt推送（confirmed）
	 *
	 */
	private boolean postGiltConfirmedOrderSub(OutFormOrderdetailOrders orderInfo) {
		// 异常判定
		boolean ret = true;

		try {
			// Gilt 推送
			List<Object> retArr = postGiltConfirmedOrderByService(orderInfo);

			// 推送标志更新
//			List<String> orderNumberList = new ArrayList<String>();
//			orderNumberList.add(orderInfo.getOrderNumber());
//			isSuccess = orderDao.updateOrdersSendInfoAndExtTxt1(POST_GILT_CONFIRMED_ORDER, orderNumberList, (String) retArr.get(1));
			boolean isSuccess = updateOrdersInfo(retArr, orderInfo);
			if (!isSuccess) {
				// DB 更新失败
				ret = false;
				issueLog.log("postGiltConfirmedOrder.postGiltConfirmedOrderSub",
						"postGiltConfirmedOrder send_flg(confirmed) update error;Push order number = " + orderInfo.getOrderNumber(),
						ErrorType.BatchJob,
						SubSystem.OMS);
			}

		} catch (Exception e) {
			ret = false;
			$error("postGiltConfirmedOrderSub", e);
			issueLog.log("postGiltConfirmedOrder.postGiltConfirmedOrderSub",
					"postGiltConfirmedOrder error;Push order number = " + orderInfo.getOrderNumber(),
					ErrorType.BatchJob,
					SubSystem.OMS);
		}

		return ret;
	}

	/**
	 * 正常订单向Gilt推送（调用Service 接口）
	 *
	 */
	private List<Object> postGiltConfirmedOrderByService(OutFormOrderdetailOrders orderInfo) throws Exception {
		List<Object> retArr = new ArrayList<>();
		String errorContent = "";
		try {
			GiltPatchOrderRequest request=new GiltPatchOrderRequest();
			request.setId(UUID.fromString(orderInfo.getClientOrderId()));
			request.setStatus(GiltOrderStatus.confirmed);
			GiltOrder orders= giltOrderService.patchOrder(request);
			$info("postGiltConfirmedOrder Success ; Push order uuid = " + orders.getId());
		} catch (GiltException e) {
			// TODO 通知客服 超卖（无法发货） 其他订单处理继续
			$info("postGiltConfirmedOrder order Gilt error " + e.getMessage() + " ; Push order number = " + orderInfo.getOrderNumber());
			errorContent = e.getMessage();
		}

		retArr.add(true);
		retArr.add(errorContent);
		return retArr;
	}

	/**
	 * 推送订单数据抽取
	 *
	 */
	private List<OutFormOrderdetailOrders> getPushOrderListForNotConfirmed(String orderChannelId) {
		// 订单信息取得
		return orderDao.getOrdersListByOrderChannelIdForNotConfirmed(orderChannelId, getConfirmedTime(orderChannelId));
	}

	/**
	 * Confirmed 时间取得
	 *
	 */
	private String getConfirmedTime(String orderChannelId) {

		String confirmedTimeInterval = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.confirmed_time_interval);
		Date confirmedTimeTmp = DateTimeUtil.addMinutes(DateTimeUtil.getDate(), Integer.valueOf(confirmedTimeInterval) * -1);

		return DateTimeUtil.format(confirmedTimeTmp, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
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
