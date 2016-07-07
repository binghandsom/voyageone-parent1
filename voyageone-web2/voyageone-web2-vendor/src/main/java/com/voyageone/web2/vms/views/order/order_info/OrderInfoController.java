package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VendorUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping(value = VendorUrlConstants.ORDER.ORDER_INFO.ROOT, method = RequestMethod.POST)
public class OrderInfoController extends BaseController {

    @Autowired
    OrderInfoService orderInfoService;

    // 页面初始化部分
    @RequestMapping(VendorUrlConstants.ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("channel_configs", orderInfoService.getChannelConfigs(this.getUser()));
        initialInfo.put("search_order_status", orderInfoService.getAllOrderStatusesList());
        initialInfo.put("current_shipment", orderInfoService.getCurrentShipment(this.getUser()));
        return success(initialInfo);
    }
}
