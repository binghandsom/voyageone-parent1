package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.formbean.OutFormOrderDetailOrderDetail;
import com.voyageone.batch.cms.formbean.OutFormOrderdetailOrders;
import com.voyageone.common.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderDao extends BaseDao {
	
	private Log logger = LogFactory.getLog(this.getClass());

	/**
	 * 订单信息更新（ExtFlg4）
	 *
	 * @return
	 */
	public boolean updateOrderExtFlg4(String taskName, List<String> orderNumberList, String extContent) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);
		paraIn.put("extTxt1", extContent);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "oms_bt_ext_orders_updateExtFlg4", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 订单信息更新（发送标志）
	 *
	 * @return
	 */
	public boolean updateOrdersClientOrderIdInfo(String order_number, String clientOrderId, String taskName) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("order_number", order_number);
		paraIn.put("clientOrderId", clientOrderId);
		paraIn.put("modifier", taskName);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "update_ClientOrderId", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得订单信息，根据（source_order_id）(未placed)
	 *
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersListByOrderChannelIdForNotSend(String orderChannelId) {
		List<OutFormOrderdetailOrders> ordersList = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "oms_bt_orders_getOrdersListByOrderChannelIdForNotSend", orderChannelId);

		return ordersList;
	}

	/**
	 * 获得订单详细信息，根据一组（order_number）
	 *
	 * @return
	 */
	public List<OutFormOrderDetailOrderDetail> getOrderDetailsInfo(String orderChannelId, List<String> orderNumberList) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("orderChannelId", orderChannelId);
		paraIn.put("orderNumberList", orderNumberList);

		List<OutFormOrderDetailOrderDetail> orderDetailsList =  (List) selectList(Constants.DAO_NAME_SPACE_CMS + "oms_bt_order_details_getOrderDetailsInfoByOrderNumList", paraIn);

		return orderDetailsList;
	}

	/**
	 * 订单信息更新（发送标志）
	 *
	 * @return
	 */
	public boolean updateOrdersSendInfoAndExtTxt1(String taskName, List<String> orderNumberList, String extContent) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);
		paraIn.put("extTxt1", extContent);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "oms_bt_ext_orders_updateSendFlagAndExtTxt1", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得订单信息，根据（source_order_id）（未confirmed）
	 *
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersListByOrderChannelIdForNotConfirmed(String orderChannelId, String orderDateTime) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("orderChannelId", orderChannelId);
		param.put("orderDateTime", orderDateTime);
		List<OutFormOrderdetailOrders> ordersList = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "oms_bt_orders_getOrdersListByOrderChannelIdForNotConfirmed", param);

		return ordersList;
	}

	/**
	 * 订单信息更新（ExtFlg4）
	 *
	 * @return
	 */
	public boolean updateOrderExtFlg2and3andExtTxt1(String taskName, List<String> orderNumberList, boolean cancelClientOrderSendFlag, boolean thirdPartyCancelOrderFlag, String extContent) {
		boolean ret = false;

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("taskName", taskName);
		paraIn.put("orderNumberList", orderNumberList);
		paraIn.put("cancelClientOrderSendFlag", cancelClientOrderSendFlag);
		paraIn.put("thirdPartyCancelOrderFlag", thirdPartyCancelOrderFlag);
		paraIn.put("extTxt1", extContent);

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "oms_bt_ext_orders_updateExtFlg2and3andExtTxt1", paraIn);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得订单信息，根据（source_order_id）（cancel order）
	 *
	 * @return
	 */
	public List<OutFormOrderdetailOrders> getOrdersListByOrderChannelIdForCancel(String orderChannelId) {

		List<OutFormOrderdetailOrders> ordersList = (List) selectList(Constants.DAO_NAME_SPACE_CMS + "oms_bt_orders_getOrdersListByOrderChannelIdForCancel", orderChannelId);

		return ordersList;
	}

	/**
	 * 批处理插入notes表数据
	 *
	 * @param orderNotesStr
	 * @param size
	 * @return
	 */
	public boolean insertNotesBatchData(String orderNotesStr, int size) {
		boolean ret = false;

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", orderNotesStr);

		try {
			int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_CMS + "oms_bt_notes_insertNotesBatchData", dataMap);

			if (retCount == size) {
				ret = true;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}

		return ret;
	}
}
