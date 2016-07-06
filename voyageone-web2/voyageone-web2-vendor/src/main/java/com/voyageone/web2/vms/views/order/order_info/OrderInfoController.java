package com.voyageone.web2.vms.views.order.order_info;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VendorUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * order info controller(/order/order_info)
 * Created by vantis on 16-7-6.
 */
@Controller
@RequestMapping(value = VendorUrlConstants.ORDER.ORDER_INFO.ROOT, method = POST)
public class OrderInfoController extends BaseController {

    @Autowired
    OrderInfoService orderInfoService;

    // 页面初始化部分
    @RequestMapping(VendorUrlConstants.ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("search_sku_status", orderInfoService.getAllSkuStatusesList());
        initialInfo.put("current_shipment", orderInfoService.getCurrentShipment(this.getUser()));
        return success(initialInfo);
    }
}
