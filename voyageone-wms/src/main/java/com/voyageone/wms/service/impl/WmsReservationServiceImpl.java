package com.voyageone.wms.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.taobao.top.schema.Util.StringUtil;
import com.voyageone.base.BaseAppService;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.Type;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.ajax.AjaxResponseBean;
import com.voyageone.core.modelbean.ChannelShopBean;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.PermissionBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.core.util.PageUtil;
import com.voyageone.wms.WmsCodeConstants;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.dao.BackorderDao;
import com.voyageone.wms.dao.ReservationLogDao;
import com.voyageone.wms.modelbean.ReservationBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voyageone.wms.dao.ReservationDao;
import com.voyageone.wms.formbean.FormReservation;
import com.voyageone.wms.service.WmsReservationService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.service.impl]
 * @ClassName    [WmsReservationServiceImpl]
 * @Description  [ReservationLog Service类]   
 * @Author       [sky]
 * @CreateDate   [20150421]
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]
 * @Version      [v1.0]
 */
@Service
public class WmsReservationServiceImpl implements WmsReservationService {

	private static Log logger = LogFactory.getLog(WmsReservationServiceImpl.class);

	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private ReservationLogDao reservationLogDao;

	@Autowired
	private BackorderDao backOrderDao;

	@Autowired
	private HttpServletRequest request;

	@Override
	public void getReservationLogList(FormReservation formReservationPram, UserSessionBean user, HttpServletResponse response) {
		Map<String, Object> resultMap = new HashMap<>();
		int row = formReservationPram.getRows();
		formReservationPram.setRows(0);
		//获取总数量
		int total = (reservationDao.getReservationLog(formReservationPram)).size();

		formReservationPram.setRows(row);
		List<FormReservation> list = new ArrayList<>();
		if(PageUtil.pageInit(formReservationPram, total)) {
			list = reservationDao.getReservationLog(formReservationPram);
		}
		//得到reservationLog对象后，修改状态显示的状态
		for(FormReservation formReservation : list){
			setReservationDisplayInfo(formReservation, user);
		}
		resultMap.put("reservationList", list);
		resultMap.put("total", total);
		doProcessRes(resultMap, request, response);
	}

	@Override
	public void getReservationList(HttpServletRequest request, HttpServletResponse response, FormReservation formReservation, UserSessionBean user) {
		if(!StringUtils.isEmpty(formReservation.getCart_id()) && !formReservation.getCart_id().equals("0")){
			formReservation.setOrder_channel_id(formReservation.getCart_id().split("-")[0]);
			formReservation.setCart_id(formReservation.getCart_id().split("-")[1]);
		}
        //设置公用属性
        setFormCommonValue(formReservation, user);
		AjaxResponseBean result = new AjaxResponseBean();
		Map<String, Object> reservationListMap = new HashMap<>();
		List<FormReservation> reservationList = new ArrayList<>();
		//将查询条件中的事件修改为格林威治时间
		changeDateTimeToGMT(formReservation, user);
        //特殊处理isLock锁单的查询条件，历史原因导致需要特殊处理
        if("YES".equalsIgnoreCase(formReservation.getIsLock())){
            formReservation.setIsLock("1");
        }else if("NO".equalsIgnoreCase(formReservation.getIsLock())) {
            formReservation.setIsLock("0");
        }
		int total = reservationDao.getReservationListSize(formReservation);
		reservationListMap.put("total", total);
		// 分页处理
		if (PageUtil.pageInit(formReservation, total)) {
			reservationList = reservationDao.getReservationInfo(formReservation);

			for(FormReservation reservation : reservationList){
				// 是否允许变更仓库的判断
				String change_store_flg = reservationDao.getChangeStoreFlg(reservation);
				reservation.setChange_store_flg(StringUtils.null2Space2(change_store_flg));
				setReservationDisplayInfo(reservation, user);
			}
		}
		result.setResult(true);
		//将对象中的要展示到页面的时间从格林威治时间修改为本地时间
		setLocalDateTime(reservationList, user);
		reservationListMap.put("reservationInfo", reservationList);
		result.setResultInfo(reservationListMap);
		result.writeTo(request, response);
//		logger.info(result.toString());
	}

    private void setFormCommonValue(FormReservation formReservation, UserSessionBean user) {
        formReservation.setCompanyOrdChannelList(user.getChannelList());
        formReservation.setCompanyStoreList(user.getCompanyStoreList());
		formReservation.setCompanyShopList(user.getCompanyShopList());
    }

    @Override
	public void doReservationListInit(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, UserSessionBean user) {
		Map<String, Object> reservationListMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		//获取渠道
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		List<PermissionBean> propertyList = new ArrayList<>();
		for(String propertyId : propertyPermissions.keySet()){
			propertyList.add(propertyPermissions.get(propertyId));
		}
        PermissionBean pb = new PermissionBean();
        pb.setPropertyId("");
        pb.setPropertyName("ALL");
        propertyList.add(0, pb);
		for(String strkey : paramMap.keySet()){
			List<MasterInfoBean> list = Type.getMasterInfoFromId(Integer.parseInt(paramMap.get(strkey)), true);
			reservationListMap.put(strkey, list);
		}
		// 获取开始日期（当前日期的一个月前）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		reservationListMap.put("fromDate", date_from);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		reservationListMap.put("toDate", date_to);
		//获得shopList
		List<ChannelShopBean> shopList = new ArrayList<>();
		ChannelShopBean channelShopBean = new ChannelShopBean();
		channelShopBean.setShop_id("0");
		channelShopBean.setShop_name("ALL");
		shopList.add(channelShopBean);
		shopList.addAll(user.getCompanyShopList());
		//获得storeId
		ArrayList<ChannelStoreBean> channelStoreList = new ArrayList<>();
		ChannelStoreBean channelStoreBean = new ChannelStoreBean();
		channelStoreBean.setStore_id(0);
		channelStoreBean.setStore_name("ALL");
		channelStoreList.add(channelStoreBean);
		channelStoreList.addAll(user.getCompanyStoreList());
        result.setResult(true);
		reservationListMap.put("userStore", channelStoreList);
		reservationListMap.put("shopList", shopList);
		reservationListMap.put("propertyPermissions", propertyList);
		reservationListMap.put("defaultStatus", WmsCodeConstants.Reservation_Status.Open);
		result.setResultInfo(reservationListMap);
		result.writeTo(request, response);
//		logger.info(result.toString());
	}

	@Override
	public void popInventoryInit(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		FormReservation formReservation = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormReservation.class);
		formReservation = reservationDao.getProductInfo(formReservation);
		if(formReservation == null){
			result.setResult(false);
			resultMap.put(WmsConstants.Common.RESMSG, "Can't find the sku!");
			resultMap.put(WmsConstants.Common.SUCCESEFLG, false);
		}else{
			resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		}
		// 获取渠道
		HashMap<String, PermissionBean> propertyPermissions = user.getPropertyPermissions();
		List<PermissionBean> propertyList = propertyPermissions.keySet().stream().map(propertyPermissions::get).collect(Collectors.toList());

		result.setResult(true);
		resultMap.put("productInfo", formReservation);
		resultMap.put("channel", propertyList);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void getInventoryInfo(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		FormReservation formReservation_param = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormReservation.class);
		setFormCommonValue(formReservation_param, user);
		//获取仓库列表
		List<FormReservation> formReservation_store = reservationDao.getStoreByChannelId(formReservation_param);
		//获取产品列表
        List<FormReservation> formReservation_inventory = new ArrayList<>();
        //求总数设置row为0,sql里面row为0的情况，不加条数限制
        int row = formReservation_param.getRows();
        formReservation_param.setRows(0);
        int total = reservationDao.getProductInventoryCount(formReservation_param);
        formReservation_param.setRows(row);
        if(PageUtil.pageInit(formReservation_param, total)) {
            formReservation_inventory = reservationDao.getProductInventory(formReservation_param);
        }
		//过滤sku为空的记录（由于使用仓库做主表做的左连接可能导致sku为null的记录，需要过滤掉）
		for(int i = 0; i < formReservation_inventory.size(); i++ ){
			if(StringUtils.isEmpty(formReservation_inventory.get(i).getSku())){
				formReservation_inventory.remove(i);
				total = total - 1;
			}
		}
		resultMap.put("total", total);
		//处理列表结果集
		getTableContent(formReservation_inventory);
		resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		result.setResult(true);
		resultMap.put("storeList", formReservation_store);
		resultMap.put("inventoryList", formReservation_inventory);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

	@Override
	public void resetInventoryInfo(Map<String, Object> paramMap, HttpServletResponse response) {

		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();

		// 取得画面的各个参数
		String order_channel_id = (String) paramMap.get("order_channel_id");
		String sku = (String) paramMap.get("sku");

		logger.info("渠道："+order_channel_id +"，需要重新推送至各平台的库存SKU："+sku);
		reservationDao.deleteInventoryInfo(order_channel_id, sku);

		resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);

	}

	@Override
	public void getSkuHisList(HttpServletRequest request, HttpServletResponse response, Object object, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		FormReservation formReservation_param = JsonUtil.jsonToBean(JsonUtil.getJsonString(object), FormReservation.class);
		changeDateTimeToGMT(formReservation_param, user);
		//获取最新库存
		int curQty;
        curQty = reservationDao.getCurQty(formReservation_param);

		int row = formReservation_param.getRows();
		formReservation_param.setRows(0);
		//获取列表总数
		int total = reservationDao.getSkuHisListCount(formReservation_param);

		//获取产品库存变更列表
		formReservation_param.setRows(row);
		List<FormReservation> formReservation_skuHisList = new ArrayList<>();
		if(PageUtil.pageInit(formReservation_param, total)){
			formReservation_skuHisList = reservationDao.getSkuHisList(formReservation_param);
		}

		setLocalDateTime(formReservation_skuHisList, user);
        //处理库存变化来源
        procTransferOrigin(formReservation_skuHisList);
		resultMap.put(WmsConstants.Common.SUCCESEFLG, true);
		result.setResult(true);
		resultMap.put("curQty", curQty);
		resultMap.put("skuHisList", formReservation_skuHisList);
		resultMap.put("total", total);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		//logger.info(result.toString());
	}

    @Override
	public void initSkuHisList(HttpServletRequest request, HttpServletResponse response, UserSessionBean user) {
		Map<String, Object> resultMap = new HashMap<>();
		AjaxResponseBean result = new AjaxResponseBean();
		// 获取开始日期（当前日期的一个月前）
		String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("fromDate", date_from);
		// 获取结束日期（当前日期）
		String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
		resultMap.put("toDate", date_to);
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

    /**
     * 【skuHisList 页面】 viw_wms_transfer_item里面的transfer_origin;
     *                    1) transfer_origin=0（正常Transfer）时，用transfer_type对应的Name来表示;
     *                    2) transfer_origin=0以外（模拟Transfer）时，用transfer_origin对应的Name来表示
     * @param formReservation_skuHisList 超卖参数
     */
    private void procTransferOrigin(List<FormReservation> formReservation_skuHisList) {
        for(FormReservation fr : formReservation_skuHisList){
            if("0".equals(fr.getTransfer_origin())){
                fr.setOrigin(Type.getTypeName(TypeConfigEnums.MastType.transferType.getId(), fr.getTransfer_type()));
            }else{
                fr.setOrigin(Type.getTypeName(TypeConfigEnums.MastType.transferOrigin.getId(), fr.getTransfer_origin()));
            }
        }
    }

	private void getTableContent(List<FormReservation> formReservation_inventory) {
		for(FormReservation formReservation : formReservation_inventory){
			String[] inventory_Qtys = formReservation.getInventory_qty().split(",");
			String[] openRes_qtys = formReservation.getOpenRes_qty().split(",");
			List<Map<String, String>> list = new ArrayList<>();
			int i = 0;
			int totalInventory = 0, totalOpenRes = 0;
			for(String inventoryQty : inventory_Qtys){
				//合计处理
				totalInventory = totalInventory + Integer.parseInt(inventoryQty);
				totalOpenRes = totalOpenRes + Integer.parseInt(openRes_qtys[i]);
				//将所有仓库的qty存入List，这里使用tempMap的原因是由于页面ng-repear限制使用对象类型而不是基础类型
				Map<String, String> tempMap = new HashMap<>();
				String openResQtyStr = "";
				if(Integer.parseInt(openRes_qtys[i]) > 0){
					openResQtyStr = "(-" + openRes_qtys[i]+")";
				}
				tempMap.put("inventoryDel", inventoryQty + openResQtyStr);
				list.add(tempMap);
				i++;
			}
			if(totalOpenRes + Integer.parseInt(formReservation.getNewOrd_qty() + formReservation.getInProcess_qty()) > 0){
				formReservation.setTotal(totalInventory + "(-"
										 + (totalOpenRes  //open reserve的订单
									     + Integer.parseInt(formReservation.getNewOrd_qty()) //美国系统产生的订单，还未到Se
										 + formReservation.getInProcess_qty())+")"); //到Se 还未到Wms的订单
			}else {
				formReservation.setTotal(totalInventory + "");
			}
			formReservation.setStoreInventory(list);
		}
	}

	//将时间类型的字段处理成本地时间（默认参数时间是格林威治时间）
	private void setLocalDateTime(List<FormReservation> sessionList, UserSessionBean user) {
		for(FormReservation formReservation : sessionList){
			if(!StringUtils.isEmpty(formReservation.getOrderDateTime())){
				formReservation.setOrderDateTime(DateTimeUtil.getLocalTime(formReservation.getOrderDateTime(), user.getTimeZone()));
			}
			if(!StringUtils.isEmpty(formReservation.getCreated())){
				formReservation.setCreated(DateTimeUtil.getLocalTime(formReservation.getCreated(), user.getTimeZone()));
			}
		}
	}

	//将查询条件中的时间字段转化成格林威治时间
	private void changeDateTimeToGMT(FormReservation formReservation, UserSessionBean user) {
		if(!StringUtils.isEmpty(formReservation.getOrderDateTime_s())) {
			formReservation.setOrderDateTime_s(DateTimeUtil.getGMTTimeFrom(formReservation.getOrderDateTime_s(), user.getTimeZone()));
		}
		if(!StringUtils.isEmpty(formReservation.getOrderDateTime_e())){
			formReservation.setOrderDateTime_e(DateTimeUtil.getGMTTimeTo(formReservation.getOrderDateTime_e(), user.getTimeZone()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(formReservation.getFromDate())){
			formReservation.setFromDate(DateTimeUtil.getGMTTimeFrom(formReservation.getFromDate(), user.getTimeZone()));
		}
		if(!org.springframework.util.StringUtils.isEmpty(formReservation.getToDate())){
			formReservation.setToDate(DateTimeUtil.getGMTTimeTo(formReservation.getToDate(), user.getTimeZone()));
		}
	}

	/**
	 * 【reservationList页面】 更新状态
	 * @param paramMap 超卖参数
	 * @param user 用户登录信息
	 * @return FormReservation 物品记录
	 */
	@Transactional
	@Override
	public FormReservation doPopChangeReservationChange(Map<String, Object> paramMap, UserSessionBean user) {

		// 取得画面的参数
		String reservationID = (String) paramMap.get("reservationID");
		String changeKind = (String) paramMap.get("changeKind");
		String notes = (String) paramMap.get("notes");
		String store = (String) paramMap.get("store");
		String modified = (String) paramMap.get("modified");

		String changLog = "";
		String storeName = "";
		String statusName;
		logger.info("reservationID：" + reservationID  + "，changeKind：" + changeKind  + "，store：" + store  + "，modified：" + modified  + "，notes：" + notes);

		// 更新tt_reservation的状态
		ReservationBean reservationInfo = new ReservationBean();
		reservationInfo.setId(Long.valueOf(reservationID));

		reservationInfo.setNote(notes);
		reservationInfo.setUpdate_time(modified);
		reservationInfo.setUpdate_person(user.getUserName());

		if (WmsConstants.ChangeKind.Store.equals(changeKind)) {

			StoreBean storeBean = StoreConfigs.getStore(Long.valueOf(store));
			if (storeBean != null) {
				storeName = storeBean.getStore_name();

				// 跨境电商并且是国内仓库时，为了和Synship系统统一，仓库名定为TM
				if (storeBean.getStore_location().equals(StoreConfigEnums.Location.CN.getId()) &&
						ChannelConfigs.getVal1(storeBean.getOrder_channel_id(), ChannelConfigEnums.Name.sale_type).equals(ChannelConfigEnums.Sale.CN.getType())) {
					storeName = WmsConstants.StoreName.CN;
				}

			}

			reservationInfo.setStore_id(Long.valueOf(store));
			reservationInfo.setStore(storeName);

			changLog = "Reservation [ " + reservationID + " ] 's Store changed to：" + storeName;
		}
		else if (WmsConstants.ChangeKind.Note.equals(changeKind)) {
			changLog = "Reservation [ " + reservationID + " ] 's Note changed";
		}
		else if (WmsConstants.ChangeKind.Cancelled.equals(changeKind)) {
			reservationInfo.setStatus(WmsCodeConstants.Reservation_Status.Cancelled);
			statusName = Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(),reservationInfo.getStatus());
			changLog = "Reservation [ " + reservationID + " ] 's Status changed to：" + statusName;
		}
		else if (WmsConstants.ChangeKind.BackOrdered.equals(changeKind)) {
			reservationInfo.setStatus(WmsCodeConstants.Reservation_Status.BackOrdered);
			statusName = Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(),reservationInfo.getStatus());
			changLog = "Reservation [ " + reservationID + " ] 's Status changed to：" + statusName;
		}
		else if (WmsConstants.ChangeKind.Open.equals(changeKind)) {
			reservationInfo.setStatus(WmsCodeConstants.Reservation_Status.Open);
			statusName = Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(),reservationInfo.getStatus());
			changLog = "Reservation [ " + reservationID + " ] 's Status changed to：" + statusName;
		}
		else if (WmsConstants.ChangeKind.BackOrderConfirmed.equals(changeKind)) {
			reservationInfo.setStatus(WmsCodeConstants.Reservation_Status.BackOrderConfirmed);
			statusName = Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(),reservationInfo.getStatus());
			changLog = "Reservation [ " + reservationID + " ] 's Status changed to：" + statusName;
		}
		logger.info(changLog);

		int updateResult = reservationDao.changeReservation(reservationInfo);

		// 更新失败的场合，直接抛出错误
		if (updateResult == 0) {
			throw new BusinessException(WmsMsgConstants.RsvListMsg.UPDATE_ERROR, reservationID);
		}

		// 超卖的场合，插入backorder记录
		if (WmsCodeConstants.Reservation_Status.BackOrdered.equals(reservationInfo.getStatus())) {

			// 取得相关仓库的库存管理类型
			String inventory_manager = backOrderDao.getStoreInventoryManager(Long.valueOf(reservationID));

			// 库存管理的场合，将超卖记录插入到wms_bt_backorder中
			if (!StringUtils.isNullOrBlank2(inventory_manager) && inventory_manager.equals(StoreConfigEnums.Manager.YES.getId())) {

				// 插入wms_bt_backorder(插入前判断是否已有相同记录)
				int isExistsSku = backOrderDao.isExistsSkuByReservationID(Long.valueOf(reservationID));

				if (isExistsSku == 0) {
					logger.info("插入wms_bt_backorder：" + reservationID);
					backOrderDao.insertSkuByReservationID(Long.valueOf(reservationID), user.getUserName());
				}else {
					logger.info("wms_bt_backorder中已存在，无需重复插入：" + reservationID);
				}
			}else {
				logger.info("库存由品牌方自行管理，无需插入wms_bt_backorder：" + reservationID);
			}
		}

		// Open的场合，删除backorder记录（暂不删除，由人工判断）
//		if (WmsCodeConstants.Reservation_Status.Open.equals(reservationInfo.getStatus())) {
//			 backOrderDao.deleteSkuByReservationID(Long.valueOf(reservationID), user.getUserName());
//		}

		// 插入ReservationLog
		List<Long> reservationList = new ArrayList<>();
		reservationList.add(reservationInfo.getId());
		reservationLogDao.insertReservationLog(reservationList, changLog + "\r\n" + notes , user.getUserName());

		// 返回画面用(刷新纪录)
		FormReservation formReservation =reservationDao.getReservation(reservationInfo.getId()) ;

		setReservationDisplayInfo(formReservation, user);

		return formReservation;
	}

	/**
	 * 【reservationList页面】 选择画面初期化
	 * @param paramMap 参数
	 * @return Map 画面初期化内容
	 */
	@Override
	public Map<String, Object> popChangeReservationInit(Map<String, Object> paramMap) {

		// 取得画面的参数
		String orderChannelId = (String) paramMap.get("orderChannelId");
		String storeId = (String) paramMap.get("storeId");

		Map<String, Object> resultMap = new HashMap<>();

		//获取渠道可选择仓库（虚拟仓库不能选择）
		List<StoreBean> storeList = StoreConfigs.getChannelStoreList(orderChannelId, false, false);

		List<StoreBean> storeChangeList = new ArrayList<>();
		//排除销售外的仓库
		for (StoreBean storeBean : storeList) {
			if (storeBean.getIs_sale().equals("1")) {
				storeChangeList.add(storeBean);
			}
		}

		//如果现有仓库在渠道仓库中不存在，或者是虚拟仓库，则排除此仓库
		StoreBean storeBean = StoreConfigs.getStore(orderChannelId, Long.valueOf(storeId));
		if (storeBean == null || storeBean.getStore_kind().equals(StoreConfigEnums.Kind.VIRTUAL.getId())) {
			storeId = "";
		}

		resultMap.put("storeList", storeChangeList);
		resultMap.put("storeId", storeId);

		return resultMap;
	}

	/**
	 * 对于一些画面表示用项目进行设置
	 * @param reservation 抽出的捡货记录
	 * @param user 用户登录信息
	 */
	private void setReservationDisplayInfo(FormReservation reservation, UserSessionBean user) {

		// 订单渠道
		reservation.setOrder_channel_name(StringUtils.isNullOrBlank2(reservation.getOrder_channel_id()) ? "" : ChannelConfigs.getChannel(reservation.getOrder_channel_id()).getName());

		// 仓库名
		reservation.setStore_name(StringUtils.isNullOrBlank2(reservation.getStore_id()) ? "" : StoreConfigs.getStore(Long.valueOf(reservation.getStore_id())).getStore_name());

		// 物品状态名
		reservation.setRes_status_name(StringUtils.isNullOrBlank2(reservation.getRes_status_id()) ? "" : Type.getTypeName(TypeConfigEnums.MastType.reservationStatus.getId(), reservation.getRes_status_id()));

		// 创建时间（本地时间）
		reservation.setCreated_local(StringUtils.isNullOrBlank2(reservation.getCreated()) ? "" : DateTimeUtil.getLocalTime(reservation.getCreated(), user.getTimeZone()));

		// 更新时间（本地时间）
		reservation.setModified_local(StringUtils.isNullOrBlank2(reservation.getModified()) ? "" : DateTimeUtil.getLocalTime(reservation.getModified(), user.getTimeZone()));

		// 下单时间（本地时间）
		reservation.setOrderDateTime_local(StringUtils.isNullOrBlank2(reservation.getOrderDateTime()) ? "" : DateTimeUtil.getLocalTime(reservation.getOrderDateTime(), user.getTimeZone()));

	}

	/**
	 * 处理检索结果
	 * @param resultMap 结果集Map
	 * @param response HttpServletResponse
	 * @create 20150702
	 */
	private void doProcessRes(Map<String,Object> resultMap, HttpServletRequest request, HttpServletResponse response) {
		AjaxResponseBean result = new AjaxResponseBean();
		result.setResult(true);
		result.setResultInfo(resultMap);
		result.writeTo(request, response);
		logger.info(result.toString());
	}

}
