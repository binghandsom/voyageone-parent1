package com.voyageone.service.daoext.vms;

import com.voyageone.service.bean.vms.channeladvisor.order.VmsBtClientOrderDetailsGroupModel;
import com.voyageone.service.model.vms.VmsBtClientOrderDetailsModel;
import com.voyageone.service.model.vms.VmsBtClientOrdersModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author aooer 2016/9/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@Repository
public interface VmsBtClientOrdersDaoExt {

    //根据id查询clientOrder
    VmsBtClientOrdersModel selectClientOrderById(String orderId, String orderChannelId);

    //根据id查询clientOrderDetail
    List<VmsBtClientOrderDetailsModel> selectClientOrderDetailById(String orderId, String orderChannelId, String status);

    //根据状态，查询clientOrders
    List<VmsBtClientOrdersModel> selectClientOrderList(String orderChannelId, String status, int limit);

    //根据ids，查询ClientOrderDetails
    List<VmsBtClientOrderDetailsGroupModel> selectClientOrderDetailList(String orderChannelId, List<String> orderIds);

    //根据id，修改clientOrder状态
    int updateClientOrderStatus(String orderId, String orderChannelId, String status);

    //批量更新items对应的明细
    int updateItemsSkuList(List<VmsBtClientOrderDetailsModel> vmsBtClientOrderDetailsModelList);

}
