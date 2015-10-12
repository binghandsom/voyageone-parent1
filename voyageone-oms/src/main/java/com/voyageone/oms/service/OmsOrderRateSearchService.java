package com.voyageone.oms.service;

import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;

import java.util.List;

/**
 * OMS 订单明细检索service
 * 
 * @author jacky
 *
 */
public interface OmsOrderRateSearchService {

	/**
	 * 汇率检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @param currency 币种
	 * @param page 当前页
	 * @param size 页面行数
	 * @param user 当前用户
	 * @return
	 */
	public List<OutFormSearchRate> searchRate(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency, int page, int size, UserSessionBean user);

	/**
	 * 汇率件数检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @param currency 币种
	 * @return
	 */
	public  int getSearchRateCount(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency);
}
