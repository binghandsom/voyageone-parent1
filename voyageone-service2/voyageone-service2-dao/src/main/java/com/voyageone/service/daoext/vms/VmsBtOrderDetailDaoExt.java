package com.voyageone.service.daoext.vms;

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

    List<VmsBtOrderDetailModel> orderDetailselectList(Map<String, Object> orderSearchParams);

    List<String> orderIdselectList(Map<String, Object> orderSearchParams);

    long selectPlatformOrderListNumLimitedByTime(Map<String, Object> orderSearchParamsWithLimitAndSort);

    long selectSkuListNumLimitedByTime(Map<String, Object> skuSearchParamsWithLimitAndSort);

    int updateOrderStatus(Map<String, Object> changeStatusParam);

    List<Map<String, Object>> selectListByShipmentTime(Map<String, Object> orderSearchParams);

    int updateSkuShipmentStatus(Map<String, Object> sortedParams);
}
