package com.voyageone.oms.controller;

import com.voyageone.base.BaseController;
import com.voyageone.common.Constants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.MasterInfoBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.oms.OmsConstants;
import com.voyageone.oms.OmsMessageConstants;
import com.voyageone.oms.OmsUrlConstants;
import com.voyageone.oms.formbean.OutFormSearchRate;
import com.voyageone.oms.formbean.OutFormSearchSettlementFile;
import com.voyageone.oms.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * OMS OrdersDetail画面
 * 
 * @author Jerry
 *
 */
@Scope(Constants.SCOPE_PROTOTYPE)
@Controller
@RequestMapping(value = OmsUrlConstants.URL_OMS_ORDER_ACCOUNTINGS)
public class OmsOrderAccountingsController extends BaseController {
	/**
	 * 日志
	 */
	private static Log logger = LogFactory.getLog(OmsOrderAccountingsController.class);
	@Autowired
	private OmsOrdersSearchService omsOrdersSearchService;
	@Autowired
	private OmsOrderAccountingsService omsOrderAccountingsService;
	@Autowired
	private OmsOrderAccountingsSearchService omsOrderAccountingsSearchService;
	@Autowired
	private OmsOrderRateService omsOrderRateService;
	@Autowired
	private OmsOrderRateSearchService omsOrderRateSearchService;

	/**
	 * 账务文件保存
	 */
	@RequestMapping(value = "/doSaveAccountingFile", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResponseBean doSaveAccountingFile(@RequestParam String fileType, @RequestParam MultipartFile file) {

		// ajax 返回结果
		AjaxResponseBean result = AjaxResponseBean.newResult(true, this).setResult(true);

		omsOrderAccountingsService.saveSettlementFile(fileType, file, result, getUser());

//		return AjaxResponseBean.newResult(true, this).setResult(false, OmsMessageConstants.MESSAGE_CODE_210060, MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION);
		return result;
	}

	/**
	 * 画面初始化（获得查询条件）
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/doInit", method = RequestMethod.POST)
	public void doInit(HttpServletRequest request, HttpServletResponse response) {
		// 从session中获得该用户的检索条件
		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		Iterator<String> it = propertyPermissions.keySet().iterator();
		// 门店列表
		List<PermissionBean> propertyList = new ArrayList<PermissionBean>();
		// 门店对应渠道列表
		List<Map<String, Object>> shoppingCartInfoList = new ArrayList<Map<String, Object>>();
		while (it.hasNext()) {
			String propertyId = it.next();
			propertyList.add(propertyPermissions.get(propertyId));

			List<MasterInfoBean> shoppingCartList = omsOrdersSearchService.getShoppingCarts(propertyId);
			Map<String, Object> shoppingCartInfo = new HashMap<String, Object>();
			shoppingCartInfo.put("propertyId", propertyId);

//			// default 项目追加
//			MasterInfoBean defaultItem = new MasterInfoBean();
//			defaultItem.setId("");
//			defaultItem.setName("Please select...");
//			shoppingCartList.add(0, defaultItem);

			shoppingCartInfo.put("shoppingCartList", shoppingCartList);
			shoppingCartInfoList.add(shoppingCartInfo);
		}

		// 获取用户时区
		int zone = user.getTimeZone();
		// 检索结束日
		String searchDateTo = DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), zone).substring(0, 10);
		String searchDateFrom = DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(OmsConstants.ORDER_SEARCH_FROM_MONTH), zone).substring(0, 10);

		// 获得检索条件下拉信息
		Map<String, Object> resultDataMap = new HashMap<String, Object>();
		resultDataMap.put("propertyList", propertyList);
		resultDataMap.put("shoppingCartInfoList", shoppingCartInfoList);
		// 检索时间终了
		resultDataMap.put("searchDateTo", searchDateTo);
		// 检索时间开始
		resultDataMap.put("searchDateFrom", searchDateFrom);

		AjaxResponseBean responseBean = new AjaxResponseBean();
		// 设置返回结果
		responseBean.setResult(true);
		responseBean.setResultInfo(resultDataMap);

		// 结果返回输出流
		responseBean.writeTo(request, response);

		// 输出结果出力
		logger.info(responseBean.toString());

		return;
	}

	/**
	 * SettlementFile检索
	 *
	 * @param params
	 * @param response
	 */
	@RequestMapping(value = "/doSearchSettlementFile", method = RequestMethod.POST)
	public void search(@RequestBody Map<String, Object> params, HttpServletResponse response) {
		// DB中file_type
		String fileType = (String) params.get("fileType");
		// DB中order_channel_id
		List<String> storeId = (List<String>) params.get("storeId");
		// DB中cart_id
		List<String> channelId = (List<String>) params.get("channelId");
		String searchDateFrom = (String) params.get("searchDateFrom");
		String searchDateTo = (String) params.get("searchDateTo");

		int page = (int) params.get("page");
		int size = (int) params.get("size");

		searchDateFrom = StringUtils.isNullOrBlank2(searchDateFrom) ? "1990-01-01 00:00:00" : DateTimeUtil.getGMTTimeFrom(searchDateFrom, getUser().getTimeZone());
		searchDateTo = StringUtils.isNullOrBlank2(searchDateTo) ? "2099-12-31 23:59:59" : DateTimeUtil.getGMTTimeTo(searchDateTo, getUser().getTimeZone());

		List<OutFormSearchSettlementFile> settlementFileList = omsOrderAccountingsSearchService.searchSettlementFile(fileType, storeId, channelId, searchDateFrom, searchDateTo, page, size, getUser());

		int rowsCount = omsOrderAccountingsSearchService.getSearchSettlementFileCount(fileType, storeId, channelId, searchDateFrom, searchDateTo);

		Map<String, Object> settlementFileSearchResultMap = new HashMap<String, Object>();
		settlementFileSearchResultMap.put("data", settlementFileList);
		settlementFileSearchResultMap.put("rows", rowsCount);

		AjaxResponseBean
				.newResult(true)
				.setResultInfo(settlementFileSearchResultMap)
				.writeTo(getRequest(), response);
	}

	/**
	 * 汇率保存
	 *
	 * @param params
	 * @param response
	 */
	@RequestMapping(value = "/doSaveRate", method = RequestMethod.POST)
	public void doSaveRate(@RequestBody Map<String, Object> params, HttpServletResponse response) {
		// DB 中order_channel_id
		String storeId = (String) params.get("storeId");
		// DB中cart_id
		String channelId = (String) params.get("channelId");
		// 汇率
		String rate = (String) params.get("rate");
		// 计算误差
		String calculationError = (String) params.get("calculationError");
		// 币种
		String currency = (String) params.get("currency");
		// 汇率日期（含时间）
		String rateTime = DateTimeUtil.getGMTTime();

		List<Object> retSaveRet = omsOrderRateService.saveRate(storeId, channelId, rate, currency, rateTime, calculationError, getUser());

		if ((boolean)retSaveRet.get(0)) {
			Map<String, Object> saveRateResultMap = new HashMap<String, Object>();
			saveRateResultMap.put("calculationError", (String)retSaveRet.get(1));

			AjaxResponseBean
					.newResult(true)
					.setResultInfo(saveRateResultMap)
					.writeTo(getRequest(), response);
		} else {
			AjaxResponseBean
					.newResult(false)
					.setResult(false, OmsMessageConstants.MESSAGE_CODE_210071,
							MessageConstants.MESSAGE_TYPE_BUSSINESS_EXCEPTION)
					.writeTo(getRequest(), response);
		}
	}

	/**
	 * 汇率检索
	 *
	 * @param params
	 * @param response
	 */
	@RequestMapping(value = "/doSearchRate", method = RequestMethod.POST)
	public void doSearchRate(@RequestBody Map<String, Object> params, HttpServletResponse response) {
		// DB 中order_channel_id
		List<String> storeId = (List<String>) params.get("storeId");
		// DB中cart_id
		List<String> channelId = (List<String>) params.get("channelId");
		String searchDateFrom = (String) params.get("searchDateFrom");
		String searchDateTo = (String) params.get("searchDateTo");
		String currency = (String) params.get("currency");

		int page = (int) params.get("page");
		int size = (int) params.get("size");

		searchDateFrom = StringUtils.isNullOrBlank2(searchDateFrom) ? "1990-01-01 00:00:00" : DateTimeUtil.getGMTTimeFrom(searchDateFrom, getUser().getTimeZone());
		searchDateTo = StringUtils.isNullOrBlank2(searchDateTo) ? "2099-12-31 23:59:59" : DateTimeUtil.getGMTTimeTo(searchDateTo, getUser().getTimeZone());

		List<OutFormSearchRate> rateList = omsOrderRateSearchService.searchRate(storeId, channelId, searchDateFrom, searchDateTo, currency, page, size, getUser());

		int rowsCount = omsOrderRateSearchService.getSearchRateCount(storeId, channelId, searchDateFrom, searchDateTo, currency);

		Map<String, Object> rateSearchResultMap = new HashMap<String, Object>();
		rateSearchResultMap.put("data", rateList);
		rateSearchResultMap.put("rows", rowsCount);

		AjaxResponseBean
				.newResult(true)
				.setResultInfo(rateSearchResultMap)
				.writeTo(getRequest(), response);
	}
}
