package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsUrlConstants;
import com.voyageone.web2.vms.bean.order.OrderSearchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * order info controller(/vms/order/order_info)
 * Created by vantis on 16-7-6.
 */
@RestController
@RequestMapping(value = VmsUrlConstants.ORDER.ORDER_INFO.ROOT, method = RequestMethod.POST)
public class OrderInfoController extends BaseController {

    private OrderInfoService orderInfoService;

    @Autowired
    public OrderInfoController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    // 页面初始化部分
    @RequestMapping(VmsUrlConstants.ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("channelConfigs", orderInfoService.getChannelConfigs(this.getUser()));
        initialInfo.put("searchOrderStatus", orderInfoService.getAllOrderStatusesList());
        initialInfo.put("currentShipment", orderInfoService.getCurrentShipment(this.getUser()));
        initialInfo.put("orderInfo", orderInfoService.getOrderInfo(this.getUser()));
        return success(initialInfo);
    }


    @RequestMapping(VmsUrlConstants.ORDER.ORDER_INFO.SEARCH)
    public AjaxResponse search(@RequestBody OrderSearchInfo orderSearchInfo) {
        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("orderInfo", orderInfoService.getOrderInfo(this.getUser(), orderSearchInfo));
        return success(orderInfo);
    }

}
