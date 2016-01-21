package com.voyageone.batch.wms.service;

import com.google.gson.Gson;
import com.voyageone.batch.base.BaseTaskService;
import com.voyageone.batch.core.CodeConstants;
import com.voyageone.batch.core.Enums.TaskControlEnums;
import com.voyageone.batch.core.modelbean.TaskControlBean;
import com.voyageone.batch.core.util.TaskControlUtils;
import com.voyageone.batch.wms.WmsConstants;
import com.voyageone.batch.wms.dao.ReservationDao;
import com.voyageone.batch.wms.dao.ReservationLogDao;
import com.voyageone.batch.wms.modelbean.CmsWsdlResponseBean;
import com.voyageone.batch.wms.modelbean.ItemCodeBean;
import com.voyageone.batch.wms.modelbean.ProductBean;
import com.voyageone.batch.wms.modelbean.ReservationBean;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.configs.ChannelConfigs;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.Enums.StoreConfigEnums;
import com.voyageone.common.configs.StoreConfigs;
import com.voyageone.common.configs.beans.OrderChannelBean;
import com.voyageone.common.configs.beans.OrderChannelConfigBean;
import com.voyageone.common.configs.beans.StoreBean;
import com.voyageone.common.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WmsAllotInventoryService extends BaseTaskService {

    @Autowired
    ReservationDao reservationDao;

    @Autowired
    ReservationLogDao reservationLogDao;

    @Autowired
    private TransactionRunner transactionRunner;

    @Override
    public SubSystem getSubSystem() {
        return SubSystem.WMS;
    }

    @Override
    public String getTaskName() {
        return "WmsAllotInventoryJob";
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

        // 根据订单渠道运行
        for (final String orderChannelID : orderChannelIdList) {

            final String postURI = TaskControlUtils.getVal2(taskControlList, TaskControlEnums.Name.order_channel_id, orderChannelID);

            final int finalIntRowCount = intRowCount;

            threads.add(() -> new setAllotInventory(orderChannelID, postURI, finalIntRowCount).doRun());

        }

        runWithThreadPool(threads, taskControlList);

    }

    /**
     * 按渠道进行模拟入出库
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

        public void doRun() {
            logger.info(channel.getFull_name()+"第三方库存分配开始" );

            //分配仓库取得（如果能够取得相应的值，则说明只是模拟分配而已，不再对一些信息进行检查）
            String allot_store = ChannelConfigs.getVal1(channel.getOrder_channel_id(),ChannelConfigEnums.Name.allot_store);

            if (StringUtils.isNullOrBlank2(allot_store)) {
//                // ItemDetail表中没有的记录取得
//                List<String> notExistsItemCode = reservationDao.getNotExistsItemCode(channel.getOrder_channel_id());
//
//                logger.info(channel.getFull_name() + "----------SKU在ItemDetail中不存在件数：" + notExistsItemCode.size());
//                if (notExistsItemCode.size() > 0) {
//                    logIssue(channel.getFull_name() + "：SKU在ItemDetail中不存在，无法库存分配", notExistsItemCode);
//                }
                logger.info(channel.getFull_name() + "----------分配仓库未指定，无法模拟分配");
                logIssue(channel.getFull_name() + "：分配仓库未指定，无法模拟分配");
                return;
            }

            // 需要库存分配的记录取得
            final List<ReservationBean> reservationList = reservationDao.getAllotInventoryInto(channel.getOrder_channel_id(), rowCount);

            logger.info(channel.getFull_name() + "----------第三方库存分配件数：" + reservationList.size());

            for (final ReservationBean reservation : reservationList) {

                 transactionRunner.runWithTran(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            logger.info(channel.getFull_name() + "----------Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku());

                            ItemCodeBean itemCodeInfo = new ItemCodeBean();

                            // 防止Code情报取不到，将金额设置为产品售价
                            itemCodeInfo.setPrice(StringUtils.isNullOrBlank2(reservation.getPrice_per_unit()) || reservation.getPrice_per_unit().equals("0.00") ? "1" : reservation.getPrice_per_unit());
                            itemCodeInfo.setSalePrice(StringUtils.isNullOrBlank2(reservation.getPrice_per_unit()) || reservation.getPrice_per_unit().equals("0.00") ? "1" : reservation.getPrice_per_unit());

                            if (!StringUtils.isNullOrBlank2(postURI) && !StringUtils.isNullOrBlank2(reservation.getItemCode())) {

                                // SKU信息取得
                                String result = getItemCodeInfo(channel.getOrder_channel_id(), reservation.getItemCode(), postURI);
                                logger.info(channel.getFull_name() + "----------getItemCodeInfo：" + result);

                                // 调用WebService时返回为空时，抛出错误
                                if (StringUtils.isNullOrBlank2(result)) {
                                    logIssue(channel.getFull_name() + "第三方库存分配时SKU信息取得失败" + "，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number()+ "，SKU：" + reservation.getSku());
                                } else {

                                    List<ItemCodeBean> itemCodeInfoList = new ArrayList<>();
                                    String errorMessage = "";

                                    // 老的webService（调用老的webService, Spling）
                                    if (postURI.contains("aspx")) {
                                        itemCodeInfoList = JsonUtil.jsonToBeanList(result, ItemCodeBean.class);
                                        // 新的webService（Jewelry）
                                    } else {
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
                                        logIssue(channel.getFull_name() + "第三方库存分配时SKU信息未取得", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，ErrorMessage：" + errorMessage);
                                    } else {
                                        itemCodeInfo = itemCodeInfoList.get(0);
                                    }
                                }
                            }

                            // TODO 聚美税号等信息取得预定


                            // 产品属性的检查
                            boolean productAttributes = true;
                            if (!StringUtils.isNullOrBlank2(reservation.getItemCode())) {
                                checkProductAttributes(channel.getOrder_channel_id(), itemCodeInfo);
                            }

                            // 产品属性设置完全时，允许分配
                            if (productAttributes) {
                                // 分配仓库
                                long storeId = 0;

                                if (StringUtils.isNullOrBlank2(allot_store)) {
                                    storeId = reservationDao.getAllotStore(channel.getOrder_channel_id(), reservation.getCart_id(), reservation.getSku());
                                }else {
                                    storeId = Long.valueOf(allot_store);
                                }

                                logger.info(channel.getFull_name() + "----------getAllotStore：" + storeId);

                                // 仓库分配没有取得
                                if (storeId == 0) {
                                    logIssue(channel.getFull_name() + "第三方库存分配时仓库取得失败", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Store：" + storeId);
                                } else {

                                    // Reservation表编辑
                                    setReservation(reservation, itemCodeInfo, storeId);

                                    // 插入tt_reservation表
                                    long res_id = reservationDao.insertReservation(reservation);

                                    if (res_id == 0) {
                                        logger.info(channel.getFull_name() + "----------第三方库存分配失败：" + res_id);
                                        logIssue(channel.getFull_name() + "第三方库存分配时插入Reservation表失败", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku());
                                    } else {

                                        // 更新OrderDetails
                                        reservationDao.updateOrderDetails(reservation.getOrder_number(), reservation.getItem_number(), res_id, getTaskName());

                                        // 插入ReservationLog
                                        List<Long> reservationList = new ArrayList<>();
                                        reservationList.add(res_id);
                                        reservationLogDao.insertReservationLog(reservationList, "AllotInventory", getTaskName());

                                        logger.info(channel.getFull_name() + "----------第三方库存分配成功：" + res_id);
                                    }
                                }
                            } else {
                                String json = itemCodeInfo == null ? "" : new Gson().toJson(itemCodeInfo);
                                logger.info(channel.getFull_name() + "----------产品属性设置不全");
                                logIssue(channel.getFull_name() + "税号等产品属性设置不全，无法第三方库存分配", "Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number() + "，SKU：" + reservation.getSku() + "，Attributes：" + json);
                            }

                        } catch (Exception e) {
                            logIssue(channel.getFull_name() + "----------第三方库存分配错误，Order_Number：" + reservation.getOrder_number() + "，Item_Number：" + reservation.getItem_number()+ "，SKU：" + reservation.getSku(),e);

                            throw new RuntimeException(e);
                        }
                    }
                });

            }

            logger.info(channel.getFull_name() + "第三方库存分配结束");

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
            itemCodeBean.setShortDescription(productBean.getShortDescription());
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
    private String getItemCodeInfo(String orderChannelId, String itemCode, String postURI) throws UnsupportedEncodingException {

        String json = "";
        Map<String, Object> jsonMap = new HashMap<>();

        // 使用新旧WebService的判断
        if(postURI.contains("aspx")) {

            jsonMap.put("channelId", orderChannelId);

            jsonMap.put("code", itemCode);


        } else {

            Map<String, Object> dataBodyMap = new HashMap<>();

            dataBodyMap.put("orderChannelId", orderChannelId);

            dataBodyMap.put("itemCode", itemCode);

//            String dataBody = new Gson().toJson(dataBodyMap);

            String timeStamp = DateTimeUtil.getNow();
            jsonMap.put("timeStamp", timeStamp);

            String prefixStr = "VoyageOne";
            jsonMap.put("signature", MD5.getMD5(prefixStr + timeStamp));

            jsonMap.put("dataBody", dataBodyMap);

        }

        json = new Gson().toJson(jsonMap);

        logger.info(json);
        return HttpUtils.post(postURI, json);

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

}
