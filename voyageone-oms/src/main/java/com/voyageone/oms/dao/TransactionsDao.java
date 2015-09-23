package com.voyageone.oms.dao;

import java.util.HashMap;
import java.util.List;

import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.oms.formbean.OutFormOrderdetailTransactions;
import com.voyageone.oms.modelbean.TransactionsBean;

@Repository
public class TransactionsDao extends BaseDao {
	
	/**
	 * 获得订单Transactions信息 
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTransactions> getOrderTransactionsInfo(String orderNumber) {
		 List<OutFormOrderdetailTransactions> orderTransactionsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_getOrderTransactionsInfo", orderNumber);
		
		return orderTransactionsList;
	}
	
	/**
	 * 获得订单Transactions信息 ，根据一组（order_number）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTransactions> getOrderTransactionsInfo(List<String> orderNumberList) {
		
		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderNumberList", orderNumberList);
		
		 List<OutFormOrderdetailTransactions> orderTransactionsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_getOrderTransactionsInfoByOrderNumList", paraIn);
		
		return orderTransactionsList;
	}
	
	/**
	 * 获得订单Transactions信息 ，根据一组（order_number）
	 * 
	 * @return
	 */
	public List<OutFormOrderdetailTransactions> getOrderTransactionsInfoBySourceOrderId(String sourceOrderId) {		
		
		 List<OutFormOrderdetailTransactions> orderTransactionsList =  (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_getOrderTransactionsInfoBySourceOrderId", sourceOrderId);
		
		return orderTransactionsList;
	}
	
	/**
	 * 订单Transactions信息追加
	 * 
	 * @return
	 */
	public boolean insertTransactionsInfo(TransactionsBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_insertTransactionInfo", bean);
		
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
	public boolean updateTransactionsInfoForBindPriceDiffOrder(TransactionsBean bean) {
		boolean ret = true;

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_transactions_updateTransactionsInfoForBindPriceDiffOrder", bean);

		return ret;
	}
}
