package com.voyageone.batch.wms.service;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Constants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.InventoryDao;
import com.voyageone.batch.wms.dao.ReservationDao;
import com.voyageone.batch.wms.dao.ReservationLogDao;
import com.voyageone.batch.wms.modelbean.*;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.ShopConfigs;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.mail.Mail;
import com.voyageone.common.util.*;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductForWmsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductForWmsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WmsSetAllotInventoryService extends BaseTaskService {

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ReservationLogDao reservationLogDao;

    @Autowired
    protected VoApiDefaultClient voApiClient;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsSetAllotInventoryJob";
    }

    public void onStartup(List<TaskControlBean> taskControlList) throws Exception {

        // 允许运行的订单渠道取得
        List<String> orderChannelIdList = TaskControlUtils.getVal1List(taskControlList, TaskControlEnums.Name.order_channel_id);

        // 抽出件数
        String row_count = TaskControlUtils.getVal1(taskControlList,TaskControlEnums.Name.row_count);

        int intRowCount = 1;
        if (!StringUtils.isNullOrBlank2(row_count)) {
            intRowCount = Integer.valueOf(row_count);
        }

        // 线程
        List<Runnable> threads = new ArrayList<>();

        List<Exception> exceptions = new ArrayList<>();

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final String postURI = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            final int finalIntRowCount = intRowCount;

            threads.add(() -> {
                try {
                    new setAllotInventory(orderChannelID, postURI, finalIntRowCount).doRun();
                } catch (MessagingException e) {
                    exceptions.add(e);
                }
            });

        }

        runWithThreadPool(threads, taskControlList);

        // 任务结束后统一生成 issueLog
        exceptions.forEach(this::logIssue);

    }

    /**
     * 按渠道进行订单库存分配
     */
    public class setAllotInventory  {
        private OrderChannelBean channel;
        private String postURI;
        private int rowCount;

        public setAllotInventory(String orderChannelId, String postURI, int rowCount) {
            this.channel = ChannelConfigs.getChannel(orderChannelId);
            this.postURI = postURI;
            this.rowCount = rowCount;
        }

        public void doRun() throws MessagingException {
            $info(channel.getFull_name()+"(订单级别)库存分配开始" );

            if (StringUtils.isNullOrBlank2(postURI)) {
                $info(channel.getFull_name()+"(订单级别)库存分配错误：PostURL地址没有设置" );
                logIssue(channel.getFull_name() + " PostURL地址没有设置，无法库存分配");
                return;
            }

            // ItemDetail表中没有的记录取得
            List<String> notExistsItemCode = reservationDao.getNotExistsItemCode(channel.getOrder_channel_id());

            $info(channel.getFull_name() + "------(订单级别)SKU在ItemDetail中不存在件数：" + notExistsItemCode.size());
            if (notExistsItemCode.size() > 0 ) {
                logIssue(channel.getFull_name() + "：SKU在ItemDetail中不存在，无法库存分配", notExistsItemCode);
            }

            // 需要库存分配的记录取得
            List<ReservationBean> reservationList = reservationDao.getAllotInventoryIntoByOrder(channel.getOrder_channel_id(), rowCount);

            $info(channel.getFull_name() + "------(订单级别)库存分配明细件数：" + reservationList.size());

            List<AllotInventoryDetailBean>  allotInventoryDetailList = new ArrayList<>();

            List<AllotInventoryDetailBean>  errorAllotInventoryDetailList = new ArrayList<>();

            long orderNumber = 0;
            AllotInventoryDetailBean allotInventoryDetailBean = null;

            // 按照订单来分组数据
            for (ReservationBean reservation : reservationList) {

                if (orderNumber == reservation.getOrder_number()) {
                    List<ReservationBean> lstReservation = allotInventoryDetailBean.getLstReservation();
                    if (lstReservation == null) {
                        lstReservation = new ArrayList<>();
                    }

                    lstReservation.add(reservation);

                    allotInventoryDetailBean.setLstReservation(lstReservation);
                } else {
                    allotInventoryDetailBean = new AllotInventoryDetailBean();

                    allotInventoryDetailList.add(allotInventoryDetailBean);

                    allotInventoryDetailBean.setOrder_number(reservation.getOrder_number());

                    List<ReservationBean> lstReservation = allotInventoryDetailBean.getLstReservation();
                    if (lstReservation == null) {
                        lstReservation = new ArrayList<>();
                    }

                    lstReservation.add(reservation);

                    allotInventoryDetailBean.setLstReservation(lstReservation);

                    orderNumber = reservation.getOrder_number();

                }

            }

            $info(channel.getFull_name() + "------(订单级别)库存分配订单件数：" + allotInventoryDetailList.size());

            // 取得再分配标志
            List<OrderChannelConfigBean> allot_inventory_again = ChannelConfigs.getConfigs(channel.getOrder_channel_id(), ChannelConfigEnums.Name.allot_inventory_again);

            for (AllotInventoryDetailBean allotInventory : allotInventoryDetailList) {

                $info(channel.getFull_name() + "---------(订单级别)Order_Number：" + allotInventory.getOrder_number() + "，明细件数：" + allotInventory.getLstReservation().size());

                allotInventory.setAllot_error(false);
                try {

                    // 取得物品的产品信息、分配仓库等
                    for (ReservationBean reservation : allotInventory.getLstReservation()) {

                        $info(channel.getFull_name() + "----------(订单级别)初回处理Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number()+ "，SKU：" + reservation.getSku());

                        // SKU信息取得
                        String result = getItemCodeInfo(channel.getOrder_channel_id(), reservation.getItemCode(), reservation.getSku(), postURI);
                        $info(channel.getFull_name() + "----------(订单级别)初回处理getItemCodeInfo："+ result);

                        ItemCodeBean itemCodeInfo = new ItemCodeBean();
                        // 调用WebService时返回为空时，抛出错误
                        if (StringUtils.isNullOrBlank2(result)) {
                            $info(channel.getFull_name() + "----------库存分配时SKU信息取得失败"+ "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number()+ "，SKU：" + reservation.getSku());
                            logIssue(channel.getFull_name() + "库存分配时SKU信息取得失败" + "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku());
                            allotInventory.setAllot_error(true);
                        } else {

                            List<ItemCodeBean> itemCodeInfoList = new ArrayList<>();
                            String errorMessage = "";

                            // 老的webService（调用老的webService, ）
                            if (postURI.contains("aspx") ) {
                                itemCodeInfoList = JsonUtil.jsonToBeanList(result, ItemCodeBean.class);
                            }
                            else if ( postURI.contains("sdk-client"))
                            {
                                ProductBean productBean = JsonUtil.jsonToBean(result, ProductBean.class);
                                itemCodeInfoList = proResult(productBean);
                            } else {
                                // 新的webService
                                CmsWsdlResponseBean cmsWsdlResponseBean = JsonUtil.jsonToBean(result, CmsWsdlResponseBean.class);

                                // 返回结果为NG时，记录下错误信息
                                if (cmsWsdlResponseBean.getResult().equals("NG")) {
                                    errorMessage = cmsWsdlResponseBean.getMessage();
                                } else if (cmsWsdlResponseBean.getResult().equals("OK")) {

                                    if (cmsWsdlResponseBean.getResultInfo().size() > 0) {
                                        itemCodeInfoList = proResult(cmsWsdlResponseBean.getResultInfo().get(0));
                                    }
                                }
                            }

                            if (itemCodeInfoList.size() == 0) {
                                $info(channel.getFull_name() + "----------库存分配时SKU信息取得失败"+ "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number()+ "，SKU：" + reservation.getSku() + "，ErrorMessage：" + errorMessage);
                                logIssue(channel.getFull_name() + "库存分配时SKU信息未取得", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，ErrorMessage：" + errorMessage);
                                allotInventory.setAllot_error(true);
                            } else {

                                itemCodeInfo = itemCodeInfoList.get(0);
                                reservation.setItemCodeInfo(itemCodeInfo);

                                // 产品属性的检查
                                boolean productAttributes = checkProductAttributes(channel.getOrder_channel_id(), itemCodeInfo);

                                if (!productAttributes) {
                                    String json = itemCodeInfo == null ? "" : new Gson().toJson(itemCodeInfo);
                                    $info(channel.getFull_name() + "----------(订单级别)初回处理产品属性设置不全");
                                    logIssue(channel.getFull_name() + "税号等产品属性设置不全，无法库存分配", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Attributes：" + json);
                                    allotInventory.setAllot_error(true);
                                }

                                // 分配仓库
                                long storeId = reservationDao.getAllotStore(channel.getOrder_channel_id(), reservation.getCart_id(), reservation.getSku());
                                $info(channel.getFull_name() + "----------(订单级别)初回处理getAllotStore：" + storeId);

                                // 仓库分配没有取得
                                if (storeId == 0) {
                                    $info(channel.getFull_name() + "----------(订单级别)库存分配时仓库取得失败"+ "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                    logIssue(channel.getFull_name() + "(订单级别)库存分配时仓库取得失败", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                    allotInventory.setAllot_error(true);
                                } else {
                                    reservation.setStore_id(storeId);

                                    // 库存再分配仓库判断
                                    if (allot_inventory_again != null) {
                                        for (OrderChannelConfigBean orderChannelConfigBean : allot_inventory_again) {
                                            if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.INVENTORY_MANAGER)) {
                                                if (StoreConfigs.getStore(storeId).getInventory_manager().equals(orderChannelConfigBean.getCfg_val2())) {
                                                    allotInventory.setAllot_again(true);
                                                }
                                            }
                                            if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.STORE_AREA)) {
                                                if (StoreConfigs.getStore(storeId).getStore_area().contains(orderChannelConfigBean.getCfg_val2())) {
                                                    allotInventory.setAllot_again(true);
                                                }
                                            }
                                        }
                                    }


                                }
                            }
                        }
                    }

                    // 库存分配仓库再判定
                    if (!allotInventory.isAllot_error()) {

                        // 过滤仓库
                        List <Long> exceptStoreList = new ArrayList<>();

                        List<StoreBean> storeLst = StoreConfigs.getChannelStoreList(channel.getOrder_channel_id());

                        for (StoreBean storeBean : storeLst) {

                            // 库存再分配仓库判断
                            if (allot_inventory_again != null) {
                                for (OrderChannelConfigBean orderChannelConfigBean : allot_inventory_again) {
                                    if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.INVENTORY_MANAGER)) {
                                        if (!StoreConfigs.getStore(storeBean.getStore_id()).getInventory_manager().equals(orderChannelConfigBean.getCfg_val2())) {
                                            exceptStoreList.add(storeBean.getStore_id());
                                        }
                                    }
                                    if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.STORE_AREA)) {
                                        if (!StoreConfigs.getStore(storeBean.getStore_id()).getStore_area().contains(orderChannelConfigBean.getCfg_val2())) {
                                            exceptStoreList.add(storeBean.getStore_id());
                                        }
                                    }
                                }
                            }
                        }

                        for (ReservationBean reservation : allotInventory.getLstReservation()) {

                             if (allotInventory.isAllot_again()) {

                                boolean blnFound = false;

                                // 库存再分配仓库判断
                                 if (allot_inventory_again != null) {
                                     for (OrderChannelConfigBean orderChannelConfigBean : allot_inventory_again) {
                                         if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.INVENTORY_MANAGER)) {
                                             if (!StoreConfigs.getStore(reservation.getStore_id()).getInventory_manager().equals(orderChannelConfigBean.getCfg_val2())) {
                                                 blnFound = true;
                                             }
                                         }
                                         if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ALLOT_INVENTORY_AGAIN.STORE_AREA)) {
                                             if (!StoreConfigs.getStore(reservation.getStore_id()).getStore_area().contains(orderChannelConfigBean.getCfg_val2())) {
                                                 blnFound = true;
                                             }
                                         }
                                     }
                                 }

                                if (blnFound) {

                                    String storeList = "0";
                                    if  (exceptStoreList.size() > 0) {
                                        storeList = exceptStoreList.toString().replace("[","").replace("]","");
                                    }
                                    // 再次分配仓库
                                    long storeId = reservationDao.getAllotStoreAgain(channel.getOrder_channel_id(), reservation.getCart_id(), reservation.getSku(), storeList);
                                    $info(channel.getFull_name() + "----------(订单级别)再次分配getAllotStore：" + storeId);

                                    // 仓库分配没有取得
                                    if (storeId == 0) {
                                        $info(channel.getFull_name() + "----------(订单级别)库存分配时仓库取得失败"+ "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                        logIssue(channel.getFull_name() + "(订单级别)库存分配时仓库取得失败", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                        allotInventory.setAllot_error(true);
                                    } else {
                                        // 真实仓库时，记入
                                        if (StoreConfigs.getStore(storeId).getStore_kind().equals(StoreConfigEnums.Kind.REAL.getId())) {
                                            $info(channel.getFull_name() + "----------(订单级别)再次分配Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                            reservation.setStore_id(storeId);
                                        }
                                    }
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    $info(channel.getFull_name() + "----------(订单级别)库存分配错误，Order_Number："+ allotInventory.getOrder_number() + "，ErrorMessage：" + e);
                    logIssue(channel.getFull_name() + "----------(订单级别)库存分配错误，Order_Number：" + allotInventory.getOrder_number(),e);
                    allotInventory.setAllot_error(true);
                }

                if (!allotInventory.isAllot_error()) {

                    transactionRunner.runWithTran(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                List<Long> reservationList = new ArrayList<>();
                                List<Long> storeList = new ArrayList<>();
                                for (ReservationBean reservation : allotInventory.getLstReservation()) {

                                    // Reservation表编辑
                                    setReservation(reservation, reservation.getItemCodeInfo(), reservation.getStore_id());

                                    // 插入tt_reservation表
                                    long res_id = reservationDao.insertReservation(reservation);
                                    reservation.setId(res_id);
                                    reservationList.add(res_id);
                                    storeList.add(reservation.getStore_id());

                                    // 更新OrderDetails
                                    reservationDao.updateOrderDetails(reservation.getOrder_number(), reservation.getItem_number(), res_id, getTaskName());

                                    $info(channel.getFull_name() + "----------(订单级别)库存分配成功：" + res_id);

                                }

                                // 插入ReservationLog
                                reservationLogDao.insertReservationLog(reservationList, "AllotInventory", getTaskName());

                                // 发货渠道的取得
                                for (long resId : reservationList) {

                                    String shipChannel = reservationDao.getShippingMethod(channel.getOrder_channel_id(), allotInventory.getOrder_number(), resId);

                                    // 发货渠道和默认发货渠道不一致时，更新发货渠道并插入Log
                                    if (!shipChannel.equals(ChannelConfigs.getVal1(channel.getOrder_channel_id(), ChannelConfigEnums.Name.default_ship_channel))) {
                                        $info(channel.getFull_name() + "----------(订单级别)发货渠道变更：" + shipChannel);
                                        reservationDao.updateShipChannel(allotInventory.getOrder_number(), resId, shipChannel);
                                        List<Long> reservationLogList = new ArrayList<>();
                                        reservationLogList.add(resId);
                                        reservationLogDao.insertReservationLog(reservationLogList, "ShipChannel change to " + shipChannel, getTaskName());
                                    }
                                }

                                // 判断是否需要拆单
                                // 拆单的场合，将物品取消、插入Log并插入订单备注
                                if (isSplitOrder(channel.getOrder_channel_id(), storeList)) {
                                    $info(channel.getFull_name() + "----------Order_Number：" + allotInventory.getOrder_number() + "需要拆单");
                                    reservationDao.updateReservationStatus(allotInventory.getOrder_number(), reservationList, CodeConstants.Reservation_Status.Cancelled);

                                    String note = "Warehouse is different, need to split the order";
                                    reservationLogDao.insertReservationLog(reservationList, note, getTaskName());

                                    reservationDao.insertOrderNotes(allotInventory.getOrder_number(), allotInventory.getLstReservation().get(0).getSource_order_id(), note, getTaskName());

                                    errorAllotInventoryDetailList.add(allotInventory);
                                }


                            } catch (Exception e) {
                                $info(channel.getFull_name() + "----------(订单级别)库存分配错误，Order_Number：" + allotInventory.getOrder_number() + "，ErrorMessage：" + e);
                                logIssue(channel.getFull_name() + "----------(订单级别)库存分配错误，Order_Number：" + allotInventory.getOrder_number(), e);

                                throw new RuntimeException(e);
                            }
                        }
                    });
                }

            }

            String errorMail = sendErrorMail(errorAllotInventoryDetailList);

            if (!StringUtils.isNullOrBlank2(errorMail)) {
                $info("错误邮件出力");
                String subject = String.format(WmsConstants.EmailSetAllotInventoryErrorSpilt.SUBJECT, channel.getFull_name());
                Mail.sendAlert(CodeConstants.EmailReceiver.NEED_SOLVE, subject, errorMail, true);
            }

             $info(channel.getFull_name() + "库存分配结束");

        }

        //将wbservice返回的Json结果，转化成List<ItemCodeBean>
        private List<ItemCodeBean> proResult(ProductBean productBean) {
            List<ItemCodeBean> itemCodeInfoList = new ArrayList<>();
            ItemCodeBean itemCodeBean = new ItemCodeBean();
//            ProductBean productBean = JsonUtil.jsonToBean(result, ProductBean.class);
            //设置itemCodeBean
            itemCodeBean.setBrand(productBean.getBrandName());
            itemCodeBean.setGender(productBean.getGender());
            itemCodeBean.setHs_code(productBean.getHsCode());
            itemCodeBean.setHs_code_pu(productBean.getHsCodePu());
            itemCodeBean.setHs_description(productBean.getHsDescription());
            itemCodeBean.setHs_description_pu(productBean.getHsDescriptionPu());
            itemCodeBean.setProduct_type(productBean.getDescription());
            itemCodeBean.setSalePrice(productBean.getPrice());
            itemCodeBean.setPrice(productBean.getMsrp());
            itemCodeBean.setShortDescription(StringUtils.isNullOrBlank2(productBean.getShortDescription())? productBean.getName():productBean.getShortDescription());
            itemCodeBean.setOrigin(productBean.getCountryName());
            itemCodeBean.setUnit(productBean.getUnit());
            itemCodeBean.setWeight(productBean.getWeightlb());
            itemCodeBean.setUnit_pu(productBean.getUnitPu());
            itemCodeBean.setTitle(productBean.getCnName());
            itemCodeInfoList.add(itemCodeBean);
            return itemCodeInfoList;
        }
    }

    /**
     * 从CMS取得SKU信息
     *
     * @param orderChannelId 订单渠道
     * @param itemCode 产品Code
     * @param postURI 推送URL
     * @return response
     * @throws UnsupportedEncodingException
     */
    private String getItemCodeInfo(String orderChannelId, String itemCode, String sku, String postURI) throws UnsupportedEncodingException {

        String json = "";
        Map<String, Object> jsonMap = new HashMap<>();
        String result = "";

        // 使用新旧WebService的判断
        if(postURI.contains("aspx")) {

            jsonMap.put("channelId", orderChannelId);

            jsonMap.put("code", itemCode);

            json = new Gson().toJson(jsonMap);

            $info(json);

            result = HttpUtils.post(postURI, json);

        }
        // 使用新CMS的判断
        if(postURI.contains("sdk-client")) {

            ProductForWmsGetRequest requestModel = new ProductForWmsGetRequest(orderChannelId);
            requestModel.setCode(itemCode);

            //SDK取得Product 数据
            ProductForWmsGetResponse response = voApiClient.execute(requestModel);

            if (response.getResultInfo() != null ) {
                result = JsonUtil.getJsonString(response.getResultInfo());
            }

        }
        else {

            Map<String, Object> dataBodyMap = new HashMap<>();

            dataBodyMap.put("orderChannelId", orderChannelId);

            dataBodyMap.put("itemCode", itemCode);

//            String dataBody = new Gson().toJson(dataBodyMap);

            String timeStamp = DateTimeUtil.getNow();
            jsonMap.put("timeStamp", timeStamp);

            String prefixStr = "VoyageOne";
            jsonMap.put("signature", MD5.getMD5(prefixStr + timeStamp));

            jsonMap.put("dataBody", dataBodyMap);

            json = new Gson().toJson(jsonMap);

            $info(json);

            result = HttpUtils.post(postURI, json);

        }

        return result;

    }

    /**
     * Reservation表编辑
     * @param reservation reservation信息
     * @param itemCodeInfo SKU信息
     * @param storeId 分配仓库
     */
    private void setReservation(ReservationBean reservation, ItemCodeBean itemCodeInfo, long storeId) {

        // 获取当前日期
        String dateFormat = DateTimeUtil.getNow();

        // 仓库ID
        reservation.setStore_id(storeId);

        // 仓库名
        StoreBean store = StoreConfigs.getStore(storeId);
        String storeName;
        //如果是虚拟仓库的话，设置成CH；
        if (store.getStore_kind().equals(StoreConfigEnums.Kind.VIRTUAL.getId())) {
            storeName = WmsConstants.StoreName.CH;
        }
//        //如果该渠道是境外电商、并且仓库所属在国内的话，设置成TM；
//        else if (ChannelConfigs.getVal1(reservation.getOrder_channel_id(), ChannelConfigEnums.Name.sale_type).equals(ChannelConfigEnums.Sale.CB.getType()) &&
//                store.getStore_location().equals(StoreConfigEnums.Location.CN.getId())) {
//            storeName = WmsConstants.StoreName.CN;
//        }
        //以外的场合，设置成仓库ID所属的仓库名称
        else {
            storeName = store.getStore_name();
        }
        reservation.setStore(storeName);

        // 状态
        reservation.setStatus(CodeConstants.Reservation_Status.Open);

        // 参考
        reservation.setReference("");

        // 上传时间
        reservation.setUpload_time(dateFormat);

        // 日期
        reservation.setDate("");

        // 是否打印
        reservation.setPrinted("0");

        // 备注
        reservation.setNote("");

        // 源cart_id
        reservation.setSrcCart_id(reservation.getCart_id());

        // pos_processed
        reservation.setPos_processed("0");

        // 物流订单号
        reservation.setSyn_ship_no("");

        // 发送渠道
        reservation.setShip_channel(ChannelConfigs.getVal1(reservation.getOrder_channel_id(), ChannelConfigEnums.Name.default_ship_channel));

        // 报关价格
        reservation.setDeclare_price(0);

        // 物品详细信息
        if (StringUtils.isNullOrBlank2(itemCodeInfo.getTitle())) {
            reservation.setDescription_inner(reservation.getProduct());
        }
        else {
            reservation.setDescription_inner(StringUtils.null2Space2(itemCodeInfo.getTitle()));
        }

        // 物品简略信息
        if (StringUtils.isNullOrBlank2(itemCodeInfo.getShortDescription2())) {
            reservation.setDescription_short(StringUtils.null2Space2(itemCodeInfo.getShortDescription()));
        }
        else {
            reservation.setDescription_short(StringUtils.null2Space2(itemCodeInfo.getShortDescription2()));
        }

        // 物品信息
        reservation.setDescription(StringUtils.null2Space2(itemCodeInfo.getProduct_type()));

        // 物品品牌
        reservation.setBrand(StringUtils.null2Space2(itemCodeInfo.getBrand()));

        // 性别
        reservation.setGender(StringUtils.null2Space2(itemCodeInfo.getGender()));

        // 鞋帮材料
        // 鞋底材料
        if (StringUtils.null2Space2(itemCodeInfo.getProduct_type()).equals(WmsConstants.ProductType.SHOES)) {
            reservation.setUpper_material(WmsConstants.Material.LEATHER);
            reservation.setSole_material(WmsConstants.Material.RUBBER);
        }else {
            reservation.setUpper_material("");
            reservation.setSole_material("");
        }

        // 源产地
        reservation.setOrigin(StringUtils.null2Space2(itemCodeInfo.getOrigin()));

        // 物品单价
        reservation.setPrice(StringUtils.isNullOrBlank2(itemCodeInfo.getPrice())? 0 : Double.valueOf(itemCodeInfo.getPrice()));

        // 物品单价单位(国内电商时，设置成"RMB";境外电商时，设置成"USD")
        if (ChannelConfigs.getVal1(reservation.getOrder_channel_id(), ChannelConfigEnums.Name.sale_type).equals(ChannelConfigEnums.Sale.CN.getType())) {
            reservation.setPrice_unit(WmsConstants.CurrencyUnit.RMB);
        }else {
            reservation.setPrice_unit(WmsConstants.CurrencyUnit.USD);
        }

        // 物品原价
        reservation.setOriginal_price(StringUtils.isNullOrBlank2(itemCodeInfo.getPrice()) ? 0 : Double.valueOf(itemCodeInfo.getPrice()));

        // 物品原价单位
        reservation.setOriginal_price_unit(reservation.getPrice_unit());

        // 销售单价
        reservation.setSale_price(StringUtils.isNullOrBlank2(itemCodeInfo.getSalePrice()) ? 0 : Double.valueOf(itemCodeInfo.getSalePrice()));

        // 销售单价单位
        reservation.setSale_price_unit(WmsConstants.CurrencyUnit.RMB);

        // 物品重量（磅）
        reservation.setWeight_lb(StringUtils.isNullOrBlank2(itemCodeInfo.getWeight()) ? "0.0" : itemCodeInfo.getWeight());

        // 物品重量（公斤）
        reservation.setWeight_kg(lbToKg(itemCodeInfo.getWeight()));

        // 单位
        reservation.setUnit(StringUtils.null2Space2(itemCodeInfo.getUnit()));

        // 报关号
        reservation.setHs_code(StringUtils.null2Space2(itemCodeInfo.getHs_code()));

        // 报关号pu
        reservation.setHs_code_pu(StringUtils.null2Space2(itemCodeInfo.getHs_code_pu()));

        // 物品单位pu
        reservation.setUnit_pu(StringUtils.null2Space2(itemCodeInfo.getUnit_pu()));

        // 物品信息pu
        reservation.setHs_description_pu(StringUtils.null2Space2(itemCodeInfo.getHs_description_pu()));

        // 物品信息
        reservation.setHs_description(StringUtils.null2Space2(itemCodeInfo.getHs_description()));

        // 发送标志位
        reservation.setSent_flg("0");

        // Close标志位
        // 根据仓库判断库存是否需要管理
        StoreBean storebean = StoreConfigs.getStore(storeId);
        String inventory_manager = storebean.getInventory_manager();
        String closeDayFlg = WmsConstants.CloseDayFlg.Process;
        if (StoreConfigEnums.Manager.NO.getId().equals(inventory_manager)) {
            closeDayFlg = WmsConstants.CloseDayFlg.Done;
        }
        reservation.setClose_day_flg(closeDayFlg);

        // 创建时间
        reservation.setCreate_time(dateFormat);

        // 更新时间
        reservation.setUpdate_time(dateFormat);

        // 创建者
        reservation.setCreate_person(getTaskName());

        // 更新者
        reservation.setUpdate_person(getTaskName());

    }

    private static String lbToKg(String weightLb) {
        if (weightLb == null || "".equals(weightLb)) {
            weightLb = "0.0";
        }
        BigDecimal weightLbB = new BigDecimal(weightLb);
        BigDecimal weightKgB = weightLbB.multiply(new BigDecimal(0.4536));
        String weightKg =
                String.valueOf(new BigDecimal(weightKgB.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
        return weightKg;
    }

    /**
     * 产品属性是否设置完全的判断
     * @param order_channel_id
     * @param itemCodeInfo
     * @return
     */
    private boolean checkProductAttributes(String order_channel_id, ItemCodeBean itemCodeInfo) {

        boolean result = true;

        // 电商类型
        String sale_type = ChannelConfigs.getVal1(order_channel_id, ChannelConfigEnums.Name.sale_type);
        // 发货方式
        List<OrderChannelConfigBean> shippingMethodList = ChannelConfigs.getConfigs(order_channel_id, ChannelConfigEnums.Name.shipping_method);

        // 跨境电商时，需要判断行邮税号等产品属性是否已设定
        if (sale_type.equals(ChannelConfigEnums.Sale.CB.getType())) {

            if (shippingMethodList != null) {

                // 如果只有一个发货方式，并且发货方式是菜鸟时，则不做检查
                if (shippingMethodList.size() == 1 && shippingMethodList.get(0).getCfg_val1().equals(WmsConstants.ShipChannel.SYB)) {

                } else {
                    if (StringUtils.isNullOrBlank2(itemCodeInfo.getHs_code_pu()) ||
                            StringUtils.isNullOrBlank2(itemCodeInfo.getUnit_pu()) ||
                            StringUtils.isNullOrBlank2(itemCodeInfo.getHs_description_pu())) {
                        result = false;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 拆单判断处理
     * @param order_channel_id 订单渠道
     * @param storeList 仓库一览
     * @return 是否拆单
     */
    private boolean isSplitOrder(String order_channel_id, List<Long> storeList) {

        // 取得订单拆分
        List<OrderChannelConfigBean> order_split = ChannelConfigs.getConfigs(order_channel_id, ChannelConfigEnums.Name.order_split);

        boolean blnSpilt = false ;

        if (order_split != null) {
            for (OrderChannelConfigBean orderChannelConfigBean : order_split) {

                // 不同区域判断
                if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ORDER_SPLIT.AREA)) {

                    String store_area = "";
                    for (long store : storeList) {

                        if (!StringUtils.isNullOrBlank2(store_area) && !store_area.equals(StoreConfigs.getStore(store).getStore_area())) {
                            blnSpilt = true;
                            break;
                        }
                        store_area = StoreConfigs.getStore(store).getStore_area();
                    }

                    if (blnSpilt == true) {
                        break;
                    }

                }
                // 不同库存管理判断
                else if (orderChannelConfigBean.getCfg_val1().equals(WmsConstants.ORDER_SPLIT.INVENTORY)) {

                    String inventory_manager = "";
                    for (long store : storeList) {

                        if (!StringUtils.isNullOrBlank2(inventory_manager) && !inventory_manager.equals(StoreConfigs.getStore(store).getInventory_manager())) {
                            blnSpilt = true;
                            break;
                        }
                        inventory_manager = StoreConfigs.getStore(store).getInventory_manager();
                    }

                    if (blnSpilt == true) {
                        break;
                    }

                }

            }
        }



        return blnSpilt;
    }

    /**
     * 错误邮件出力
     * @param errorList 错误SKU一览
     * @return 错误邮件内容
     */
    private String sendErrorMail(List<AllotInventoryDetailBean> errorList) {

        StringBuilder builderContent = new StringBuilder();

        if (errorList.size() > 0) {

            StringBuilder builderDetail = new StringBuilder();

            int index = 0;
            for (AllotInventoryDetailBean error : errorList) {

                ReservationBean reservationBean = error.getLstReservation().get(0);

                List<String> storeName = new ArrayList<>();
                for (ReservationBean reservation : error.getLstReservation()) {
                    if (!storeName.contains(reservation.getStore())) {
                        storeName.add(reservation.getStore());
                    }

                }

                index = index + 1;
                builderDetail.append(String.format(WmsConstants.EmailSetAllotInventoryErrorSpilt.ROW,
                        index,
                        ShopConfigs.getShopNameDis(reservationBean.getOrder_channel_id(), reservationBean.getCart_id()),
                        reservationBean.getOrder_number(),
                        reservationBean.getSource_order_id(),
                        storeName.toString()));
            }

            String count = String.format(WmsConstants.EmailSetAllotInventoryErrorSpilt.COUNT, errorList.size());

            String detail = String.format(WmsConstants.EmailSetAllotInventoryErrorSpilt.TABLE, count, builderDetail.toString());

            builderContent
                    .append(Constants.EMAIL_STYLE_STRING)
                    .append(WmsConstants.EmailSetAllotInventoryErrorSpilt.HEAD)
                    .append(detail);


        }

        return builderContent.toString();
    }

}
