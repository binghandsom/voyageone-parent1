package com.voyageone.wms.service.impl;

import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.dao.ReportDao;
import com.voyageone.wms.formbean.FormReportBean;
import com.voyageone.wms.service.WmsReportService;
import com.voyageone.wms.service.impl.reportImpl.WmsInvDetReportService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service.impl]  
 * @ClassName    [WmsReportServiceImpl]
 * @Description  [report服务类接口实现类]
 * @Author       [sky]   
 * @CreateDate   [20150720]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Service
public class WmsReportServiceImpl implements WmsReportService{

	protected final static Log logger = LogFactory.getLog(WmsReportServiceImpl.class);

	@Autowired
	private ReportDao reportDao;

	@Autowired
	private WmsInvDetReportService wmsInvDetReportService;

	@Override
	public void init(HttpServletRequest request, HttpServletResponse response, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取渠道
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		List<PermissionBean> propertyList = propertyPermissions.keySet().stream().map(propertyPermissions::get).collect(Collectors.toList());
        // 获取开始日期（当前日期的前6天）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addDays(DateTimeUtil.getDate(), -6), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);

//		//获取用户仓库(只显示库存不是由品牌方管理的仓库)
//		ArrayList<ChannelStoreBean> channelStoreList = new ArrayList<>();
//		ChannelStoreBean channelStoreBean = new ChannelStoreBean();
//		channelStoreBean.setStore_id(0);
//		channelStoreBean.setStore_name("ALL");
//
//		channelStoreList.add(channelStoreBean);
//		for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
//			StoreBean store = StoreConfigs.getStore(new Long(storeBean.getStore_id()));
//			if (store.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId())) {
//				channelStoreList.add(storeBean);
//			}
//		}

		resultMap.put("fromDate", date_from);
		resultMap.put("toDate", date_to);
//		resultMap.put("userStore",channelStoreList);
		resultMap.put("channel", propertyList);
		resultDeal(request, response, resultMap);
	}

	@Override
	public byte[] downloadInvDelReport(String param, UserSessionBean user) {
		FormReportBean formReportBean = JsonUtil.jsonToBean(param, FormReportBean.class);
        formReportBean.setFromDateGMT(DateTimeUtil.getGMTTimeFrom(formReportBean.getFromDate(), user.getTimeZone()));
        formReportBean.setToDateGMT(DateTimeUtil.getGMTTimeTo(formReportBean.getToDate(), user.getTimeZone()));
		List<FormReportBean> formReportBeans  = reportDao.getInvDelList(formReportBean);
		return wmsInvDetReportService.createReportByte(formReportBeans, formReportBean);
	}

	private void resultDeal(HttpServletRequest request, HttpServletResponse response, Map<String, Object> resultMap){
		AjaxResponseBean result = new AjaxResponseBean();
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
	}

}
