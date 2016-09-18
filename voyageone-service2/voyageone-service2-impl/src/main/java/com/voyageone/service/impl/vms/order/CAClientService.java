package com.voyageone.service.impl.vms.order;

import com.voyageone.service.bean.vms.channeladvisor.order.VmsBtClientOrderDetailsGroupModel;
import com.voyageone.service.daoext.vms.VmsBtClientOrdersDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.vms.VmsBtClientOrderDetailsModel;
import com.voyageone.service.model.vms.VmsBtClientOrdersModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author aooer 2016/9/12.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CAClientService extends BaseService {

    @Autowired
    private VmsBtClientOrdersDaoExt vmsBtCAClientDao;

    public List<VmsBtClientOrdersModel> getClientOrderList(String channelId, String status, String limit) {
        return vmsBtCAClientDao.selectClientOrderList(channelId, status, StringUtils.isEmpty(limit) ? 0 : Integer.parseInt(limit));
    }

    public VmsBtClientOrdersModel getClientOrderById(String channelId, String orderID) {
        return vmsBtCAClientDao.selectClientOrderById(orderID, channelId);
    }

    public List<VmsBtClientOrderDetailsModel> getClientOrderDetailById(String channelId, String orderID, String status) {
        return vmsBtCAClientDao.selectClientOrderDetailById(orderID, channelId, status);
    }

    public int updateItemsSkuList(List<VmsBtClientOrderDetailsModel> vmsBtClientOrderDetailsModelList) {
        int count=0;
        for (VmsBtClientOrderDetailsModel vmsBtClientOrderDetailsModel : vmsBtClientOrderDetailsModelList) {
            count+=vmsBtCAClientDao.updateItemsSkuList(vmsBtClientOrderDetailsModel);
        }
        return count;
    }

    public List<VmsBtClientOrderDetailsGroupModel> getClientOrderDetailList(String channelId, List<String> orderIds) {
        return vmsBtCAClientDao.selectClientOrderDetailList(channelId, orderIds);
    }

    public int updateClientOrderStatus(String channelId, String orderId, String status) {
        return vmsBtCAClientDao.updateClientOrderStatus(orderId, channelId, status);
    }

}
