package com.voyageone.service.impl.vms.order;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.vms.VmsBtOrderDetailDao;
import com.voyageone.service.dao.vms.VmsBtOrderLogDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtOrderLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * service about product's status
 * Created by vantis on 16-7-6.
 */
@Service
@VOTransactional
public class OrderDetailService extends BaseService {

    public final static String STATUS_OPEN = "1";
    public final static String STATUS_PACKAGED = "2";
    public final static String STATUS_SHIPPED = "3";
    public final static String STATUS_RECEIVED = "5";
    public final static String STATUS_CANCEL = "7";

    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;
    private VmsBtOrderDetailDao vmsBtOrderDetailDao;
    private VmsBtOrderLogDao vmsBtOrderLogDao;

    @Autowired
    public OrderDetailService(VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt,
                              VmsBtOrderDetailDao vmsBtOrderDetailDao, VmsBtOrderLogDao vmsBtOrderLogDao) {
        this.vmsBtOrderDetailDaoExt = vmsBtOrderDetailDaoExt;
        this.vmsBtOrderDetailDao = vmsBtOrderDetailDao;
        this.vmsBtOrderLogDao = vmsBtOrderLogDao;
    }

    /**
     * 查询订单
     *
     * @param orderSearchParams 搜索条件
     * @return 订单详情
     */
    public List<VmsBtOrderDetailModel> selectOrderList(Map<String, Object> orderSearchParams) {

        return vmsBtOrderDetailDaoExt.orderDetailselectList(orderSearchParams);
    }

    /**
     * 自条件搜索订单号
     *
     * @param orderSearchParams 搜索条件
     * @return 订单号List
     */
    public List<String> selectPlatformOrderIdList(Map<String, Object> orderSearchParams) {

        return vmsBtOrderDetailDaoExt.orderIdselectList(orderSearchParams);

    }

    /**
     * 获取订单总量
     *
     * @param orderSearchParamsWithLimitAndSort 搜索条件
     * @return 订单总量
     */
    public long getTotalOrderNum(Map<String, Object> orderSearchParamsWithLimitAndSort) {

        return vmsBtOrderDetailDaoExt.countOrder(orderSearchParamsWithLimitAndSort);
    }

    /**
     * 记录orderInfo状态的变更
     *
     * @param changeStatusParams 更新条件
     */
    private void logOrderDetails(Map<String, Object> changeStatusParams) {
        List<VmsBtOrderDetailModel> originalOrderModelList = this.select(changeStatusParams);
        originalOrderModelList.stream()
                .map(vmsBtOrderDetailModel -> new VmsBtOrderLogModel() {{
                    setChannelId(vmsBtOrderDetailModel.getChannelId());
                    setReservationId(vmsBtOrderDetailModel.getReservationId());
                    setStatus(vmsBtOrderDetailModel.getStatus());
                    setCreater(vmsBtOrderDetailModel.getModifier());
                }})
                .forEach(vmsBtOrderLogDao::insert);
    }

    /**
     * 更新订单状态
     *
     * @param channelId            channelId
     * @param consolidationOrderId 订单号
     * @param status               待更新状态
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateOrderStatus(String channelId, String consolidationOrderId, String status, String modifier) {
        return updateOrderStatus(channelId, consolidationOrderId, status, modifier, new Date(), modifier);
    }

    /**
     * 更新订单状态
     *
     * @param channelId            channelId
     * @param consolidationOrderId 订单号
     * @param status               待更新状态
     * @param operateTime          操作时间
     * @param operator             操作着
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateOrderStatus(String channelId, String consolidationOrderId, String status, String modifier, Date operateTime, String operator) {

        Map<String, Object> changeStatusParams = new HashMap<String, Object>() {{
            // 更新条件
            put("channelId", channelId);
            put("consolidationOrderId", consolidationOrderId);
            // 更新内容
            put("status", status);
            put("modifier", modifier);
            if (STATUS_CANCEL.equals(status)) {
                put("containerizingTimeNull", "1");
                put("containerizerNull", "1");
                put("shipmentIdNull", "1");
                put("cancelTime", operateTime);
                put("canceler", operator);
            } else if (STATUS_SHIPPED.equals(status)) {
                put("shipmentTime", operateTime);
            } else if (STATUS_RECEIVED.equals(status)) {
                put("receivedTime", operateTime);
                put("receiver", operator);
            }
        }};

        int count = vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParams);

        // 记录订单变更状态
        if (count > 0) {
            this.logOrderDetails(changeStatusParams);
        }

        return count;
    }

    /**
     * 更新订单状态
     *
     * @param channelId     channelId
     * @param reservationId reservation ID
     * @param status        待更新状态
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateReservationStatus(String channelId, String reservationId, String status, String modifier) {
        return updateReservationStatus(channelId, reservationId, status, modifier, new Date(), modifier);
    }

    /**
     * 更新订单状态
     *
     * @param channelId     channelId
     * @param reservationId 物品id
     * @param status        待更新状态
     * @param operateTime   操作时间
     * @param operator      操作着
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateReservationStatus(String channelId, String reservationId, String status, String modifier, Date operateTime, String operator) {

        Map<String, Object> changeStatusParams = new HashMap<String, Object>() {{
            // 更新条件
            put("channelId", channelId);
            put("reservationId", reservationId);
            // 更新内容
            put("status", status);
            put("modifier", modifier);
            if (STATUS_CANCEL.equals(status)) {
                put("containerizingTimeNull", "1");
                put("containerizerNull", "1");
                put("shipmentIdNull", "1");
                put("cancelTime", operateTime);
                put("canceler", operator);
            } else if (STATUS_SHIPPED.equals(status)) {
                put("shipmentTime", operateTime);
            } else if (STATUS_RECEIVED.equals(status)) {
                put("receivedTime", operateTime);
                put("receiver", operator);
            }
        }};

        int count = vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParams);

        // 记录订单变更状态
        if (count > 0) {
            this.logOrderDetails(changeStatusParams);

        }

        return count;
    }

    /**
     * 条件查询sku总数
     *
     * @param skuSearchParamsWithLimitAndSort 搜索条件
     * @return sku总数
     */
    public long getTotalSkuNum(Map<String, Object> skuSearchParamsWithLimitAndSort) {

        return vmsBtOrderDetailDaoExt.countSku(skuSearchParamsWithLimitAndSort);
    }

    /**
     * 查找记录
     *
     * @param searchParam 搜索条件
     * @return 订单列表
     */
    public List<VmsBtOrderDetailModel> select(Map<String, Object> searchParam) {
        return vmsBtOrderDetailDao.selectList(searchParam);
    }

    /**
     * 查找OrderInfo
     *
     * @param searchParam 搜索条件
     * @return 订单列表
     */
    public List<Map<String, Object>> getOrderInfo(Map<String, Object> searchParam) {
        return vmsBtOrderDetailDaoExt.selectListByTime(searchParam);
    }

    /**
     * 新加一条OrderInfo
     *
     * @param model VmsBtOrderDetailModel
     * @return 增加件数
     */
    @VOTransactional
    public int insertOrderInfo(VmsBtOrderDetailModel model) {
        int count = vmsBtOrderDetailDao.insert(model);
        if (count == 1) {
            VmsBtOrderLogModel logModel = new VmsBtOrderLogModel();
            logModel.setChannelId(model.getChannelId());
            logModel.setStatus(model.getStatus());
            logModel.setReservationId(model.getReservationId());
            logModel.setCreater(model.getCreater());
            logModel.setModifier(model.getModifier());
            vmsBtOrderLogDao.insert(logModel);
        }
        return count;
    }

    public List<VmsBtOrderDetailModel> getScannedSku(String channelId, String shipmentId) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("shipmentId", shipmentId);
        }};

        Map<String, Object> modifiedParams = MySqlPageHelper.build(params)
                .addSort("containerizing_time", Order.Direction.DESC)
                .toMap();
        return vmsBtOrderDetailDao.selectList(modifiedParams);
    }

    public List<VmsBtOrderDetailModel> getScannedSku(String channelId, int shipmentId, String
            consolidationOrderId) {

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("shipmentId", shipmentId);
            put("consolidationOrderId", consolidationOrderId);
        }};

        Map<String, Object> modifiedParams = MySqlPageHelper.build(params)
                .addSort("containerizing_time", Order.Direction.DESC)
                .toMap();
        return vmsBtOrderDetailDao.selectList(modifiedParams);
    }

    public int scanInSku(String channelId, String userName, String barcode, int shipmentId, String shipmentStatus) {

        // 查找该channel下 扫描barcode对应的SKU(状态必须为OPEN)
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("barcode", barcode);
            put("status", STATUS_OPEN);
        }};

        int count = 0;

        Map<String, Object> sortedParams = MySqlPageHelper.build(params)
                .addSort("consolidation_order_time", Order.Direction.ASC)
                .limit(1)
                .toMap();
        List<VmsBtOrderDetailModel> vmsBtOrderDetailModelList = vmsBtOrderDetailDao.selectList(sortedParams);
        VmsBtOrderDetailModel vmsBtOrderDetailModel = (null != vmsBtOrderDetailModelList
                && vmsBtOrderDetailModelList.size() > 0) ? vmsBtOrderDetailModelList.get(0) : null;

        // 若有 则更新状态 置入shipment信息
        if (null != vmsBtOrderDetailModel) {
            Map<String, Object> updateParams = new HashMap<String, Object>() {{
                put("channelId", channelId);
                put("reservationId", vmsBtOrderDetailModel.getReservationId());
                put("containerizer", userName);
                put("modifier", userName);
                put("shipmentId", shipmentId);
                if (STATUS_SHIPPED.equals(shipmentStatus)) {
                    put("status", STATUS_SHIPPED);
                    put("shipmentTime", new Date());
                } else {
                    put("status", STATUS_PACKAGED);
                }
            }};

            count = vmsBtOrderDetailDaoExt.updateOrderStatus(updateParams);
            if (count > 0) {
                this.logOrderDetails(updateParams);
            }
//            count = this.updateReservationStatus(channelId, vmsBtOrderDetailModel.getReservationId(),
//                    STATUS_PACKAGED, userName);
        }

        return count;
    }


    public int scanInOrder(String channelId, String userName, String barcode, String orderId, int shipmentId) {

        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("barcode", barcode);
            put("consolidationOrderId", orderId);
            put("status", STATUS_OPEN);// Open
            put("containerizer", userName);
            put("modifier", userName);
            put("shipmentId", shipmentId);
        }};

        return vmsBtOrderDetailDaoExt.updateShipmentStatusInOrder(params);
    }

    public int removeSkuShipmentId(String channelId, Integer shipmentId, String userName) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("shipmentId", shipmentId);
            put("modifier", userName);
        }};
        return vmsBtOrderDetailDaoExt.cancelOrderShipmentStatus(params);
    }

    public int removeSkuOrderId(String channelId, String consolidationOrderId, String userName) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("consolidationOrderId", consolidationOrderId);
            put("modifier", userName);
        }};
        return vmsBtOrderDetailDaoExt.cancelOrderShipmentStatus(params);
    }

    public int updateOrderStatusWithShipmentId(String channelId, Integer shipmentId, String status, Date operateTime,
                                               String operator) {

        // 更新status
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("wShipmentId", shipmentId); // 更新的where条件
            put("shipmentTime", new Date());
            put("status", status);

            if (STATUS_CANCEL.equals(status)) {
                put("containerizingTimeNull", "1");
                put("containerizerNull", "1");
                put("shipmentIdNull", "1");
                put("cancelTime", operateTime);
                put("canceler", operator);
            } else if (STATUS_SHIPPED.equals(status)) {
                put("shipmentTime", operateTime);
            } else if (STATUS_RECEIVED.equals(status)) {
                put("receivedTime", operateTime);
                put("receiver", operator);
            }
        }};

        int updated = vmsBtOrderDetailDaoExt.updateOrderStatus(params);

        // 记录订单变更状态
        if (updated > 0) {
            params.put("shipmentId", shipmentId); // 搜索条件增加之前更新的shipmentId
            this.logOrderDetails(params);
        }

        return updated;
    }

    /**
     * 更新订单状态为Shipped(外部接口更新成Shipment用)
     *
     * @param channelId     channelId
     * @param reservationId 物品id
     * @param operateTime   操作时间
     * @param operator      操作着
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateReservationStatusToShipped(String channelId, String reservationId, Integer shipment_id, String modifier, Date operateTime, String operator) {

        Map<String, Object> changeStatusParams = new HashMap<String, Object>() {{
            // 更新条件
            put("channelId", channelId);
            put("reservationId", reservationId);
            // 更新内容
            put("status", STATUS_SHIPPED);
            put("modifier", modifier);
            put("shipmentId", shipment_id);
            put("shipmentTime", operateTime);
        }};

        int count = vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParams);

        // 记录订单变更状态
        if (count > 0) {
            this.logOrderDetails(changeStatusParams);

        }

        return count;
    }

    public int countOrderWithShipment(String channelId, Integer shipmentId) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("shipmentId", shipmentId);
        }};
        return vmsBtOrderDetailDaoExt.countOrder(params);
    }

    public int countSkuWithShipment(String channelId, Integer shipmentId) {
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("shipmentId", shipmentId);
        }};
        return vmsBtOrderDetailDaoExt.countSku(params);
    }

    public Date getLatestPrintedTime(String channelId, Integer shipmentId) {
        return vmsBtOrderDetailDaoExt.getLatestPrintedTIme(
                new HashMap<String, Object>() {{
                    put("channelId", channelId);
                    put("shipmentId", shipmentId);
                }}
        );
    }

    public int clearOrderCancelInfo(String channelId, String consolidationOrderId) {
        return vmsBtOrderDetailDaoExt.clearOrderCancelInfo(
                new HashMap<String, Object>() {{
                    put("channelId", channelId);
                    put("consolidationOrderId", consolidationOrderId);
                }}
        );
    }

    public int clearSkuCancelInfo(String channelId, String reservationId) {
        return vmsBtOrderDetailDaoExt.clearSkuCancelInfo(
                new HashMap<String, Object>() {{
                    put("channelId", channelId);
                    put("reservationId", reservationId);
                }}
        );
    }
}
