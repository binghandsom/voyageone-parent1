package com.voyageone.oms.service.impl;

import com.csvreader.CsvReader;
import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.dao.OrderDetailDao;
import com.voyageone.oms.dao.SettlementDao;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;
import com.voyageone.oms.formbean.OutFormUploadSettlementFile;
import com.voyageone.oms.modelbean.SettlementBean;
import com.voyageone.oms.modelbean.SettlementFileBean;
import com.voyageone.oms.service.OmsOrderAccountingsSearchService;
import com.voyageone.oms.service.OmsOrderAccountingsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrderAccountingsSearchServiceImpl implements OmsOrderAccountingsSearchService {
	private static Log logger = LogFactory.getLog(OmsOrderAccountingsSearchServiceImpl.class);

	@Autowired
	private SettlementDao settlementDao;

	/**
	 * 账务文件检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @param page 当前页
	 * @param size 页面行数
	 * @param user 当前用户
	 * @return
	 */
	public List<OutFormSearchSettlementFile> searchSettlementFile(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo, int page, int size, UserSessionBean user) {
		List<OutFormSearchSettlementFile> settlementFileList = settlementDao.getSettlementFileList(storeId, channelId, searchDateFrom, searchDateTo, page * size, size);

		if (settlementFileList.size() > 0) {
			for (int i = 0; i < settlementFileList.size(); i++) {
				OutFormSearchSettlementFile settlementFileInfo = settlementFileList.get(i);
				settlementFileInfo.setUploadTime(DateTimeUtil.getLocalTime(settlementFileInfo.getUploadTime(), user.getTimeZone()));
			}
		}

		return settlementFileList;
	}

	/**
	 * 账务文件件数检索
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param searchDateFrom 检索日开始
	 * @param searchDateTo 检索日终了
	 * @return
	 */
	public  int getSearchSettlementFileCount(List<String> storeId, List<String> channelId, String searchDateFrom, String searchDateTo) {
		int settlementFileCount = settlementDao.getSettlementFileCount(storeId, channelId, searchDateFrom, searchDateTo);

		return settlementFileCount;
	}
}
