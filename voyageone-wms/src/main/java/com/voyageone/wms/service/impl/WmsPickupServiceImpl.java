package com.voyageone.wms.service.impl;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.common.configs.Enums.TypeConfigEnums.MastType;
import com.voyageone.common.configs.Properties;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.Type;
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
import com.voyageone.wms.dao.ReservationDao;
import com.voyageone.wms.dao.ReservationLogDao;
import com.voyageone.wms.dao.TrackingInfoDao;
import com.voyageone.wms.formbean.FormPickUpLabelBean;
import com.voyageone.wms.formbean.FormPickupBean;
import com.voyageone.wms.formbean.FormReservation;
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
        resultMap.put("defaultStatus", WmsCodeConstants.Reservation_Status.Open);

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
                if (store.getInventory_manager().equals(StoreConfigEnums.Manager.YES.getId()) && store.getIs_sale().equals(StoreConfigEnums.Sale.YES.getId())) {
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

        // 获取相关渠道对应的扫描方式
        List<String> orderChannelList = user.getChannelList();
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
    public List<FormReservation> getPickupInfo(Map<String, Object> paramMap, UserSessionBean user) {

        // 检索参数的取得和设置
        Map<String, Object> selectParams = getSelectParams(paramMap, user);

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
    public int getPickupCount(Map<String, Object> paramMap, UserSessionBean user) {

        // 检索参数的取得和设置
        Map<String, Object> selectParams = getSelectParams(paramMap, user);

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

        // 取得符合条件的记录
        List<FormPickupBean> scanInfoListALL = reservationDao.getScanInfo(scanMode, scanType, scanNo, scanStatus, scanStore, channelStoreList, orderChannelList);

        String StatusName = Type.getTypeName(MastType.reservationStatus.getId(),scanStatus);

        List<FormPickupBean> scanInfoList =new ArrayList<>();
        int intCancelled = 0;

        for (FormPickupBean formPickupBean : scanInfoListALL) {
            // 取得满足状态条件的记录
            if (formPickupBean.getStatus().equals(scanStatus)) {
                scanInfoList.add(formPickupBean);
            } else if (formPickupBean.getStatus().equals(WmsCodeConstants.Reservation_Status.Cancelled)) {
                intCancelled ++;
            }
        }

        // 0件的场合，错误信息表示
        if (scanInfoList.size() == 0) {
            // 存在取消记录时，请仓库进行入库操作
            if (intCancelled > 0) {
                logger.info("该记录已被取消" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.CANCELLED);
            } else {
                logger.info("没有取得符合条件的记录" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+ "）");
                throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_SCANNO, reserveType, scanTypeName, scanNo, StatusName);
            }
        }

        // 需要判断港口的场合，取得相关港口（BHFO等渠道是由品牌方仓库发出后再捡货的）
        if (!StringUtils.isNullOrBlank2(scanPort)) {
            String port = reservationDao.getPort(scanInfoList.get(0).getSyn_ship_no(),scanInfoList.get(0).getId());

            if (!scanPort.equals(port)) {
                logger.info("没有取得符合条件的记录" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo+  "，ScanPort：" + scanPort + "）");

                throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_SCANNO, reserveType, scanTypeName, scanNo, StatusName);
            }
        }

        // 得到订单对应的物品数
        List<String> orderSkuList = reservationDao.getOrderProductList(scanInfoList.get(0).getOrder_number());
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
        // 跨境电商的场合，发货渠道的判断
        if (ChannelConfigEnums.Sale.CB.getType().equals(ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sale_type))) {
            shipChannel = reservationDao.getShippingMethod(orderChannelId, scanInfoList.get(0).getOrder_number(), scanInfoList.get(0).getId());
        }
        // 国内电商的场合，默认发货渠道的取得
        else  if (ChannelConfigEnums.Sale.CB.getType().equals(ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.sale_type))) {
            shipChannel = ChannelConfigs.getVal1(orderChannelId, ChannelConfigEnums.Name.default_ship_channel);
        }
        logger.info("发货渠道判定" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo  + "，ReservationID：" + reservationList.toString() + "，ShipChannel：" + shipChannel +  "）");

        // 根据发货渠道计算折扣
        BigDecimal price = new BigDecimal(scanInfoList.get(0).getOriginal_price());

        BigDecimal declareRate = ChannelConfigs.getDiscountRate(orderChannelId, shipChannel);

        if (declareRate == null) {
            logger.info("未取得相关发货渠道的折扣" + "（OrderChannelId：" + orderChannelId  + "，ShipChannel：" + shipChannel  +  "）");
            throw new BusinessException(WmsMsgConstants.PickUpMsg.NOT_FOUND_DISCOUNT_RATE, orderChannelId, shipChannel);
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
        FormPickUpLabelBean pickupLabel = getPickupLabel(scanInfoList, scanType);
        //设置skuList
        pickupLabel.setSkuList(skuListToString(orderSkuList));
        String printPickupLabel = "[" + JsonUtil.getJsonString(pickupLabel)  + "]";
        logger.info("捡货单内容取得：" + printPickupLabel);

        // 根据仓库判断库存是否需要管理
        StoreBean store = StoreConfigs.getStore(Long.valueOf(scanStore));
        String inventory_manager = store.getInventory_manager();
        String closeDayFlg = "";
        // 捡货时 需要判断closeDayFlg
        if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
            closeDayFlg = WmsConstants.CloseDayFlg.Process;
            if (StoreConfigEnums.Manager.NO.getId().equals(inventory_manager)) {
                closeDayFlg = WmsConstants.CloseDayFlg.Done;
            }
        }

        // 更新捡货物品的状态和发货渠道
        int resultUpdatePickup = reservationDao.updatePickupStatus(reservationList, scanStatus, updateStatus, shipChannel, price, closeDayFlg, user.getUserName());

        if (resultUpdatePickup == 0) {
            logger.info("捡货物品更新失败" + "（scanTypeName：" + scanTypeName  + "，ScanNo：" + scanNo  + "，ReservationID：" + reservationList.toString() +  "）");
            throw new BusinessException(WmsMsgConstants.RsvListMsg.UPDATE_ERROR, reservationList.toString());
        }

        // 插入ReservationLog
        reservationLogDao.insertReservationLog(reservationList, scanMode, user.getUserName());

        // 捡货时 插入TrackingInfo
        if (WmsConstants.ScanType.SCAN.equals(scanMode)) {
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
    private  Map<String, Object> getSelectParams(Map<String, Object> paramMap, UserSessionBean user) {

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
    private FormPickUpLabelBean getPickupLabel (List<FormPickupBean>scanInfoList, String scanType) {

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

            // SKU
            pickupLabelBean.setSku(scanInfoList.get(0).getSku());
        }
        // 订单捡货时，按照订单级别设置
        else if (ChannelConfigEnums.Scan.ORDER.getType().equals(scanType))  {

            // 配货号
            pickupLabelBean.setReservation_id("");

            // 货品名称
            pickupLabelBean.setProduct("");

            // SKU
            pickupLabelBean.setSku("");

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
}
