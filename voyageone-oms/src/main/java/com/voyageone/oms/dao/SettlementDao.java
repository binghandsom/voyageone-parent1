package com.voyageone.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;
import com.voyageone.oms.modelbean.CartBean;
import com.voyageone.oms.modelbean.SettlementFileBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SettlementDao extends BaseDao {

	/**
	 * 批处理插入Settlement表数据
	 *
	 * @param settlementSqlValue
	 * @param size
	 * @return
	 */
	public boolean insertSettlementInfo(String settlementSqlValue, int size) {
		boolean ret = false;

		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("values", settlementSqlValue);

		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_insertSettlementInfo", dataMap);

		if (retCount == size) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 批处理插入Settlement表数据
	 *
	 * @param originSourceOrderList
	 * @return
	 */
	public boolean updateSettlementInfo(List<String> originSourceOrderList) {
		boolean ret = true;

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("originSourceOrderList", originSourceOrderList);

		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_updateSettlementInfo", dataMap);

		return ret;
	}

	/**
	 * 订单明细信息追加
	 *
	 * @return
	 */
	public boolean insertSettlementFileInfo(SettlementFileBean bean) {
		boolean ret = false;
		int retCount = updateTemplate.insert(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_file_insertSettlementFileInfo", bean);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得订单信息，根据（order_number）
	 *
	 * @return
	 */
	public SettlementFileBean getSettlementFileInfo(String settlementFileId) {
		SettlementFileBean settlementFileInfo = (SettlementFileBean) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_file_getSettlementFileInfo", settlementFileId);

		return settlementFileInfo;
	}

	/**
	 * 获得订单信息，根据（order_number）
	 *
	 * @return
	 */
	public List<OutFormSearchSettlementFile> getSettlementFileList(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, int page, int size) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("storeId", storeId);
		paraIn.put("channelId", channelId);
		paraIn.put("searchDateFrom", searchDateFrom);
		paraIn.put("searchDateTo", searchDateTo);
		paraIn.put("offset", page);
		paraIn.put("size", size);

		List<OutFormSearchSettlementFile> settlementFileList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_file_search", paraIn);

		return settlementFileList;
	}

	/**
	 * 获得订单信息，根据（order_number）
	 *
	 * @return
	 */
	public int getSettlementFileCount(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("storeId", storeId);
		paraIn.put("channelId", channelId);
		paraIn.put("searchDateFrom", searchDateFrom);
		paraIn.put("searchDateTo", searchDateTo);

		int settlementFileCount = (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_settlement_file_search_count", paraIn);

		return settlementFileCount;
	}
}
