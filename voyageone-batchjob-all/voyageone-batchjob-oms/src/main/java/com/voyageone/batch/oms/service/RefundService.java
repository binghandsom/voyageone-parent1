package com.voyageone.batch.oms.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.jd.open.api.sdk.domain.refundapply.RefundApplySoaService.RefundApplyVo;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.oms.OmsConstants;
import com.voyageone.batch.oms.dao.RefundDao;
import com.voyageone.batch.oms.modelbean.ChangedOrderInfo4Log;
import com.voyageone.common.components.jd.JdRefundService;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;

@Service
public class RefundService {
	private static Log logger = LogFactory.getLog(RefundService.class);

	@Autowired
	private RefundDao refundDao;

	@Autowired
	JdRefundService jdRefundService;

	@Autowired
	private DataSourceTransactionManager transactionManager;

	private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

	/**
	 * 获得未关闭退款订单
	 * 
	 * @param orderChannelId
	 * @param cartId
	 * @return
	 */
	public List<String> getUnclosedRefundId(String orderChannelId, String cartId) {
		return refundDao.getUnclosedRefundId(orderChannelId, cartId);
	}

	/**
	 * 取得订单history表中最新订单信息的时间, 以此时间作为向京东请求的开始时间
	 * 
	 * @return
	 */
	public String getJdLastHistoryRefundOrderTime(int preTime) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("orderChannelId", OmsConstants.CHANNEL_SNEAKERHEAD);
		dataMap.put("cartId", OmsConstants.CART_JD);

		String lastOrderTime = refundDao
				.getJdLastHistoryRefundOrderTime(dataMap);
		// 如果没取到时间
		if (StringUtils.isNullOrBlank2(lastOrderTime)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -preTime);
			lastOrderTime = df.format(calendar.getTime());
			// 格林威治时间转北京时间
			lastOrderTime = DateTimeUtil.getLocalTime(lastOrderTime,
					OmsConstants.TIME_ZONE_8);
		}

		logger.info("订单history表中最新订单信息的北京时间为：" + lastOrderTime);

		return lastOrderTime;
	}

	public ChangedOrderInfo4Log doJDRefundDetail(String id,
			String orderChannelId, String cartId, String target) {
		// 获得调用sneakerhead京东API所需信息
		ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);
		RefundApplyVo refundApplyVo = jdRefundService.doJDRefundDetail(
				shopBean, id);
		// 京东状态与SE状态转换表
		HashMap<Long, String> mapTradeStatusMap = new HashMap<Long, String>();
		mapTradeStatusMap.put(0l, "RefundCreated");
		mapTradeStatusMap.put(3l, "RefundSuccess");

		ChangedOrderInfo4Log refundOrder = new ChangedOrderInfo4Log();
		if (refundApplyVo != null) {
			refundOrder.setOrderChannelId(orderChannelId);
			refundOrder.setCartId(cartId);
			refundOrder.setTid(refundApplyVo.getOrderId());
			refundOrder.setBuyerNick(refundApplyVo.getBuyerName());
			refundOrder.setFee(refundApplyVo.getApplyRefundSum().toString());
			refundOrder.setRid(Long.toString(refundApplyVo.getId()));
			// String modifiedTime = DateTimeUtil.getLocalTime(new Date(),
			// OmsConstants.TIME_ZONE_8);
			refundOrder.setModifiedTime(refundApplyVo.getApplyTime());

			refundOrder.setStatus(mapTradeStatusMap.get(refundApplyVo
					.getStatus()));
			refundOrder.setTarget(target);
		}
		return refundOrder;
	}

	public List<ChangedOrderInfo4Log> doJDSearchRefundOrders(String ids,
			Long status, String refundApplyStartDate, String orderChannelId,
			String cartId, String target) throws IOException {
		// 获得当前时间
		String refundApplyEndDate = DateTimeUtil.getNow();
		// 格林威治时间转北京时间
		refundApplyEndDate = DateTimeUtil.getLocalTime(refundApplyEndDate,
				OmsConstants.TIME_ZONE_8);
		// 获得调用sneakerhead京东API所需信息
		ShopBean shopBean = ShopConfigs.getShop(orderChannelId, cartId);
		// 取新的未审核退款
		List<RefundApplyVo> refundApplyVos = jdRefundService
				.doJDSearchRefundOrders(shopBean, ids, refundApplyStartDate,
						refundApplyEndDate, status);

		List<ChangedOrderInfo4Log> refundOrders = new ArrayList<ChangedOrderInfo4Log>();
		// 京东状态与SE状态转换表
		HashMap<Long, String> mapTradeStatusMap = new HashMap<Long, String>();
		mapTradeStatusMap.put(0l, "RefundCreated");
		mapTradeStatusMap.put(3l, "RefundSuccess");
		// mapTradeStatusMap.put(2l, "RefundClosed");

		// File file = new File("d:\\lock.txt");
		// FileOutputStream fis = new FileOutputStream(file);

		// 遍历一下
		for (RefundApplyVo refundApplyVo : refundApplyVos) {

			ChangedOrderInfo4Log refundOrder = new ChangedOrderInfo4Log();
			refundOrder.setOrderChannelId(orderChannelId);
			refundOrder.setCartId(cartId);
			refundOrder.setTid(refundApplyVo.getOrderId());
			refundOrder.setBuyerNick(refundApplyVo.getBuyerName());
			refundOrder.setFee(refundApplyVo.getApplyRefundSum().toString());
			refundOrder.setRid(Long.toString(refundApplyVo.getId()));
			// String modifiedTime = DateTimeUtil.getLocalTime(new Date(),
			// OmsConstants.TIME_ZONE_8);
			refundOrder.setModifiedTime(refundApplyVo.getApplyTime());

			refundOrder.setStatus(mapTradeStatusMap.get(refundApplyVo
					.getStatus()));
			refundOrder.setTarget(target);
			refundOrders.add(refundOrder);

			// StringBuilder sb = new StringBuilder();
			// sb.append("Tid="+refundApplyVo.getOrderId()+",ApplyTime=" +
			// refundApplyVo.getApplyTime()
			// +",CheckTime="+refundApplyVo.getCheckTime()+",status="+refundApplyVo.getStatus());
			// sb.append("\r\n");
			// fis.write(sb.toString().getBytes());

			// logger.info("订单：Tid="+refundApplyVo.getOrderId()+",ApplyTime=" +
			// refundApplyVo.getApplyTime()
			// +",CheckTime="+refundApplyVo.getCheckTime()+",status="+refundApplyVo.getStatus());
		}
		// fis.close();
		return refundOrders;
	}

	public String getBatchRefundOrderSqlData(
			List<ChangedOrderInfo4Log> refundOrderInfoList, String taskName) {
		StringBuilder sqlBuffer = new StringBuilder();

		int size = refundOrderInfoList.size();

		for (int i = 0; i < size; i++) {
			// 新订单信息
			ChangedOrderInfo4Log refundOrderInfo = refundOrderInfoList.get(i);

			// 拼装SQL values 部分
			String sqlValue = preparechangedOrderHistroyData(refundOrderInfo,
					taskName);
			sqlBuffer.append(sqlValue);
			if (i < (size - 1)) {
				sqlBuffer.append(Constants.COMMA_CHAR);
			}
		}

		return sqlBuffer.toString();
	}

	private String preparechangedOrderHistroyData(
			ChangedOrderInfo4Log refundOrder, String taskName) {
		StringBuilder sqlValueBuffer = new StringBuilder();

		sqlValueBuffer.append(Constants.LEFT_BRACKET_CHAR);
		// order_channed_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getOrderChannelId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// cart_id
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getCartId());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// tid
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getTid());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// status
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getStatus());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// modified_time
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getModifiedTime());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// buyer_nick
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getBuyerNick());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// target
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getTarget());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);

		// fee
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(refundOrder.getFee());
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append(Constants.COMMA_CHAR);
		
		// refund_phase
		sqlValueBuffer.append(Constants.APOSTROPHE_CHAR);
		sqlValueBuffer.append("OnSail");
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

	public boolean insertRefundOrdersInfo(
			List<ChangedOrderInfo4Log> refundOrderList, String taskName) {
		String sqlBatch = getBatchRefundOrderSqlData(refundOrderList, taskName);
		return refundDao.insertRefundOrdersInfo(sqlBatch,
				refundOrderList.size());
	}
}
