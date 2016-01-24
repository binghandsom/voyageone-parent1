package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.CoreConstants;
import com.voyageone.core.MessageConstants;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.wms.WmsCodeConstants;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.dao.ItemDao;
import com.voyageone.wms.dao.ReservationDao;
import com.voyageone.wms.dao.ReservationLogDao;
import com.voyageone.wms.dao.ReturnDao;
import com.voyageone.wms.formbean.FormReturn;
import com.voyageone.wms.service.WmsReturnService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service.impl]  
 * @ClassName    [WmsReturnServiceImpl]   
 * @Description  [return服务类接口实现类]   
 * @Author       [sky]   
 * @CreateDate   [20150427]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Service
public class WmsReturnServiceImpl implements WmsReturnService {

	private static Log logger = LogFactory.getLog(WmsReturnServiceImpl.class);

	@Autowired
	private ReturnDao returnDao;

	@Autowired
	private ReservationLogDao reservationLogDao;

	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private ItemDao itemDao;

//	@Override
//	public void changeStatus(HttpServletRequest request, HttpServletResponse response, String returnId) {
//		Map<String, Object> resultMap = new HashMap<>();
//		FormReturn formReturn = new FormReturn();
//		AjaxResponseBean result = new AjaxResponseBean();
//		UserSessionBean user = (UserSessionBean)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
//		formReturn.setReturn_id(returnId);
//		formReturn.setModifier(user.getUserName());
//		formReturn.setReturn_status("1");
//		boolean flag;
//        flag = returnDao.changeStatus(formReturn);
//		if(flag){
//			resultMap.put(WmsConstants.Common.RESMSG, "Change status successful!");
//		}else{
//			resultMap.put(WmsConstants.Common.RESMSG, "Change status failed!");
//		}
//		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
//		result.setResult(flag);
//		result.setResultInfo(resultMap);
//		result.writeTo(request, response);
//		logger.info(result.toString());
//	}

	@Override
	public void getOrderInfoByOrdNo(HttpServletRequest request, HttpServletResponse response, String orderNumber, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();


		FormReturn formReturn = new FormReturn();
		formReturn.setOrderChannelId(getOrderChanleList(user));
		formReturn.setOrder_num(orderNumber);

		// 根据传入的值来取得真正的orderNumber
		String order_nember = returnDao.getOrderNumber(formReturn);

		if (!StringUtils.isNullOrBlank2(order_nember)) {
			formReturn.setOrder_num(order_nember);
		}

		List<FormReturn> orderInfo;
		orderInfo = returnDao.getOrderInfoByOrdNo(formReturn);

		for (FormReturn returnInfo : orderInfo) {
			// 根据输入的条形码找到对应的UPC
			String Upc = itemDao.getUPC(returnInfo.getOrder_channel_id(), returnInfo.getBarCode());
			returnInfo.setUpc(Upc);
		}

		setDisplayInfo(orderInfo, user);

		resultMap.put("orderInfo", orderInfo);
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void insertReturnInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		AjaxResponseBean result = new AjaxResponseBean();
		UserSessionBean userInfo = (UserSessionBean) request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_INFO);
		String userName = userInfo.getUserName();
		List<FormReturn> list = JsonUtil.jsonToBeanList(JsonUtil.getJsonString(object), FormReturn.class);
		for(FormReturn formReturn : list){
			formReturn.setUser(userName);
		}
		result.setResult(returnDao.insertReturnInfo(list));
		result.writeTo(request, response);
		logger.info(result.toString());
	}

    @Override
	public void createReturnSession(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean userInfo) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		FormReturn formReturn = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormReturn.class);
		formReturn.setUser(userInfo.getUserName());
		//根据用户属性设置是否需要入库，是否同步标志
		if("1".equals(userInfo.getCompanyReturnType())){
			formReturn.setReturn_type("1");
		}else{
			formReturn.setReturn_type("0");
		}
		// 根据仓库判断库存是否需要管理
		StoreBean store = StoreConfigs.getStore(Long.valueOf(formReturn.getStore_id()));
		String inventory_manager = store.getInventory_manager();
		String synFlg = "0";
		if (StoreConfigEnums.Manager.NO.getId().equals(inventory_manager)) {
			synFlg = "1";
		}
        formReturn.setSyn_flg(synFlg);
		String returnSessionId = returnDao.createReturnSession(formReturn);
		resultMap.put("returnType", formReturn.getReturn_type());
		resultMap.put("returnSessionId", returnSessionId);
		result.setResult(StringUtils.isEmpty(returnSessionId));
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

	@Transactional
	@Override
	public void closeReturnSession(HttpServletRequest request, HttpServletResponse response,String sessionId, UserSessionBean userInfo, HttpSession session) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		boolean flag;
		FormReturn formReturn = new FormReturn();
		formReturn.setReturn_session_id(sessionId);
		setFormCommonValue(request, formReturn, userInfo);
		flag = returnDao.changeSessionStatus(formReturn);
		if(flag){
			//session里面的内容为空处理则无需处理reServation表里面的状态
			List<FormReturn> list = returnDao.getSessionInfo(formReturn);
			if(list.size() != 0){
				//修改reservation的状态
				flag = changeReservationStatus(formReturn);
				//状态修改成功后，记录reservationLog
				if(flag) {
					List<Long> reservationList = new ArrayList<>();

					for(FormReturn formReturnNew : list){

						// 将需要记入日志的ID保存
						reservationList.add(Long.valueOf(formReturnNew.getRes_id()));

					}
					reservationLogDao.insertReservationLog(reservationList, "Status change to : Return", userInfo.getUserName());
				}
			}
		}
		if(!flag){
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		resultMap.put(WmsConstants.Common.RESMSG, "CloseSession  successful!");
		resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

	@Override
	public void saveItemEdit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		String fromReturnStr = JsonUtil.getJsonString(object);
		FormReturn formReturn = JsonUtil.jsonToBean(fromReturnStr, FormReturn.class);
		formReturn.setUser(user.getUserName());
		boolean flag;
		try{
			 flag = returnDao.saveItemEdit(formReturn);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Save successful!");
		}else{
			resultMap.put(WmsConstants.Common.RESMSG, "Save Failed");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		new AjaxResponseBean().setResult(flag).setResultInfo(resultMap).writeTo(request, response);
	}

	@Override
	public void removeReturnInfo(HttpServletRequest request, HttpServletResponse response, String resId) {
		Map<String, Object> resultMap = new HashMap<>();
		boolean flag;
		try {
			flag = returnDao.removeReturnInfo(resId);
		}catch (Exception e){
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		if(flag){
			resultMap.put(WmsConstants.Common.RESMSG, "Remove successful");
		}else{
			resultMap.put(WmsConstants.Common.RESMSG, "Remove failed");
		}
		resultMap.put(WmsConstants.Common.SUCCESEFLG, flag);
		new AjaxResponseBean()
				.setResult(flag)
				.setResultInfo(resultMap)
				.writeTo(request, response);
	}

	@Override
	public void doSessionListSearch(HttpServletRequest request, HttpServletResponse response, Object object, HttpSession session, UserSessionBean user) {
		String formReturnJson = JsonUtil.getJsonString(object);
		FormReturn formReturn = JsonUtil.jsonToBean(formReturnJson, FormReturn.class);
		setFormCommonValue(request, formReturn, user);
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		//查询条件转化成格林威治时间
		changeDateTimeToGMT(formReturn, user);
		try{
			int total = returnDao.getSessionListSize(formReturn);
			resultMap.put("total", total);
			List<FormReturn> sessionInfoList = new ArrayList<>();
			if (PageUtil.pageInit(formReturn, total)) {
				sessionInfoList = returnDao.doSessionListSearch(formReturn);
				//结果集里面的日期格式化成本地时间
				setDisplayInfo(sessionInfoList, user);

			}
			resultMap.put("sessionInfoList", sessionInfoList);
			result.setResult(true);
		}catch(Exception e){
			result.setResult(false);
			logger.error(e);
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void SessionListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap,	HttpSession session, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		// 获取开始日期（当前日期的一个月前）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("fromDate", date_from);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("toDate", date_to);
		doGetComBoxInfo(request, response, paramMap, resultMap);
	}

	@Override
	public void getSessionInfo(HttpServletRequest request, HttpServletResponse response, String sessionId, HttpSession session, UserSessionBean user) {
		AjaxResponseBean result = new AjaxResponseBean();
		Map<String, Object> resultListMap = new HashMap<>();
		List<FormReturn> sessionInfo;
		FormReturn formReturn = new FormReturn();
		formReturn.setReturn_session_id(sessionId);
		setFormCommonValue(request, formReturn, user);
		sessionInfo = returnDao.getSessionInfo(formReturn);
		setDisplayInfo(sessionInfo,user);
		result.setResult(false);
		result.setResult(true);
		resultListMap.put("returnList", sessionInfo);
		result.setResultInfo(resultListMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void doReturnListSearch(HttpServletRequest request, HttpServletResponse response, FormReturn formReturn, UserSessionBean user) {
		AjaxResponseBean result = new AjaxResponseBean();
		Map<String, Object> resultListMap = new HashMap<>();
		List<FormReturn> returnList = new ArrayList<>();
		setFormCommonValue(request, formReturn, user);
		changeDateTimeToGMT(formReturn, user);
		int total = getReturnListSize(formReturn);
		resultListMap.put("total", total);
		if (PageUtil.pageInit(formReturn, total)) {
			returnList = getReturnList(formReturn);
		}
		setDisplayInfo(returnList, user);
		result.setResult(true);
		resultListMap.put("returnList", returnList);
		result.setResultInfo(resultListMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void doNewSessionInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user) {
		FormReturn formReturnParam = new FormReturn();
		setFormCommonValue(request, formReturnParam, user);
		//获得用户下的仓库
		//List<ChannelStoreBean> storeList = user.getCompanyRealStoreList();
		ArrayList<ChannelStoreBean> storeList = new ArrayList<>();
		// 排除品牌方管理库存的仓库
		for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
			if (StoreConfigs.getStore(new Long(storeBean.getStore_id())).getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId())) {
				storeList.add(storeBean);
			}
		}

		//获取所有未关闭的session并且添加选项【NewSession】
		List<FormReturn> processingSessionAllList = getProcessingSessionName(formReturnParam);
		//获取用户所属的未关闭的session并且添加选项【NewSession】
		List<FormReturn> processingSessionList = new ArrayList<>();
		for (FormReturn formReturn : processingSessionAllList) {

			for (ChannelStoreBean channelStoreBean : storeList) {
				if (formReturn.getStore_id().equals(String.valueOf(channelStoreBean.getStore_id()))) {
					processingSessionList.add(formReturn);
					break;
				}
			}
		}

		setDisplayInfo(processingSessionList, user);
		FormReturn formReturn = new FormReturn();
		formReturn.setReturn_session_id(null);
		formReturn.setCreated_local("NewSession");
		processingSessionList.add(0, formReturn);

		//获取下拉框内容
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		for(String strkey : paramMap.keySet()){
			List<MasterInfoBean> list = Type.getMasterInfoFromId(Integer.parseInt(paramMap.get(strkey)), false);
			resultMap.put(strkey, list);
		}

		//处理结果
		resultMap.put("userStore", storeList);
		resultMap.put("processingSessionList", processingSessionList);
		resultMap.put("returnType", user.getCompanyReturnType());
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

	@Override
	public void doGetReceivedFromItemByStoreId(HttpServletRequest request, HttpServletResponse response, int storeId, UserSessionBean user) {
		//获取下拉框内容
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		List<ChannelStoreBean> storeList = user.getCompanyRealStoreList();
		String store_location = "";
		for(ChannelStoreBean channelStoreBean : storeList){
			if(storeId == channelStoreBean.getStore_id()){
				store_location = channelStoreBean.getStore_location();
				break;
			}
		}
		String lang;
		//0：国外仓库；1：中国仓库
		if(StoreConfigEnums.Location.CN.getId().equals(store_location)){
			lang = Constants.LANGUAGE.CN;
		}else{
			lang =  Constants.LANGUAGE.EN;
		}
		List<MasterInfoBean>  receivedFromList = Type.getMasterInfoFromId(TypeConfigEnums.MastType.returnExpress.getId(), false, lang);
		resultMap.put("receivedFromList", receivedFromList);
        result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
	}

    @Override
    public void getReturnType(HttpServletRequest request, HttpServletResponse response, UserSessionBean user) {
        Map<String, Object> resultMap = new HashMap<>();
        AjaxResponseBean result = new AjaxResponseBean();
        resultMap.put("returnType", user.getCompanyReturnType());
        result.setResultInfo(resultMap);
        result.writeTo(request, response);
        logger.info(result.toString());
    }

    //将查询条件中的时间字段转化成格林威治时间
	private void changeDateTimeToGMT(FormReturn formReturn, UserSessionBean user) {
		if(!StringUtils.isEmpty(formReturn.getUpdateTime_s())) {
			formReturn.setUpdateTime_s(DateTimeUtil.getGMTTimeFrom(formReturn.getUpdateTime_s(), user.getTimeZone()));
		}
		if(!StringUtils.isEmpty(formReturn.getUpdateTime_e())){
			formReturn.setUpdateTime_e(DateTimeUtil.getGMTTimeTo(formReturn.getUpdateTime_e(), user.getTimeZone()));
		}
		if(!StringUtils.isEmpty(formReturn.getCreateTime_s())){
			formReturn.setCreateTime_s(DateTimeUtil.getGMTTimeFrom(formReturn.getCreateTime_s(), user.getTimeZone()));
		}
		if(!StringUtils.isEmpty(formReturn.getCreateTime_e())){
			formReturn.setCreateTime_e(DateTimeUtil.getGMTTimeTo(formReturn.getCreateTime_e(), user.getTimeZone()));
		}
	}

	private void setDisplayInfo(List<FormReturn> list, UserSessionBean user) {
		for(FormReturn formReturn : list){
			//将时间类型的字段处理成本地时间（默认时间是格林威治时间）
			if(!StringUtils.isEmpty(formReturn.getCreated())){
				formReturn.setCreated_local(DateTimeUtil.getLocalTime(formReturn.getCreated(), user.getTimeZone()));
			}
			if(!StringUtils.isEmpty(formReturn.getModified())){
				formReturn.setModified_local(DateTimeUtil.getLocalTime(formReturn.getModified(), user.getTimeZone()));
			}

			// 货架取得
			String locationName = reservationDao.getLocationBySKU(formReturn.getOrder_channel_id(),formReturn.getSku(),formReturn.getStore_id());
			if (StringUtils.isNullOrBlank2(locationName)) {
				formReturn.setLocation_name(formReturn.getStore_name());
			} else {
				formReturn.setLocation_name(locationName.split(",")[0]);
			}

			// 物品状态取得
			if (!StringUtils.isNullOrBlank2(formReturn.getRes_status())) {
				formReturn.setRes_status_name(Codes.getCodeName(WmsCodeConstants.Reservation_Status.Name,formReturn.getRes_status()));
			}

		}
	}

	//设置ReturnForm公用信息
	private void setFormCommonValue(HttpServletRequest request, FormReturn formReturn, UserSessionBean user){
		//设置用户名称
		formReturn.setUser(user.getUserName());
		//用户对应的渠道
		formReturn.setOrderChannelId(getOrderChanleList(user));
		//设置用户语言
		String lang = (String)request.getSession().getAttribute(CoreConstants.VOYAGEONE_USER_LANG);
		formReturn.setLang(lang);
		//设置typeId
		Map<String, Integer> typeIdMap = new HashMap<>();
		typeIdMap.put(WmsConstants.Return.STATUS, TypeConfigEnums.MastType.returnStatus.getId());
		typeIdMap.put(WmsConstants.Return.REASON, TypeConfigEnums.MastType.returnReason.getId());
		typeIdMap.put(WmsConstants.Return.EXPRESS, TypeConfigEnums.MastType.returnExpress.getId());
		typeIdMap.put(WmsConstants.Return.SESSIONSTATUS, TypeConfigEnums.MastType.returnSessionStatus.getId());
		typeIdMap.put(WmsConstants.Return.CONDITION, TypeConfigEnums.MastType.returnCondition.getId());
		typeIdMap.put(WmsConstants.Return.RETURNTYPE, TypeConfigEnums.MastType.returnType.getId());
		formReturn.setTypeIdMap(typeIdMap);
		//returnType，是否需要同步标志
		if(user.getCompanyReturnType().equals("1")){
			formReturn.setReturn_type("1");
		}else{
			formReturn.setReturn_type("0");
		}
	}

	//获取用户对应的channel
	private List<String> getOrderChanleList(UserSessionBean user){
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		List<String> orderChannelIdList = new ArrayList<>();
		for(String propertyId : propertyPermissions.keySet()){
			orderChannelIdList.add(propertyId);
		}
		return orderChannelIdList;
	}

	//获取returnList
	private List<FormReturn> getReturnList(FormReturn formReturn) {
        return returnDao.getReturnList(formReturn);
	}

	//获取returnList总记录条数，供分页使用
	private int getReturnListSize(FormReturn formReturn) {
        return returnDao.getReturnListSize(formReturn);
	}

	//获取未closeSession的session
	private List<FormReturn> getProcessingSessionName(FormReturn formReturn) {
        return returnDao.getProcessinSessionName(formReturn);
	}

	//更改reservation表的记录状态
	private boolean changeReservationStatus(FormReturn formReturn) {
		//检查tt_reservation的本条记录是否已经closeDay,如果close_day_flg为1才能操作
		if(!returnDao.checkCloseDayFlg(formReturn.getReturn_session_id())){
			return returnDao.changeReservationStatus(formReturn);
		}else{
			throw new BusinessException(MessageConstants.ComMsg.SYSTEM_ERR);
		}
	}

	/**
	 * @Description 【newSession页面】获取下拉框内容,paramMap格式{"typeName":"typeId;hasAllOptionFlg"}
	 *               typeName：contorl里面接受数据的名称；
	 *               typeId：com_mt_value里面的typeId;
	 *               hasAllOptionFlg：是否要ALL选项,默认true
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @param paramMap 参数Map
	 * @create 20150507
	 */
	private void doGetComBoxInfo(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> resultMap) {
		AjaxResponseBean result = new AjaxResponseBean();
		for(String strkey : paramMap.keySet()){
			String[] strArray = paramMap.get(strkey).split(";");
			String typeId;
			boolean hasAllOption = true;
			if(strArray.length == 1){
				typeId = strArray[0];
			}else if(strArray.length == 2){
				typeId = strArray[0];
				hasAllOption = Boolean.parseBoolean(strArray[1]);
			}else{
				return;
			}
			List<MasterInfoBean> list = Type.getMasterInfoFromId(Integer.parseInt(typeId), hasAllOption);
			resultMap.put(strkey, list);
		}
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

}
