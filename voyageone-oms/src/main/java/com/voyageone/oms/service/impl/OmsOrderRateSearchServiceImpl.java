package com.voyageone.oms.service.impl;

import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.dao.RateDao;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.service.OmsOrderRateSearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrderRateSearchServiceImpl implements OmsOrderRateSearchService {
	private static Log logger = LogFactory.getLog(OmsOrderRateSearchServiceImpl.class);

	@Autowired
	private RateDao rateDao;

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
	public List<OutFormSearchRate> searchRate(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency, int page, int size, UserSessionBean user) {
		List<OutFormSearchRate> rateList = rateDao.getRateList(storeId, channelId, searchDateFrom, searchDateTo, currency, page * size, size);

		if (rateList.size() > 0) {
			for (int i = 0; i < rateList.size(); i++) {
				OutFormSearchRate rateInfo = rateList.get(i);
				rateInfo.setRateTime(DateTimeUtil.getLocalTime(rateInfo.getRateTime(), user.getTimeZone()));
			}
		}

		return rateList;
	}

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
	public  int getSearchRateCount(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, String currency) {
		int rateCount = rateDao.getRateCount(storeId, channelId, searchDateFrom, searchDateTo, currency);

		return rateCount;
	}
}
