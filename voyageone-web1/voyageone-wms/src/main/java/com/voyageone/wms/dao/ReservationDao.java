package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.util.StringUtils;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.wms.formbean.FormPickupBean;
import com.voyageone.wms.formbean.FormReservation;
import com.voyageone.wms.formbean.PickedInfoBean;
import com.voyageone.wms.modelbean.ReservationBean;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**   
 * Simple to Introduction  
 * @Package      [com.voyageone.wms.dao]  
 * @ClassName    [ReservationDao]   
 * @Description  [Reservation Dao类]   
 * @Author       [sky]   
 * @CreateDate   [20150421]   
 * @UpdateUser   [${user}]   
 * @UpdateDate   [${date} ${time}]   
 * @UpdateRemark [说明本次修改内容]  
 * @Version      [v1.0] 
 */
@Repository
public class ReservationDao extends BaseDao {

    /**
     * @Description 获得reservationLog
     * @param formReservation  参数
     * @return List<FromReservation>
     */
    public List<FormReservation> getReservationLog(FormReservation formReservation) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getReservationLogInfo", formReservation);
    }

    /**
     * @Description 获得reservationList
     * @param formReservation 对象
     * @return List<FromReservation>
     */
	public List<FormReservation> getReservationInfo(FormReservation formReservation) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getReservationInfo", formReservation);
	}

    /**
     * @Description 获得是否允许变更仓库
     * @param params 对象
     * @return List<FromReservation>
     */
    public String getChangeStoreFlg(FormReservation params) {
        String change_store_flg = "1";
        int result = selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getChangeStoreFlg", params);

        if (result > 0) {
            change_store_flg = "0";
        }

        return change_store_flg;

    }

    /**
     * @Description 获得reservationList
     * @param formReservation 对象
     * @return List<FromReservation>
     */
	public int getReservationListSize(FormReservation formReservation) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getReservationListSize", formReservation);
	}

    /**
     * 获得捡货信息一览
     * @param params 检索条件
     * @return List<FormReservation>
     */
    public List<FormReservation> getPickupInfo(Map<String, Object> params) {

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "viw_wms_reservation_pickup_mapping_getPickupInfo", params);
    }

    /**
     * 获得捡货信息一览的件数
     * @param params 检索条件
     * @return long 件数
     */
    public int getPickupCount(Map<String, Object> params) {

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "viw_wms_reservation_pickup_mapping_getPickupCount", params);
    }

    /**
     * 获得捡货信息一览
     * @param scanMode 画面上选择的扫描方式
     * @param scanType 扫描类型
     * @param scanNo 输入的scanNo
     * @param scanStatus 物品的状态
     * @param scanStore 扫描时的仓库
     * @param storeList 用户所属的仓库
     * @param orderChannelList 用户所属的ChannelID
     * @return List<FormPickupBean>
     */
    public List<FormPickupBean> getScanInfo(String scanMode, String scanType, String scanNo, String scanStatus, String scanStore, List<ChannelStoreBean>  storeList, List<String> orderChannelList, String reserveType) {

        Map<String, Object> params = new HashMap<>();

        params.put("scanMode", scanMode);
        params.put("scanType", scanType);
        params.put("scanNo", scanNo);
        params.put("scanStatus", scanStatus);
        params.put("scanStore", StringUtils.isNullOrBlank2(scanStore)? "0" :scanStore);
        params.put("storeList", storeList);
        params.put("orderChannelList", orderChannelList);
        params.put("reserveType", reserveType);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "viw_wms_reservation_pickup_mapping_getScanInfo", params);

    }

    /**
     *  获得港口信息
     * @param syn_ship_no Synship物流号
     * @param id 物品ID
     * @returnString
     */
    public String getPort(String syn_ship_no, long id) {

        Map<String, Object> params = new HashMap<>();

        params.put("syn_ship_no", syn_ship_no);
        params.put("id", id);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "tt_reservation_getPort", params);

    }

    /**
     *  获得订单的物品数
     * @param order_number 订单号
     * @param scanType 扫描类型
     * @return list sku
     */
    public List<String> getOrderProductList(long order_number, String scanType) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_number", order_number);
        params.put("scanType", scanType);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "tt_reservation_getProductNum", params);

    }

    /**
     * 发货渠道的取得
     * @param order_channel_id 订单渠道
     * @param order_number 订单号
     * @param reservationId
     * @return 物品ID
     */
    public String getShippingMethod(String order_channel_id, long order_number, long reservationId){

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("order_number", order_number);
        params.put("reservationId", reservationId);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "tt_reservation_getShippingMethod", params);

    }

    /**
     * 更新捡货状态及发货渠道
     * @param reservationList 捡货一览
     * @param scanStatus 扫描时状态
     * @param updateStatus 更新状态
     * @param shipChannel 发货渠道
     * @param price 换算后的金额
     * @param closeDayFlg 是否计算库存
     * @param userName 更新者
     * @return int
     */
    public int updatePickupStatus(List<Long>reservationList, String scanStatus, String updateStatus, String shipChannel, BigDecimal price, String closeDayFlg, String userName,String scanType,String scanMode) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationList", reservationList);
        params.put("scanStatus", scanStatus);
        params.put("updateStatus", updateStatus);
        params.put("shipChannel", shipChannel);
        params.put("price", new BigDecimal(price.doubleValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
        params.put("closeDayFlg", closeDayFlg);
        params.put("userName", userName);
        params.put("scanType", scanType);
        params.put("scanMode", scanMode);

        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "tt_reservation_updatePickupStatus", params);
    }

    /**
     * 更新状态
     * @param reservationInfo 更新记录
     * @return int
     */
    public int changeReservation(ReservationBean reservationInfo) {

        Map<String, Object> params = new HashMap<>();

        params.put("reservationInfo", reservationInfo);

        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "tt_reservation_changeReservation", params);
    }


    /**
     * @Description 获得reservation
     * @param reservationId 对象
     * @return FromReservation
     */
    public FormReservation getReservation(long reservationId) {
        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getReservation", reservationId);
    }

    /**
     * @description 根据sku和orderChannelId获取itemCode等产品信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public FormReservation getProductInfo(FormReservation formReservation) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getProductInfo", formReservation);
    }

    /**
     * @description 根据itemCode和orderChannelId获取itemCode下面所有Sku级别产品的库存信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public int getProductInventoryCount(FormReservation formReservation) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getProductInventoryCount", formReservation);
    }

    /**
     * @description 根据itemCode和orderChannelId获取itemCode下面所有Sku级别产品的库存信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public List<FormReservation> getProductInventory(FormReservation formReservation) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getProductInventory", formReservation);
    }

    /**
     * @description 根据order_channel_id获取对应的仓库信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public List<FormReservation> getStoreByChannelId(FormReservation formReservation) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getStoreByChannelId", formReservation);
    }

    /**
     * @description 根据order_channel_id、code、sku,和时间范围获取对应sku的产品的库存变更信息的件数
     * @param formReservation 通过bean对象传递参数
     * @return int 对象获取传回参数
     */
    public int getSkuHisListCount(FormReservation formReservation) {
        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getSkuHisListCount", formReservation);
    }

    /**
     * @description 根据order_channel_id、code、sku,和时间范围获取对应sku的产品的库存变更信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public List<FormReservation> getSkuHisList(FormReservation formReservation) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getSkuHisList", formReservation);
    }

    /**
     * 获得可捡货信息一览（按照订单物品）

     * @param status 物品的状态
     * @param storeList 用户所属的仓库
     * @param orderChannelList 用户所属的ChannelID
     * @return List<FormPickupBean>
     */
    public List<FormPickupBean> downloadPickupItemsByReservation(String status, List<ChannelStoreBean>  storeList, List<String> orderChannelList) {

        Map<String, Object> params = new HashMap<>();

        params.put("status", status);
        params.put("storeList", storeList);
        params.put("orderChannelList", orderChannelList);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_downloadPickupItemsByReservation", params);

    }

    /**
     * 获得可捡货信息一览（按照SKU）

     * @param status 物品的状态
     * @param storeList 用户所属的仓库
     * @param orderChannelList 用户所属的ChannelID
     * @return List<FormPickupBean>
     */
    public List<FormPickupBean> downloadPickupItemsBySKU(String status, List<ChannelStoreBean>  storeList, List<String> orderChannelList) {

        Map<String, Object> params = new HashMap<>();

        params.put("status", status);
        params.put("storeList", storeList);
        params.put("orderChannelList", orderChannelList);

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_downloadPickupItemsBySKU", params);

    }

    /**
     * 获得可捡货SKU所属的货架

     * @param order_channel_id 物品的状态
     * @param sku 用户所属的仓库
     * @param store_id 用户所属的ChannelID
     * @return List<FormPickupBean>
     */
    public String getLocationBySKU(String order_channel_id,String  sku,String store_id) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("sku", sku);
        params.put("store_id", store_id);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getLocationBySKU", params);

    }

    /**
     * 获得可品牌方SKU

     * @param order_channel_id 物品的状态
     * @param sku 用户所属的仓库
     * @return String
     */
    public String getClientSku(String order_channel_id,String  sku) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("sku", sku);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getClientSKU", params);

    }

    /**
     * @description 根据order_channel_id、code、sku,和时间范围获取对应sku的产品的库存变更信息
     * @param formReservation 通过bean对象传递参数
     * @return formReservation 对象获取传回参数
     */
    public int getCurQty(FormReservation formReservation) {
        try {
            return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_reservation_getCurQty", formReservation);
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * 删除逻辑库存

     * @param order_channel_id 物品的状态
     * @param sku 用户所属的仓库
     */
    public int deleteInventoryInfo(String order_channel_id, String sku) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("sku", sku);

        return updateTemplate.delete(Constants.DAO_NAME_SPACE_WMS + "inventory_center_logic_deleteSKU", params);

    }

    /**
     * 获得已捡货信息一览

     * @param params
     * @return List<PickedInfoBean>
     */
    public List<PickedInfoBean> downloadPickedItemsByReservation(Map<String, Object> params) {

        return updateTemplate.selectList(Constants.DAO_NAME_SPACE_WMS + "reservation_getPickedInfo", params);

    }

    /**
     * 获得内部订单号
     * @param orderChannelList 用户所属的ChannelID
     * @param scanNo 输入的scanNo
     * @return String
     */
    public String getOrderNumber(List<String> orderChannelList, String scanNo) {

        Map<String, Object> params = new HashMap<>();

        params.put("scanNo", scanNo);
        params.put("orderChannelList", orderChannelList);

        return updateTemplate.selectOne(Constants.DAO_NAME_SPACE_WMS + "order_getOrderNumber", params);

    }
}
