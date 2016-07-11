package com.voyageone.service.daoext.vms;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.service.model.vms.VmsBtOrderDetailModel;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 订单相关CRUD扩展
 * Created by vantis on 16-7-7.
 */

@Repository
public interface VmsBtOrderDetailDaoExt {

    List<VmsBtOrderDetailModel> selectListLimitedByTime(Map<String, Object> orderSearchParams);

    List<String> selectList(Map<String, Object> orderSearchParams);

    long selectPlatformOrderListNumLimitedByTime(Map<String, Object> orderSearchParamsWithLimitAndSort);

    int updateOrderStatus(Map<String, Object> changeStatusParam);
}
