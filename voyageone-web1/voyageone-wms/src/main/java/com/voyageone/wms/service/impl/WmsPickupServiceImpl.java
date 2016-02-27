package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.*;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.configs.beans.MasterInfoBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JsonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.core.modelbean.UserSessionBean;
import com.voyageone.wms.WmsCodeConstants;
import com.voyageone.wms.WmsConstants;
import com.voyageone.wms.WmsConstants.ReportPickupItems;
import com.voyageone.wms.WmsConstants.ReportSetting;
import com.voyageone.wms.WmsMsgConstants;
import com.voyageone.wms.dao.ItemDao;
import com.voyageone.wms.dao.ReservationDao;
import com.voyageone.wms.dao.ReservationLogDao;
import com.voyageone.wms.dao.TrackingInfoDao;
import com.voyageone.wms.formbean.FormPickUpLabelBean;
import com.voyageone.wms.formbean.FormPickupBean;
import com.voyageone.wms.formbean.FormReservation;
import com.voyageone.wms.formbean.PickedInfoBean;
import com.voyageone.wms.service.WmsPickupService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/21/2015.
 *
 * @author Jack
 */
@Service
public class WmsPickupServiceImpl implements WmsPickupService {

	private final static Log logger = LogFactory.getLog(WmsPickupServiceImpl.class);

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private ReservationLogDao reservationLogDao;

    @Autowired
    private TrackingInfoDao trackingInfoDao;

    @Autowired
    private ItemDao itemDao;

    /**
     * 【捡货画面】初始化
     * @param user 用户登录信息
     * @return Map 画面初始化项目
     */
	@Override
	public Map<String, Object> doInit(UserSessionBean user,String reserveType) {
		Map<String, Object> resultMap = new HashMap<>();

        //获取Reservation状态、默认状态
        List<MasterInfoBean> statusList = Type.getMasterInfoFromId(MastType.reservationStatus.getId(), false);
        resultMap.put("status", statusList);

		//获取用户仓库
        ArrayList<ChannelStoreBean> channelStoreList = new ArrayList<>();

        ChannelStoreBean channelStoreBean = new ChannelStoreBean();
        channelStoreBean.setStore_id(0);
        channelStoreBean.setStore_name("ALL");

        channelStoreList.add(channelStoreBean);

        ArrayList<ChannelStoreBean> storeList = new ArrayList<>();
        // 根据reserveType来决定显示仓库
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
            StoreBean store = StoreConfigs.getStore(new Long(storeBean.getStore_id()));
            if (reserveType.equals(WmsConstants.ReserveType.PickUp)) {
                if (store.getIs_sale().equals(StoreConfigEnums.Sale.YES.getId())) {
                    storeList.add(storeBean);
                }
            }
             else  if (reserveType.equals(WmsConstants.ReserveType.Receive)) {
                if (store.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId())) {
                    storeList.add(storeBean);
                }
            }
        }

        channelStoreList.addAll(storeList);
		resultMap.put("storeList", channelStoreList);
        resultMap.put("selectStoreList",storeList);

        // 获取开始日期（当前日期的一个月前）
        String date_from = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.addMonths(DateTimeUtil.getDate(), -1), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
        resultMap.put("fromDate", date_from);

        // 获取结束日期（当前日期）
        String date_to = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
        resultMap.put("toDate", date_to);

        // 获取已发货报告开始日期\结束日期（当前日期）
        String reportDate = DateTimeUtil.parseStr(DateTimeUtil.getLocalTime(DateTimeUtil.getNow(), user.getTimeZone()), DateTimeUtil.DEFAULT_DATE_FORMAT);
        resultMap.put("reportFromDate", reportDate);
        resultMap.put("reportToDate", reportDate);

        // 获取相关渠道对应的扫描方式
        List<String> orderChannelList = user.getChannelList();
        String labelPrint = "";
        String permit = "";
        String pickupType = "";
        String pickupTypeName = "";
        String pickupStatus = "";
        String pickupPort = "";
        String relabelType = "";
        String relabelTypeName = "";
        String relabelStatus = "";
        String relabelPort = "";
        if (orderChannelList.size() > 0){
            if (reserveType.equals(WmsConstants.ReserveType.PickUp)) {
                labelPrint = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_label_print);
                permit = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_permit);
                pickupType = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_type);
                pickupTypeName = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_type, pickupType);
                pickupStatus = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_status);
                pickupPort = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.pickup_status, pickupStatus);
                relabelType = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.relabel_type);
                relabelTypeName = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.relabel_type, relabelType);
                relabelStatus = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.relabel_status);
                relabelPort = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.relabel_status, relabelStatus);
            }
            else  if (reserveType.equals(WmsConstants.ReserveType.Receive)) {
                labelPrint = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_label_print);
                permit = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_permit);
                pickupType = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_type);
                pickupTypeName = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.receive_type, pickupType);
                pickupStatus = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_status);
                pickupPort = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.receive_status, pickupStatus);
                relabelType = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_relabel_type);
                relabelTypeName = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.receive_relabel_type, relabelType);
                relabelStatus = ChannelConfigs.getVal1(orderChannelList.get(0), ChannelConfigEnums.Name.receive_relabel_status);
                relabelPort = ChannelConfigs.getVal2(orderChannelList.get(0), ChannelConfigEnums.Name.receive_relabel_status, relabelStatus);
            }
        }
        resultMap.put("defaultStatus", StringUtils.isNullOrBlank2(pickupStatus)?WmsCodeConstants.Reservation_Status.Open:pickupStatus);
        // 如没有设定值，则默认允许打印拣货单
        resultMap.put("labelPrint", StringUtils.isNullOrBlank2(labelPrint)? ChannelConfigEnums.Print.YES.getIs() : labelPrint);
        resultMap.put("permit", permit);
        resultMap.put("pickupType", pickupType);
        resultMap.put("pickupTypeName", pickupTypeName);
        resultMap.put("pickupStatus", pickupStatus);
        resultMap.put("pickupPort", pickupPort);
        resultMap.put("relabelType", relabelType);
        resultMap.put("relabelTypeName", relabelTypeName);
        resultMap.put("relabelStatus", relabelStatus);
        resultMap.put("relabelPort", relabelPort);

        return resultMap;
	}

    /**
     * 【捡货画面】根据检索条件查询取得需要捡货的一览
     * @param paramMap 检索参数
     * @param user 用户登录信息
     * @return List<FormReservation> 捡货记录
     */
    @Override
    public List<FormReservation> getPickupInfo(Map<String, Object> paramMap, UserSessionBean user,String reserveType) {

        // 检索参数的取得和设置
        Map<String, Object> selectParams = getSelectParams(paramMap, user,reserveType);

        // 根据检索参数取得记录
        List<FormReservation> resultMap = reservationDao.getPickupInfo(selectParams);

        // 对于一些表示用项目进行设置
        for (FormReservation pickup : resultMap) {
            setReservationDisplayInfo(pickup, user);
        }

        // 返回抽出结果
        return resultMap;
    }

    /**
     * 【捡货页面】根据检索条件查询取得需要捡货的一览件数
     * @param paramMap 检索参数
     * @param user 用户登录信息
     * @return long 捡货记录的件数
     */
    @Override
    public int getPickupCount(Map<String, Object> paramMap, UserSessionBean user,String reserveType) {

        // 检索参数的取得和设置
        Map<String, Object> selectParams = getSelectParams(paramMap, user,reserveType);

        // 根据检索参数取得记录
        return reservationDao.getPickupCount(selectParams);

    }

    /**
     * 【捡货页面】根据扫描的内容取得相关记录
     * @param paramMap 扫描参数
     * @param user 用户登录信息
     * @return Map 扫描记录
     */
    @Transactional
    @Override
    public Map<String, Object> getScanInfo(Map<String, Object> paramMap, UserSessionBean user, String reserveType) {

        // 取得扫描内容
        String scanNo = (String) paramMap.get("scanNo");
        String scanMode = (String) paramMap.get("scanMode");
        String scanType = (String) paramMap.get("scanType");
        String scanTypeName = (String) paramMap.get("scanTypeName");
        String scanStatus = (String) paramMap.get("scanStatus");
        String scanPort = (String) paramMap.get("scanPort");
        String scanStore = (String) paramMap.get("scanStore");

        logger.info("scanType：" + scanType + "，scanTypeName：" + scanTypeName  + "，scanMode：" + scanMode  + "，ScanNo：" + scanNo + "，scanStore：" + scanStore + "，reserveType：" + reserveType);

        Map<String, Object> resultMap = new HashMap<>();

        // 用户所属仓库的设置
        List<ChannelStoreBean> channelStoreList = user.getCompanyRealStoreList();

        // 获取相关渠道
        List<String> orderChannelList = user.getChannelList();

        String searchNo = "";
        if (WmsConstants.ScanType.SCAN.equals(scanMode) && ChannelConfigEnums.Scan.RES.getType().equals(scanType)) {
            StoreBean store = StoreConfigs.getStore(Long.valueOf(scanStore));
            // 根据输入的条形码找到对应的UPC
            searchNo = itemDao.getUPC(store.getOrder_channel_id(), scanNo);
        }else if (ChannelConfigEnums.Scan.ORDER.getType().equals(scanType)){
            // 根据输入的订单号找到对应的内部订单号
            searchNo = reservationDao.getOrderNumber(orderChannelList, scanNo);
        }
        logger.info("searchNo："+searchNo);

        // 取得符合条件的记录
        List<FormPickupBean> scanInfoListALL = reservationDao.getScanInfo(scanMode, scanType, StringUtils.isNullOrBlank2(searchNo)?scanNo:searchNo, scanStatus, scanStore, channelStoreList, orderChannelList, reserveType);

        String StatusName = Type.getTypeName(MastType.reservationStatus.getId(),scanStatus);

        List<FormPickupBean> scanInfoList =new ArrayList<>();
        int intReserved = 0;
        int intCancelled = 0;
        boolean blnOpen = false;
        boolean blnCancel = false;
        List<String> reservationStatus = new ArrayList<>();

        for (FormPickupBean formPickupBean : scanInfoListALL) {

            String statusName = Codes.getCodeName(WmsCodeConstants.Reservation_Status.Name, formPickupBean.getStatus());

            // 品牌方订单号、品牌方物品ID的追加设定（用于画面显示用，告知仓库是扫描了哪个订单或物品）
            if (ChannelConfigEnums.Scan.ORDER.getType().equals(scanType)) {
                formPickupBean.setClient_id(formPickupBean.getClient_order_id());
            } else if (ChannelConfigEnums.Scan.ITEM.getType().equals(scanType)) {
                formPickupBean.setClient_id(formPickupBean.getTracking_number());
            }

            // 取得满足状态条件的记录
            if (formPickupBean.getStatus().equals(scanStatus)) {
                scanInfoList.add(formPickupBean);
            } else if (formPickupBean.getStatus().equals(WmsCodeConstants.Reservation_Status.Open)) {
                // Open的场合，SCAN时允许拣货收货（适用于品牌方发货时的快递信息没有及时同步到我们系统刀子状态不对的场景）
                if (WmsConstants.ScanType.SCAN.equals(scanMode) && ChannelConfigEnums.Scan.ORDER.getType().equals(scanType)) {
                    scanInfoList.add(formPickupBean);
                    blnOpen = true;
                }else{
                    reservationStatus.add(statusName);
                }
            } else if (formPickupBean.getStatus().equals(WmsCodeConstants.Reservation_Status.Cancelled)) {
                // 物品级别拣货时，忽略Cancel记录
                if (ChannelConfigEnums.Scan.RES.getType().equals(scanType)) {
                    intCancelled++;
                }
                // 订单级别收货时，由于货物可能已经实际发到仓库了，即使已经取消订单，也需要将取消物品打单
                else if (ChannelConfigEnums.Scan.ORDER.getType().equals(scanType)) {
                    if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
                        scanInfoList.add(formPickupBean);
                        blnCancel = true;
                    } else {
                        intCancelled++;
                    }
                }
                // 订单物品级别收货时，由于货物可能已经实际发到仓库了，即使已经取消订单，也需要将取消物品打单
                else if (ChannelConfigEnums.Scan.ITEM.getType().equals(scanType)) {
                    if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
                        scanInfoList.add(formPickupBean);
                    } else {
                        intCancelled++;
                    }
                }
            } else if (formPickupBean.getStatus().equals(WmsCodeConstants.Reservation_Status.Reserved)) {
                intReserved ++;
            } else if (!reservationStatus.toString().contains(statusName)) {
                reservationStatus.add(statusName);
            }
        }

        // 0件的场合，错误信息表示
        if (scanInfoList.size() == 0) {
            // 存在已拣货的状态时，请仓库重新Relabel
            if (intReserved > 0) {
                logger.info("该记录已拣货" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.RESERVED, scanTypeName, scanNo);
            }
            // 订单状态不允许拣货时的提示信息
            if (reservationStatus.size() > 0 ) {
                logger.info("该记录状态是" +reservationStatus.toString() + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_RECEIVE, scanTypeName, scanNo, reservationStatus.toString(), scanMode);
            }
            // 存在取消记录时，请仓库进行入库操作
            if (intCancelled > 0) {
                logger.info("该记录已被取消" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.CANCELLED);
            } else {
                logger.info("没有取得符合条件的记录" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_SCANNO, reserveType, scanTypeName, scanNo, StatusName);
            }
        }

        // 需要判断港口的场合，取得相关港口（BHFO、JE等渠道是由品牌方仓库发出后再收货的）但如果还存在Open、Cancel记录的话，说明可能是物流信息同步不及时造成的，则不做检查
        if (!StringUtils.isNullOrBlank2(scanPort) && blnOpen == false &&  blnCancel == false) {
            String port = reservationDao.getPort(scanInfoList.get(0).getSyn_ship_no(),scanInfoList.get(0).getId());

            if (!scanPort.equals(port)) {
                logger.info("没有取得符合条件的记录" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+  "，ScanPort：" + scanPort + "）");

                throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_SCANNO, reserveType, scanTypeName, scanNo, StatusName);
            }
        }

        // 得到订单对应的物品数
        List<String> orderSkuList = reservationDao.getOrderProductList(scanInfoList.get(0).getOrder_number(),scanType);
        logger.info("取得该订单的物品" + "（orderNumber：" + scanInfoList.get(0).getOrder_number()  + "，skuList：" + orderSkuList + "）");
        int productNum = orderSkuList.size();

        List<Long> reservationList = new ArrayList<>();
        List<Long> overSoldList = new ArrayList<>();
        // 对于一些表示用项目进行设置
        for (FormPickupBean pickup : scanInfoList) {
            setPickupDisplayInfo(pickup, user);

            // 保存物品数
            pickup.setQty(String.valueOf(productNum));

            // 将需要捡货的ID保存
            reservationList.add(pickup.getId());

            // 判断哪些是属于超卖仓库
            if (pickup.getStore_kind().equals(StoreConfigEnums.Kind.VIRTUAL.getId())) {
                overSoldList.add(pickup.getId());
            }

        }

//        // 存在超卖的场合，错误信息表示
//        if (overSoldList.size() > 0) {
//            logger.info("存在超卖的物品" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo  + "，ReservationID：" + overSoldList.toString() +  "）");
//            throw new BusinessException(WmsMsgConstants.PickUpMsg.OVER_SOLD, overSoldList.toString());
//        }

        String orderChannelId = scanInfoList.get(0).getOrder_channel_id();
        String shipChannel = scanInfoList.get(0).getShip_channel();
//        // 跨境电商的场合，发货渠道的判断
//        if (ChannelConfigEnums.Sale.CB.getType().equals(ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sale_type))) {
//            shipChannel = reservationDao.getShippingMethod(orderChannelId, scanInfoList.get(0).getOrder_number(), scanInfoList.get(0).getId());
//        }
//        // 国内电商的场合，默认发货渠道的取得
//        else  if (ChannelConfigEnums.Sale.CN.getType().equals(ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sale_type))) {
//            shipChannel = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.default_ship_channel);
//        }
        shipChannel = reservationDao.getShippingMethod(orderChannelId, scanInfoList.get(0).getOrder_number(), scanInfoList.get(0).getId());
        logger.info("发货渠道判定" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo  + "，ReservationID：" + reservationList.toString() + "，ShipChannel：" + shipChannel +  "）");

        // 根据发货渠道计算折扣
        BigDecimal price = new BigDecimal(scanInfoList.get(0).getOriginal_price());

        BigDecimal declareRate = ChannelConfigs.getDiscountRate(orderChannelId, shipChannel);

        if (declareRate == null) {
//            logger.info("未取得相关发货渠道的折扣" + "（OrderChannelId：" + orderChannelId  + "，ShipChannel：" + shipChannel  +  "）");
//            throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_DISCOUNT_RATE, orderChannelId, shipChannel);
            // 没有设定的场合，固定用1来计算
            declareRate = new BigDecimal("1");
        }
        price= price.multiply(declareRate);


        // 捡货时需要更新状态，重打面单时不需要更新状态
        String updateStatus = WmsCodeConstants.Reservation_Status.Reserved;

        // 设置捡货内容
        for (FormPickupBean formPickupBean : scanInfoList){
            // 发货渠道
            formPickupBean.setShip_channel(shipChannel);

            // 物品状态名
            formPickupBean.setStatus_name(Type.getTypeName(MastType.reservationStatus.getId(), updateStatus));

            // 更新时间（本地时间）
            formPickupBean.setModified_local(DateTimeUtil.getLocalTime(user.getTimeZone()));
        }

        // 设置捡货单的内容
        FormPickUpLabelBean pickupLabel = getPickupLabel(scanInfoList, scanType, scanNo);
        //设置skuList
        pickupLabel.setSkuList(skuListToString(orderSkuList));
        String printPickupLabel = "[" + JsonUtil.getJsonString(pickupLabel)  + "]";
        logger.info("捡货单内容取得：" + printPickupLabel);

        // 根据仓库判断库存是否需要管理
        String closeDayFlg = "";
        if (!StringUtils.isNullOrBlank2(scanStore)) {
            StoreBean store = StoreConfigs.getStore(Long.valueOf(scanStore));
            String inventory_manager = store.getInventory_manager();
            // 捡货时 需要判断closeDayFlg
            if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
                closeDayFlg = WmsConstants.CloseDayFlg.Process;
                if (StoreConfigEnums.Manager.NO.getId().equals(inventory_manager)) {
                    closeDayFlg = WmsConstants.CloseDayFlg.Done;
                }
            }
        }

        // 更新捡货物品的状态和发货渠道
        int resultUpdatePickup = reservationDao.updatePickupStatus(reservationList, scanStatus, updateStatus, shipChannel, price, closeDayFlg, user.getUserName(),scanType,scanMode);

        if (resultUpdatePickup == 0) {
            logger.info("捡货物品更新失败" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo  + "，ReservationID：" + reservationList.toString() +  "）");
            throw new BusinessException(WmsMsgConstants.RsvListMsg.UPDATE_ERROR, reservationList.toString());
        }

        // 插入ReservationLog
        reservationLogDao.insertReservationLog(reservationList, scanMode, user.getUserName());

        // 捡货时 插入TrackingInfo
        if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
            // 是否存在的检查，存在的场合不插入记录
            int trackingExists = trackingInfoDao.selectTrackingByStatus(scanInfoList.get(0).getSyn_ship_no(), WmsCodeConstants.Tracking_Info.Take);

            if (trackingExists == 0) {
                trackingInfoDao.insertTrackingByStatus(scanInfoList.get(0).getSyn_ship_no(), WmsCodeConstants.Tracking_Info.Take, 0, user.getUserName());
            }

            trackingInfoDao.insertTrackingInfo(reservationList, WmsCodeConstants.Tracking_Info.Reserved, user.getUserName());
        }

        // 扫描记录的抽出
        resultMap.put("pickupLabel",scanInfoList);
        resultMap.put("printPickupLabel",printPickupLabel);

        return resultMap;

    }

    /**
     * 【捡货画面】下载可捡货列表
     * @param type 下载类型
     * @param user 用户登录信息
     * @return ResponseEntity<byte[]> 可捡货列表
     */
    @Override
    public byte[] downloadPickup(String type, UserSessionBean user) {

        byte[] bytes = null;

        // 用户所属仓库的设置
        List<ChannelStoreBean> channelStoreList = user.getCompanyRealStoreList();

        // 获取相关渠道
        List<String> orderChannelList = user.getChannelList();

        // 可捡货状态
        String status = WmsCodeConstants.Reservation_Status.Open;

        // 取得符合条件的记录
        List<FormPickupBean> pickupList = new ArrayList<>();

        // 按照订单物品下载
        if (ReportPickupItems.Type.Reservation.equals(type)) {
            pickupList = reservationDao.downloadPickupItemsByReservation(status, channelStoreList, orderChannelList);
        }
        // 按照SKU下载
        else  if (ReportPickupItems.Type.SKU.equals(type)) {
            pickupList = reservationDao.downloadPickupItemsBySKU(status, channelStoreList, orderChannelList);
        }


        logger.info("可捡货清单件数：" + pickupList.size());
        try{

            // 报表模板名取得
            String templateFile = Properties.readValue(ReportSetting.WMS_REPORT_TEMPLATE_PATH) + ReportPickupItems.FILE_NAME + ".xls";

            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            HSSFWorkbook workbook = new HSSFWorkbook(templateInput);

            // 设置内容
            if (ReportPickupItems.Type.Reservation.equals(type)) {
                setPickupItemsByReservation(workbook, pickupList, user);
            }
            else  if (ReportPickupItems.Type.SKU.equals(type)) {
                setPickupItemsBySKU(workbook, pickupList, user);
            }

            // 出力内容
            ByteArrayOutputStream outDate = new ByteArrayOutputStream();
            workbook.write(outDate);
            bytes = outDate.toByteArray();

            // 关闭
            templateInput.close();
            workbook.close();
            outDate.close();

       } catch (Exception e) {
            logger.info("可捡货清单下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.PickUpMsg.DOWNLOAD_FAILED, type);
        }

        return bytes;

    }

    /**
     * 设置可捡货列表内容（按照订单物品）
     * @param workbook 报表模板
     * @param pickupList 可捡货列表内容
     * @param user 用户登录信息
     */
    private  void setPickupItemsByReservation(HSSFWorkbook workbook, List<FormPickupBean> pickupList,UserSessionBean user) {

        // 仓库初期值
        long store_id = 0;

        HSSFSheet sheet = null;

        // 模板Sheet
        int sheetNo = ReportPickupItems.Reservation.TEMPLATE_SHEET;

        workbook.removeSheetAt(ReportPickupItems.SKU.TEMPLATE_SHEET);

        // 开始行
        int intRow = ReportPickupItems.Reservation.START_ROWS;

        for (FormPickupBean pickup : pickupList) {

            // 仓库改变时，写入新Sheet
            if (store_id != pickup.getStore_id()) {

                String channelName = ChannelConfigs.getChannel(pickup.getStore_order_channel_id()).getFull_name();
                logger.info(channelName + "_" + pickup.getStore_name() + "仓库可捡货清单做成");

                sheet = workbook.cloneSheet(ReportPickupItems.Reservation.TEMPLATE_SHEET);
                sheetNo = sheetNo +1;

                // 设置Sheet名
                workbook.setSheetName(sheetNo,channelName + "_" +pickup.getStore_name());
                store_id =pickup.getStore_id();

                // 重置开始行
                intRow = ReportPickupItems.Reservation.START_ROWS;
            }
            else {
                // 仓库没改变时，增加新行
                HSSFRow newRow = sheet.createRow(intRow);
                HSSFRow oldRow = sheet.getRow(ReportPickupItems.Reservation.START_ROWS);

                for (int col = 0; col < ReportPickupItems.Reservation.MAX_COLUMNS; col++) {
                    HSSFCell newCell = newRow.createCell(col);
                    HSSFCell oldCell = oldRow.getCell(col);

                    newCell.setCellStyle(oldCell.getCellStyle());
                }

            }

            // 得到当前行
            HSSFRow currentRow = sheet.getRow(intRow);

            // 货架名
            currentRow.getCell(ReportPickupItems.Reservation.Column_Location).setCellValue(StringUtils.null2Space2(pickup.getLocation_name()));

            // SKU
            currentRow.getCell(ReportPickupItems.Reservation.Column_SKU).setCellValue(StringUtils.null2Space2(pickup.getSku()));

            // 产品描述
            currentRow.getCell(ReportPickupItems.Reservation.Column_Product).setCellValue(StringUtils.null2Space2(pickup.getProduct()));

            // 订单号
            currentRow.getCell(ReportPickupItems.Reservation.Column_OrderNumber).setCellValue(pickup.getOrder_number());

            // 进入仓库时间
            String uploadTime = StringUtils.isNullOrBlank2(pickup.getCreate_time()) ? "" : DateTimeUtil.getLocalTime(pickup.getCreate_time(), user.getTimeZone());
            currentRow.getCell(ReportPickupItems.Reservation.Column_UploadTime).setCellValue(uploadTime);

            // 发货方式
            currentRow.getCell(ReportPickupItems.Reservation.Column_ShippingMethod).setCellValue(StringUtils.null2Space2(pickup.getShip_channel()));

            // ReservationID
            currentRow.getCell(ReportPickupItems.Reservation.Column_RsvId).setCellValue(pickup.getId());

            // 品牌方SKU
            currentRow.getCell(ReportPickupItems.Reservation.Column_Client_SKU).setCellValue(pickup.getClient_sku());

            intRow = intRow + 1;

        }

        // 如果有记录的话，删除模板sheet
        if (pickupList.size() > 0) {
            workbook.removeSheetAt(ReportPickupItems.Reservation.TEMPLATE_SHEET);
        }

    }

    /**
     * 设置可捡货列表内容（按照SKU）
     * @param workbook 报表模板
     * @param pickupList 可捡货列表内容
     * @param user 用户登录信息
     */
    private  void setPickupItemsBySKU(HSSFWorkbook workbook, List<FormPickupBean> pickupList,UserSessionBean user) {

        // 仓库初期值
        long store_id = 0;

        HSSFSheet sheet = null;

        // 模板Sheet
        int sheetNo = ReportPickupItems.SKU.TEMPLATE_SHEET;

        // 开始行
        int intRow = ReportPickupItems.SKU.START_ROWS;

        String localTime = DateTimeUtil.getLocalTime(user.getTimeZone());

        for (FormPickupBean pickup : pickupList) {

            // 仓库改变时，写入新Sheet
            if (store_id != pickup.getStore_id()) {

                String channelName = ChannelConfigs.getChannel(pickup.getStore_order_channel_id()).getFull_name();
                logger.info(channelName + "_" + pickup.getStore_name() + "仓库可捡货清单做成");

                sheet = workbook.cloneSheet(ReportPickupItems.SKU.TEMPLATE_SHEET);
                sheetNo = sheetNo +1;

                // 设置Sheet名
                workbook.setSheetName(sheetNo,channelName + "_" +pickup.getStore_name());
                store_id =pickup.getStore_id();

                // 重置开始行
                intRow = ReportPickupItems.SKU.START_ROWS;
            }
            else {
                // 仓库没改变时，增加新行
                HSSFRow newRow = sheet.createRow(intRow);
                HSSFRow oldRow = sheet.getRow(ReportPickupItems.SKU.START_ROWS);

                for (int col = 0; col < ReportPickupItems.SKU.MAX_COLUMNS; col++) {
                    HSSFCell newCell = newRow.createCell(col);
                    HSSFCell oldCell = oldRow.getCell(col);

                    newCell.setCellStyle(oldCell.getCellStyle());
                }

            }

            // 得到当前行
            HSSFRow currentRow = sheet.getRow(intRow);

            // 日期
            currentRow.getCell(ReportPickupItems.SKU.Column_Date).setCellValue(localTime);

            // SKU
            currentRow.getCell(ReportPickupItems.SKU.Column_SKU).setCellValue(StringUtils.null2Space2(pickup.getSku()));

            // 数量
            currentRow.getCell(ReportPickupItems.SKU.Column_Qty).setCellValue(StringUtils.null2Space2(pickup.getQty()));

            // 货架名
            currentRow.getCell(ReportPickupItems.SKU.Column_Location).setCellValue(StringUtils.null2Space2(pickup.getLocation_name()));

            // 品牌方SKU
            currentRow.getCell(ReportPickupItems.SKU.Column_Client_SKU).setCellValue(pickup.getClient_sku());

            intRow = intRow + 1;

        }

        // 如果有记录的话，删除模板sheet
        if (pickupList.size() > 0) {
            workbook.removeSheetAt(ReportPickupItems.SKU.TEMPLATE_SHEET);
        }

        workbook.removeSheetAt(ReportPickupItems.Reservation.TEMPLATE_SHEET);

    }

    /**
     * 取得画面的检索参数进行编辑以符合DB检索的要求
     * @param paramMap 画面的检索参数
     * @param user 用户登录信息
     * @return Map DB检索条件
     */
    private  Map<String, Object> getSelectParams(Map<String, Object> paramMap, UserSessionBean user, String reserveType) {

        // 取得画面的各个检索参数
        String order_number = (String) paramMap.get("order_number");
        String id = (String) paramMap.get("id");
        String sku = (String) paramMap.get("sku");
        String status = (String) paramMap.get("status");
        int store_id = (int) paramMap.get("store_id");
        String from = (String) paramMap.get("from");
        String to = (String) paramMap.get("to");

        // 分页的条件
        int page = (int) paramMap.get("page");
        int size = (int) paramMap.get("size");
        int offset = (page - 1) * size;

        // 用户所属仓库的设置
        List<ChannelStoreBean> channelStoreList = user.getCompanyRealStoreList();

        // 获取相关渠道
        List<String> orderChannelList = user.getChannelList();

        // DB检索用参数的设置
        Map<String, Object> resultMap = new HashMap<>();

        //数字 ？ 转化成数字 ： null or '' ? 0 : -1
        resultMap.put("order_number", StringUtils.isNumeric(order_number) ? Long.valueOf(order_number) : StringUtils.isEmpty(order_number) ? 0 : -1);
        resultMap.put("id", StringUtils.isNumeric(id)?  Long.valueOf(id) : StringUtils.isEmpty(id) ? 0 : -1);
        resultMap.put("sku", StringUtils.isNullOrBlank2(sku) ? "" : sku);
        resultMap.put("status", StringUtils.isNullOrBlank2(status) ? "" : status);
        resultMap.put("store_id", store_id);
        resultMap.put("storeList", channelStoreList);
        resultMap.put("orderChannelList",orderChannelList);
        resultMap.put("from", StringUtils.isNullOrBlank2(from)? "0000-00-00 00:00:00" : DateTimeUtil.getGMTTimeFrom(from, user.getTimeZone()));
        resultMap.put("to", StringUtils.isNullOrBlank2(to)? "9999-99-99 99:99:99" : DateTimeUtil.getGMTTimeTo(to, user.getTimeZone()));
        resultMap.put("offset", offset);
        resultMap.put("size", size);
        // 自有仓库捡货时，排除锁单记录
        resultMap.put("reserveType", reserveType);

        return resultMap;

    }

    /**
     * 对于一些画面表示用项目进行设置
     * @param pickup 抽出的捡货记录
     * @param user 用户登录信息
     */
    private void setPickupDisplayInfo(FormPickupBean pickup, UserSessionBean user) {

        // 订单渠道名
        pickup.setOrder_channel_name(ChannelConfigs.getChannel(pickup.getOrder_channel_id()).getFull_name());

        // 物品状态名
        pickup.setStatus_name(Type.getTypeName(MastType.reservationStatus.getId(), pickup.getStatus()));

        // 创建时间（本地时间）
        pickup.setCreated_local(DateTimeUtil.getLocalTime(pickup.getCreate_time(), user.getTimeZone()));

        // 更新时间（本地时间）
        pickup.setModified_local(DateTimeUtil.getLocalTime(pickup.getUpdate_time(), user.getTimeZone()));

    }

    /**
     * 设置捡货单的内容
     * @param scanInfoList 根据输入条件取得的捡货记录
     * @param scanType 扫描类型
     * @return FormPickUpLabelBean
     */
    private FormPickUpLabelBean getPickupLabel (List<FormPickupBean>scanInfoList, String scanType, String scanNo) {

        FormPickUpLabelBean pickupLabelBean = new FormPickUpLabelBean();

        // 订单号
        pickupLabelBean.setOrder_number(String.valueOf(scanInfoList.get(0).getOrder_number()));

        // 预计到货期
        pickupLabelBean.setExpected_ship_date("");

        // 分库
        pickupLabelBean.setStore(scanInfoList.get(0).getStore());

        // 收件名
        pickupLabelBean.setShip_name(StringUtils.null2Space2(scanInfoList.get(0).getShip_name()));

        // 收件公司
        pickupLabelBean.setShip_company(StringUtils.null2Space2(scanInfoList.get(0).getShip_company()));

        // 收件地址
        pickupLabelBean.setShip_address(StringUtils.null2Space2(scanInfoList.get(0).getShip_address()));

        // 收件地址2
        pickupLabelBean.setShip_address2(StringUtils.null2Space2(scanInfoList.get(0).getShip_address2()));

        // 收件城市
        pickupLabelBean.setShip_city(StringUtils.null2Space2(scanInfoList.get(0).getShip_city()));

        // 收件省
        pickupLabelBean.setShip_state(StringUtils.null2Space2(scanInfoList.get(0).getShip_state()));

        // 收件邮编
        pickupLabelBean.setShip_zip(StringUtils.null2Space2(scanInfoList.get(0).getShip_zip()));

        // 收件国家
        pickupLabelBean.setShip_country(StringUtils.null2Space2(scanInfoList.get(0).getShip_country()));

        // 发货方
        pickupLabelBean.setShipping(StringUtils.null2Space2(scanInfoList.get(0).getShipping()));

        // 物品捡货时，按照物品级别设置
        if (ChannelConfigEnums.Scan.RES.getType().equals(scanType)) {

            // 配货号
            pickupLabelBean.setReservation_id(String.valueOf(scanInfoList.get(0).getId()));

            // 货品名称
            pickupLabelBean.setProduct(scanInfoList.get(0).getProduct());

            // SKU（品牌方SKU存在时，显示品牌方SKU）
            String client_sku = StringUtils.null2Space(reservationDao.getClientSku(scanInfoList.get(0).getOrder_channel_id(), scanInfoList.get(0).getSku()));

            pickupLabelBean.setSku(StringUtils.isNullOrBlank2(client_sku) ? scanInfoList.get(0).getSku() : client_sku);

        }
        // 订单捡货时，按照订单级别设置
        else if (ChannelConfigEnums.Scan.ORDER.getType().equals(scanType))  {

            // 配货号
            pickupLabelBean.setReservation_id(scanNo);

            // 货品名称
            pickupLabelBean.setProduct("");

            // SKU
            pickupLabelBean.setSku("");

        }
        // 订单物品收货时，按照物品级别设置
        else if (ChannelConfigEnums.Scan.ITEM.getType().equals(scanType))  {

            // 配货号
            pickupLabelBean.setReservation_id(scanNo);

            // 货品名称
            pickupLabelBean.setProduct(scanInfoList.get(0).getProduct());

            // SKU（品牌方SKU存在时，显示品牌方SKU）
            String client_sku = StringUtils.null2Space(reservationDao.getClientSku(scanInfoList.get(0).getOrder_channel_id(), scanInfoList.get(0).getSku()));

            pickupLabelBean.setSku(StringUtils.isNullOrBlank2(client_sku) ? scanInfoList.get(0).getSku() : client_sku);

        }
        else  if (ChannelConfigEnums.Scan.UPC.getType().equals(scanType)) {

            // 配货号
            pickupLabelBean.setReservation_id(String.valueOf(scanInfoList.get(0).getId()));

            // 货品名称
            pickupLabelBean.setProduct(scanInfoList.get(0).getProduct());

            // SKU（品牌方SKU存在时，显示品牌方SKU）
            String client_sku = StringUtils.null2Space(reservationDao.getClientSku(scanInfoList.get(0).getOrder_channel_id(), scanInfoList.get(0).getSku()));

            pickupLabelBean.setSku(StringUtils.isNullOrBlank2(client_sku) ? scanInfoList.get(0).getSku() : client_sku);

        }

        // 发货渠道
        pickupLabelBean.setShip_channel(scanInfoList.get(0).getShip_channel());

        // 数量
        pickupLabelBean.setQty(scanInfoList.get(0).getQty());

        // 渠道编号
        pickupLabelBean.setCart_id(scanInfoList.get(0).getCart_id());

        // 捡货单类别
        pickupLabelBean.setLabel_type(scanInfoList.get(0).getLabel_type());

        // 订单渠道名
        pickupLabelBean.setOrder_channel_name(scanInfoList.get(0).getOrder_channel_name());

        // 物品状态名
        pickupLabelBean.setStatus_name(scanInfoList.get(0).getStatus_name());

        // 创建时间（本地时间）
        pickupLabelBean.setCreated_local(scanInfoList.get(0).getCreated_local());

        // 更新时间（本地时间）
        pickupLabelBean.setModified_local(scanInfoList.get(0).getModified_local());

        return pickupLabelBean;

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

        // 货架所在
        reservation.setLocation_name(reservationDao.getLocationBySKU(reservation.getOrder_channel_id(),reservation.getSku(),reservation.getStore_id()));

        // 品牌方SKU
        reservation.setClient_sku(StringUtils.null2Space(reservationDao.getClientSku(reservation.getOrder_channel_id(), reservation.getSku())));


    }

    /**
     * 将skuList拼接成约定格式的字符串
     * @param list skuList
     * @return string sku
     */
    private String skuListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        int skuNum = 1;
        if (list.size() > 1)
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i - 1).equals(list.get(i)))
                    skuNum = skuNum + 1;
                else {
                    sb.append(list.get(i - 1)).append(" * ").append(skuNum).append(",");
                    skuNum = 1;
                }
                if (i == (list.size() - 1)) {
                    sb.append(list.get(i)).append(" * ").append(skuNum);
                }
            }
        else sb.append(list.get(0)).append(" * ").append(1);
        return sb.toString();
    }

    /**
     * 【捡货画面】下载已捡货报告
     * @param store_id 画面的检索参数
     * @param from 画面的检索参数
     * @param to
     * @param user 用户登录信息
     * @return ResponseEntity<byte[]> 可捡货列表
     */
    @Override
    public byte[] downloadReportPicked(String store_id,String from,String to, UserSessionBean user, String reserveType) {

        byte[] bytes = null;

        // 检索参数的取得和设置
        Map<String, Object> selectParams = getSelectParamsForDownload(store_id, from, to, user, reserveType);

        // 取得符合条件的记录
        List<PickedInfoBean> pickedList = new ArrayList<>();
        // 根据检索参数取得记录
        pickedList = reservationDao.downloadPickedItemsByReservation(selectParams);

        logger.info("可捡货清单件数：" + pickedList.size());
        try{

            // 报表模板名取得
            String templateFile = Properties.readValue(ReportSetting.WMS_REPORT_TEMPLATE_PATH) + WmsConstants.ReportPickedItems.FILE_NAME + ".xls";

            // 报表模板名读入
            InputStream templateInput = new FileInputStream(templateFile);
            HSSFWorkbook workbook = new HSSFWorkbook(templateInput);

            // 设置内容
            setPickedItemsOfReport(workbook, pickedList, user);

            // 出力内容
            ByteArrayOutputStream outDate = new ByteArrayOutputStream();
            workbook.write(outDate);
            bytes = outDate.toByteArray();

            // 关闭
            templateInput.close();
            workbook.close();
            outDate.close();

        } catch (Exception e) {
            logger.info("可捡货清单下载失败：" + e);
            throw new BusinessException(WmsMsgConstants.PickUpMsg.DOWNLOAD_FAILED, e);
        }

        return bytes;

    }

    /**
     * 取得下载报告画面的检索参数进行编辑以符合DB检索的要求
     * @param store_id 画面的检索参数
     * @param from 画面的检索参数
     * @param to
     * @param user 用户登录信息
     * @return Map DB检索条件
     */
    private  Map<String, Object> getSelectParamsForDownload(String store_id,String from,String to, UserSessionBean user, String reserveType) {

        // 取得画面的各个检索参数
        int storeid = Integer.valueOf(store_id).intValue();

        // 用户所属仓库的设置
        List<ChannelStoreBean> channelStoreList = new ArrayList<ChannelStoreBean>();

        // 根据reserveType来决定显示仓库
        for (ChannelStoreBean storeBean : user.getCompanyRealStoreList() ) {
            StoreBean store = StoreConfigs.getStore(new Long(storeBean.getStore_id()));
            if (reserveType.equals(WmsConstants.ReserveType.PickUp)) {
                if (store.getIs_sale().equals(StoreConfigEnums.Sale.YES.getId())) {
                    channelStoreList.add(storeBean);
                }
            }
            else  if (reserveType.equals(WmsConstants.ReserveType.Receive)) {
                if (store.getInventory_manager().equals(StoreConfigEnums.Manager.NO.getId())) {
                    channelStoreList.add(storeBean);
                }
            }
        }

        // 获取相关渠道
        List<String> orderChannelList = user.getChannelList();

        // DB检索用参数的设置
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("store_id", storeid);
        resultMap.put("storeList", channelStoreList);
        resultMap.put("orderChannelList", orderChannelList);
        resultMap.put("from", StringUtils.isNullOrBlank2(from) ? "0000-00-00 00:00:00" : DateTimeUtil.getGMTTimeFrom(from, user.getTimeZone()));
        resultMap.put("to", StringUtils.isNullOrBlank2(to) ? "9999-99-99 99:99:99" : DateTimeUtil.getGMTTimeTo(to, user.getTimeZone()));

        return resultMap;

    }

    /**
     * 设置已捡货列表内容
     * @param workbook 报表模板
     * @param pickedList 已捡货列表内容
     * @param user 用户登录信息
     */
    private  void setPickedItemsOfReport(HSSFWorkbook workbook, List<PickedInfoBean> pickedList,UserSessionBean user) {

        // 仓库初期值
        String store = "";

        HSSFSheet sheet = null;

        // 模板Sheet
        int sheetNo = WmsConstants.ReportPickedItems.Reservation.TEMPLATE_SHEET;

        // 开始行
        int intRow = WmsConstants.ReportPickedItems.Reservation.START_ROWS;

        for (PickedInfoBean picked : pickedList) {

            // 仓库改变时，写入新Sheet
            if (!store.equals(picked.getStore())) {

                String channelName = ChannelConfigs.getChannel(picked.getOrder_channel_id()).getFull_name();
                logger.info(channelName + "_" + picked.getStore() + "仓库已捡货清单做成");

                sheet = workbook.cloneSheet(WmsConstants.ReportPickedItems.Reservation.TEMPLATE_SHEET);
                sheetNo = sheetNo +1;

                // 设置Sheet名
                workbook.setSheetName(sheetNo,channelName + "_" +picked.getStore());
                store =picked.getStore();

                // 重置开始行
                intRow = WmsConstants.ReportPickedItems.Reservation.START_ROWS;
            }
            else {
                // 仓库没改变时，增加新行
                HSSFRow newRow = sheet.createRow(intRow);
                HSSFRow oldRow = sheet.getRow(WmsConstants.ReportPickedItems.Reservation.START_ROWS);

                for (int col = 0; col < WmsConstants.ReportPickedItems.Reservation.MAX_COLUMNS; col++) {
                    HSSFCell newCell = newRow.createCell(col);
                    HSSFCell oldCell = oldRow.getCell(col);

                    newCell.setCellStyle(oldCell.getCellStyle());
                }

            }

            // 得到当前行
            HSSFRow currentRow = sheet.getRow(intRow);
            //RowNo,PickUP Time,OrderNumber,ResID,SKU,Des,Qty
            // RowNo
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_RowNo).setCellValue(String.valueOf(intRow));

            // PickedTime
            String pickedTime = StringUtils.isNullOrBlank2(picked.getDate()) ? "" : DateTimeUtil.getLocalTime(picked.getDate(), user.getTimeZone());
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_PickedTime).setCellValue(pickedTime);

            // OrderNumber
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_OrderNumber).setCellValue(StringUtils.null2Space2(picked.getOrder_number()));

            // ResID
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_ResID).setCellValue(picked.getId());

            // SKU
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_SKU).setCellValue(picked.getSku());

            // Des
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_Des).setCellValue(StringUtils.null2Space2(picked.getDescription_short()));

            // Qty
            currentRow.getCell(WmsConstants.ReportPickedItems.Reservation.Column_Qty).setCellValue(picked.getQty());

            intRow = intRow + 1;

        }

        // 如果有记录的话，删除模板sheet
        if (pickedList.size() > 0) {
            workbook.removeSheetAt(WmsConstants.ReportPickedItems.Reservation.TEMPLATE_SHEET);
        }

    }
}
