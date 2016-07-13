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
public class VmsOrderDetailService extends BaseService {

    private VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;
    private VmsBtOrderDetailDao vmsBtOrderDetailDao;
    private VmsBtOrderLogDao vmsBtOrderLogDao;

    @Autowired
    public VmsOrderDetailService(VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt, VmsBtOrderDetailDao
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

        return vmsBtOrderDetailDaoExt.selectListLimitedByTime(orderSearchParams);
    }

    /**
     * 自条件搜索订单号
     *
     * @param orderSearchParams 搜索条件
     * @return 订单号List
     */
    public List<String> selectPlatformOrderIdList(Map<String, Object> orderSearchParams) {

        return vmsBtOrderDetailDaoExt.selectList(orderSearchParams);

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
     * 更新订单状态
     *
     * @param changeStatusParam 更新条件
     * @return 更新条数
     */
    @VOTransactional
    public int updateOrderStatus(Map<String, Object> changeStatusParam) {

        Map<String, Object> logParams = new HashMap<String, Object>() {{
            putAll(changeStatusParam);
        }};
        logParams.remove("status");
        this.select(logParams).stream()
                .map(vmsBtOrderDetailModel -> new VmsBtOrderLogModel() {{
                    setChannelId(vmsBtOrderDetailModel.getChannelId());
                    setStatus(vmsBtOrderDetailModel.getStatus());
                    setReservationId(vmsBtOrderDetailModel.getReservationId());
                    setCreater(changeStatusParam.get("modifier").toString());
                }})
                .forEach(vmsBtOrderLogModel -> vmsBtOrderLogDao.insert(vmsBtOrderLogModel));
        return vmsBtOrderDetailDaoExt.updateOrderStatus(changeStatusParam);
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
}
