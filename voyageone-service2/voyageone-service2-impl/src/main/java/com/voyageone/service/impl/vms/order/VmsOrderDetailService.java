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

    @Autowired
    VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;

    @Autowired
    VmsBtOrderDetailDao vmsBtOrderDetailDao;

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


    public int updateOrderStatus(Map<String, Object> changeStatusParam) {
        return vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParam);
    }
}
