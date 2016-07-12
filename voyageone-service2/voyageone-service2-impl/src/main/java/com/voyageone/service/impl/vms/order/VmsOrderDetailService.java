package com.voyageone.service.impl.vms.order;

import com.voyageone.service.dao.vms.VmsBtOrderDetailDao;
import com.voyageone.service.daoext.vms.VmsBtOrderDetailDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * service about product's status
 * Created by vantis on 16-7-6.
 */
@Service
public class VmsOrderDetailService extends BaseService {

    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;
    private VmsBtOrderDetailDao vmsBtOrderDetailDao;

    @Autowired
    public VmsOrderDetailService (VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt, VmsBtOrderDetailDao vmsBtOrderDetailDao) {
        this.vmsBtOrderDetailDaoExt = vmsBtOrderDetailDaoExt;
        this.vmsBtOrderDetailDao = vmsBtOrderDetailDao;
    }

    /**
     * 查询订单
     * @param orderSearchParams 搜索条件
     * @return 订单详情
     */
    public List<VmsBtOrderDetailModel> selectOrderList(Map<String, Object> orderSearchParams) {

        // TODO: 16-7-7 条件暂未测试 vantis
        return vmsBtOrderDetailDaoExt.selectListLimitedByTime(orderSearchParams);
    }

    /**
     * 自条件搜索订单号
     * @param orderSearchParams 搜索条件
     * @return 订单号List
     */
    public List<String> selectPlatformOrderIdList(Map<String, Object> orderSearchParams) {

        return vmsBtOrderDetailDaoExt.selectList(orderSearchParams);

    }

    /**
     * 获取订单总量
     * @param orderSearchParamsWithLimitAndSort 搜索条件
     * @return 订单总量
     */
    public long getTotalOrderNum(Map<String, Object> orderSearchParamsWithLimitAndSort) {

        return vmsBtOrderDetailDaoExt.selectPlatformOrderListNumLimitedByTime(orderSearchParamsWithLimitAndSort);
    }

    /**
     * 更新订单状态
     * @param changeStatusParam 更新条件
     * @return 更新条数
     */
    public int updateOrderStatus(Map<String, Object> changeStatusParam) {
        return vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParam);
    }

    /**
     * 条件查询sku总数
     * @param skuSearchParamsWithLimitAndSort 搜索条件
     * @return sku总数
     */
    public long getTotalSkuNum(Map<String, Object> skuSearchParamsWithLimitAndSort) {

        return vmsBtOrderDetailDaoExt.selectSkuListNumLimitedByTime(skuSearchParamsWithLimitAndSort);
    }
}
