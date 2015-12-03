package com.voyageone.oms.dao;

import java.util.List;

import com.voyageone.common.Constants;
import com.voyageone.oms.modelbean.OrderPaymentsBean;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailPayments;

@Repository
public class PaymentsDao extends BaseDao {	
	
	/**
	 * 获得订单Transactions信息 ，根据source_order_id
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailPayments> getOrdePaymentsInfo(String sourceOrderId) {

		// oms_bt_payments -〉oms_bt_settlement 变更对应 20150824
//		 List<OutFormOrderdetailPayments> orderPaymentsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_getOrderPaymentsInfoBySourceOrderId", sourceOrderId);
		List<OutFormOrderdetailPayments> orderPaymentsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_getOrderPaymentsInfoBySourceOrderId", sourceOrderId);

		return orderPaymentsList;
	}

	/**
	 * 处理标志位更新
	 *
	 * @return
	 */
	public boolean insertPaymentInfo(OrderPaymentsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_payments_insertForCN", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 支付信息更新（差价订单绑定，可能记录不存在）
	 *
	 * @return
	 */
	public boolean updatePaymentsInfoForBindPriceDiffOrder(OrderPaymentsBean bean) {
		boolean ret = true;

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_payments_updatePaymentsInfoForBindPriceDiffOrder", bean);

		return ret;
	}
}
