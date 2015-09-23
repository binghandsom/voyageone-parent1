package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.OrderUpdateBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ClientOrderDao extends BaseDao {

    /**
     * 根据第三方order文件更新tt_orders表状态
     * @param params 参数
     * @return int 更新记录条数
     */
    public int updateOrders(Map<String, Object> params) {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateTTOrdersStatus", params);
    }

    /**
     * 根据第三方order文件更新tt_reservation表状态
     * @param params 参数
     * @return int 更新记录条数
     */
    public int updateReservation(Map<String, Object> params) {
        return updateTemplate.update(Constants.DAO_NAME_SPACE_WMS + "wms_updateTTReservationStatus", params);
    }

    /**
     * 插入第三方order数据文件相关数据前检查 tt_res_tracking表是否已经存在对应的记录
     * @param paramMap 参数
     * @return list<OrderUpdateBean> 在tt_res_tracking表中已经存在的对应记录
     */
    public List<OrderUpdateBean> insertTTResTrackingCheck(Map<String, Object> paramMap) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTResTracking_check", paramMap);
    }

    /**
     * 插入数据到tt_res_traking表
     * @param paramMap 参数
     * @return int 插入记录条数
     */
    public int insertTTResTracking(Map<String, Object> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTResTracking", paramMap);
    }

    /**
     * 插入数据到tt_traking表check
     * @param paramMap 参数
     * @return list<OrderUpdateBean> 在tt_tracking表中已经存在的对应记录
     */
    public List<OrderUpdateBean> insertTTTrackingCheck(Map<String, Object> paramMap) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTTracking_check", paramMap);
    }

    /**
     * 插入数据到tt_traking表
     * @param paramMap 参数
     * @return int 插入记录条数
     */
    public int insertTTTracking(Map<String, Object> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTTracking", paramMap);
    }

    /**
     * 插入数据到 tt_traking_info 表check
     * @param paramMap 参数
     * @return list<OrderUpdateBean> 在tt_tracking_info表中已经存在的对应记录
     */
    public List<OrderUpdateBean> insertTTTrackingInfoCheck(Map<String, Object> paramMap) {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTTrackingInfo_check", paramMap);
    }

    /**
     * 插入数据到 tt_traking_info 表
     * @param paramMap 参数
     * @return int 插入记录条数
     */
    public int insertTTTrackingInfo(Map<String, Object> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertTTTrackingInfo", paramMap);
    }

    /**
     * 插入数据到 wms_bt_reservation_log 表
     * @param paramMap 参数
     * @return int 插入记录条数
     */
    public int insertReservationLog(Map<String, Object> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_insertReservationLog", paramMap);
    }

    /**
     * 插入数据到 更新tt_reservation 表的状态为 backorder
     * @param paramMap 参数
     * @return int 插入记录条数
     */
    public int updateTTresStat2BackOrder(Map<String, Object> paramMap) {
        return updateTemplate.insert(Constants.DAO_NAME_SPACE_WMS + "wms_updateTTResStat2Backorder", paramMap);
    }

}
