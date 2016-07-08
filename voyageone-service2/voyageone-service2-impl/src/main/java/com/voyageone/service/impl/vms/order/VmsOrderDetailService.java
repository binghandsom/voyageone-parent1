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

    public List<String> selectPlatformOrderIdList(Map<String, Object> orderSearchParams) {

        return vmsBtOrderDetailDaoExt.selectPlatformOrderListLimitedByTime(orderSearchParams);

    }

    public long getTotalOrderNum(Map<String, Object> orderSearchParamsWithLimitAndSort) {

        // TODO: 16-7-8 总数统计尚未完成
        return 10;
//        return vmsBtOrderDetailDaoExt.selectPlatformOrderListNumLimitedByTime(orderSearchParams);
    }
}
