package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * order info controller(/vms/order/order_info)
 * Created by vantis on 16-7-6.
 */
@RestController
@RequestMapping(value = VmsUrlConstants.ORDER.ORDER_INFO.ROOT, method = RequestMethod.POST)
public class OrderInfoController extends BaseController {

    @Autowired
    OrderInfoService orderInfoService;

    // 页面初始化部分
    @RequestMapping(VmsUrlConstants.ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("channelConfigs", orderInfoService.getChannelConfigs(this.getUser()));
        initialInfo.put("searchOrderStatus", orderInfoService.getAllOrderStatusesList());
        initialInfo.put("currentShipment", orderInfoService.getCurrentShipment(this.getUser()));
        initialInfo.put("orderInfoList", orderInfoService.getOrders(this.getUser()));
        return success(initialInfo);
    }

}
