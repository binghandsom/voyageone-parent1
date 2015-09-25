package com.voyageone.oms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;
import com.voyageone.oms.modelbean.RateBean;
import com.voyageone.oms.modelbean.SettlementFileBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RateDao extends BaseDao {

	/**
	 * 鎵瑰鐞嗘彃鍏ate琛ㄦ暟鎹?
	 *
	 * @param rateInfo
	 * @return
	 */
	public boolean insertRateInfo(RateBean rateInfo) {
		boolean ret = false;

		int retCount = updateTemplate.update(Constants.DAO_NAME_SPACE_OMS + "oms_bt_rate_updateRateInfo", rateInfo);

		if (retCount > 0) {
			ret = true;
		}

		return ret;
	}

	/**
	 * 获得汇率信息，根据（order_number）
	 *
	 * @return
	 */
	public List<OutFormSearchRate> getRateList(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency, int page, int size) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("storeId", storeId);
		paraIn.put("channelId", channelId);
		paraIn.put("searchDateFrom", searchDateFrom);
		paraIn.put("searchDateTo", searchDateTo);
		paraIn.put("currency", currency);
		paraIn.put("offset", page);
		paraIn.put("size", size);

		List<OutFormSearchRate> rateList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_rate_search", paraIn);

		return rateList;
	}

	/**
	 * 获得汇率信息，根据（order_number）
	 *
	 * @return
	 */
	public int getRateCount(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("storeId", storeId);
		paraIn.put("channelId", channelId);
		paraIn.put("searchDateFrom", searchDateFrom);
		paraIn.put("searchDateTo", searchDateTo);
		paraIn.put("currency", currency);

		int settlementFileCount = (int) selectOne(Constants.DAO_NAME_SPACE_OMS + "oms_bt_rate_search_count", paraIn);

		return settlementFileCount;
	}

	/**
	 * 获得汇率信息，根据（order_number）
	 *
	 * @return
	 */
	public List<OutFormSearchRate> getRateListForCond(String storeId, String channelId, String currency) {

		HashMap<String, Object> paraIn = new HashMap<String, Object>();
		paraIn.put("storeId", storeId);
		paraIn.put("channelId", channelId);
		paraIn.put("currency", currency);

		List<OutFormSearchRate> rateList = (List) selectList(Constants.DAO_NAME_SPACE_OMS + "oms_bt_rate_searchForCond", paraIn);

		return rateList;
	}
}
