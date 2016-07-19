package com.voyageone.service.impl.vms.order;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.vms.VmsBtOrderDetailDao;
import com.voyageone.service.dao.vms.VmsBtOrderLogDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import com.voyageone.service.model.vms.VmsBtOrderLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;
    private VmsBtOrderDetailDao vmsBtOrderDetailDao;
    private VmsBtOrderLogDao vmsBtOrderLogDao;

    @Autowired
    public OrderDetailService(VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt, VmsBtOrderDetailDao
            vmsBtOrderDetailDao, VmsBtOrderLogDao vmsBtOrderLogDao) {
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

        return vmsBtOrderDetailDaoExt.selectPlatformOrderListNumLimitedByTime(orderSearchParamsWithLimitAndSort);
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

        // 记录订单变更状态
        Map<String, Object> changeStatusParams = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("consolidationOrderId", consolidationOrderId);
            put("status", status);
            put("modifier", modifier);
        }};

        int count = vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParams);
        if (count > 0) {
            this.logOrderDetails(changeStatusParams);
        }

        return count;
    }

    /**
     * 更新订单状态
     *
     * @param channelId     channelId
     * @param reservationId 订单号
     * @param status        待更新状态
     * @return 更新涉及条数
     */
    @VOTransactional
    public int updateReservationStatus(String channelId, String reservationId, String status, String modifier) {

        // 记录订单变更状态
        Map<String, Object> changeStatusParams = new HashMap<String, Object>() {{
            put("channelId", channelId);
            put("reservationId", reservationId);
            put("status", status);
            put("modifier", modifier);
        }};

        int count = vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParams);
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

        return vmsBtOrderDetailDaoExt.selectSkuListNumLimitedByTime(skuSearchParamsWithLimitAndSort);
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
}
