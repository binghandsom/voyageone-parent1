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
import com.voyageone.oms.dao.RateDao;
import com.voyageone.oms.dao.SettlementDao;
import com.voyageone.oms.formbean.OutFormOrderdetailOrders;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.formbean.OutFormUploadSettlementFile;
import com.voyageone.oms.modelbean.RateBean;
import com.voyageone.oms.modelbean.SettlementBean;
import com.voyageone.oms.modelbean.SettlementFileBean;
import com.voyageone.oms.service.OmsOrderAccountingsService;
import com.voyageone.oms.service.OmsOrderRateService;
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
import java.util.*;

@Scope(Constants.SCOPE_PROTOTYPE)
@Service
public class OmsOrderRateServiceImpl implements OmsOrderRateService {
	private static Log logger = LogFactory.getLog(OmsOrderRateServiceImpl.class);

	@Autowired
	private RateDao rateDao;

	@Autowired
	private DataSourceTransactionManager transactionManager;
	DefaultTransactionDefinition def =new DefaultTransactionDefinition();

	/**
	 * 保存汇率
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param rate 汇率
	 * @param currency 币种
	 * @param rateTime 汇率日期
	 * @param user 当前用户
	 * @return
	 */
	public List<Object> saveRate(String storeId, String channelId, String rate, String currency, String rateTime, String calculationError, UserSessionBean user) {
		List<Object> ret = new ArrayList<>();

		boolean isSuccess = false;
		String retCalculationError = calculationError;

		// 本地时间
		String localTime = DateTimeUtil.getLocalTime(rateTime, user.getTimeZone());
		// GMT时间
		String GMTTime = DateTimeUtil.getGMTTime(getLocalBeginTime(localTime), user.getTimeZone());

		RateBean rateInfo = new RateBean();
		rateInfo.setOrderChannelId(storeId);
		rateInfo.setCartId(channelId);
		rateInfo.setRate(rate);
		rateInfo.setCurrency(currency);
		rateInfo.setRateTime(GMTTime);

		retCalculationError = getCalculationError(storeId, channelId, currency, calculationError);
		rateInfo.setCalculationError(retCalculationError);

		rateInfo.setCreater(user.getUserName());
		rateInfo.setModifier(user.getUserName());

		isSuccess = rateDao.insertRateInfo(rateInfo);

		ret.add(isSuccess);
		ret.add(retCalculationError);

		return ret;
	}

	/**
	 * 获取本地时区开始时间
	 *
	 * @param localTime 本地时间
	 * @return
	 */
	private String getLocalBeginTime(String localTime) {
		String ret = "";
		String[] localTimeArr = localTime.split(" ");
		ret = localTimeArr[0] + " 00:00:00";
		return ret;
	}

	/**
	 * 计算误差取得
	 *
	 * @param storeId 渠道ID
	 * @param channelId cartID
	 * @param currency 币种
	 * @param calculationError 计算误差
	 * @return
	 */
	private String getCalculationError(String storeId, String channelId, String currency, String calculationError) {
		String ret = "";

		if (StringUtils.isEmpty(calculationError)) {
			List<OutFormSearchRate> rateList = rateDao.getRateListForCond(storeId, channelId, currency);

			if (rateList.size() == 0) {
				ret = OmsConstants.DEFAULT_CALCULATION_ERROR;
			} else {
				ret = rateList.get(0).getCalculationError();
			}
		}  else {
			ret = calculationError;
		}

		return ret;
	}
}
