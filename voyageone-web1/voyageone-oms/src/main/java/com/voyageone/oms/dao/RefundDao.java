package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailRefunds;
import com.voyageone.oms.modelbean.OrderRefundsBean;

@Repository
public class RefundDao extends BaseDao {
	
	/**
	 * 获得Refund信息
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailRefunds> getOrderRefundsListBySourceOrderId(String sourceOrderId) {
		List<OutFormOrderdetailRefunds> orderRefundsList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_getOrderRefundsInfo", sourceOrderId);
		
		return orderRefundsList;
	}

	/**
	 * 获得Refund信息，根据处理区分
	 *
	 * @return
	 */
	public List<OutFormOrderdetailRefunds> getOrderRefundsListByProcesFlag(String sourceOrderId, boolean processFlag) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderId", sourceOrderId);
		paraIn.put("processFlag", processFlag);

		List<OutFormOrderdetailRefunds> orderRefundsList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_getOrderRefundsInfoByProcesFlag", paraIn);

		return orderRefundsList;
	}

	/**
	 * 获得Refund信息，根据处理区分
	 *
	 * @return
	 */
	public int getOrderRefundsCount(String sourceOrderId, boolean processFlag) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("sourceOrderId", sourceOrderId);
		paraIn.put("processFlag", processFlag);

		int retCount = (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_getOrderRefundsCount", paraIn);

		return retCount;
	}
	
	/**
	 * 处理标志位更新
	 * 
	 * @return
	 */
	public boolean updateProcessFlag(OrderRefundsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_updateProcessFlag", bean);
		
		if (retCount > 0) {
			ret = true;
		}
		
		return ret;
	}

	/**
	 * 处理标志位更新
	 *
	 * @return
	 */
	public boolean insertRefundInfo(OrderRefundsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_insertForCN", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 交易信息更新（差价订单绑定，可能记录不存在）
	 *
	 * @return
	 */
	public boolean updateRefundsInfoForBindPriceDiffOrder(OrderRefundsBean bean) {
		boolean ret = true;

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_order_refunds_updateRefundsInfoForBindPriceDiffOrder", bean);

		return ret;
	}
}
