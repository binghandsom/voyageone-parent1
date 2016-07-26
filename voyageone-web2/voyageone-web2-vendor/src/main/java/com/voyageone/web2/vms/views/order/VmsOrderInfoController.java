package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.order.DownloadInfo;
import com.voyageone.web2.vms.bean.order.OrderSearchInfo;
import com.voyageone.web2.vms.bean.order.PlatformSubOrderInfoBean;
import com.voyageone.web2.vms.bean.order.SubOrderInfoBean;
import com.voyageone.web2.vms.views.common.ChannelConfigService;
import com.voyageone.web2.vms.views.shipment.VmsShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsConstants.PICKING_LIST;
import static com.voyageone.web2.vms.VmsConstants.XLSX;
import static com.voyageone.web2.vms.VmsUrlConstants.ORDER;

/**
 * order info controller(/vms/order/order_info)
 * Created by vantis on 16-7-6.
 */
@RestController
@RequestMapping(value = ORDER.ORDER_INFO.ROOT, method = RequestMethod.POST)
public class VmsOrderInfoController extends BaseController {

    private VmsOrderInfoService vmsOrderInfoService;
    private VmsShipmentService shipmentService;
    private ChannelConfigService channelConfigService;

    @Autowired
    public VmsOrderInfoController(VmsOrderInfoService vmsOrderInfoService,
                                  VmsShipmentService shipmentService,
                                  ChannelConfigService channelConfigService) {
        this.vmsOrderInfoService = vmsOrderInfoService;
        this.shipmentService = shipmentService;
        this.channelConfigService = channelConfigService;
    }

    // 页面初始化部分
    @RequestMapping(ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("channelConfigs", channelConfigService.getChannelConfigs(this.getUser()));
        initialInfo.put("orderStatusList", vmsOrderInfoService.getAllOrderStatusesList());
        initialInfo.put("shipmentStatusList", shipmentService.getAllStatus());
        initialInfo.put("currentShipment", shipmentService.getCurrentShipment(this.getUser()));
        return success(initialInfo);
    }

    @RequestMapping(ORDER.ORDER_INFO.SEARCH)
    public AjaxResponse search(@RequestBody OrderSearchInfo orderSearchInfo) {
        Map<String, Object> orderInfo = new HashMap<>();
        Date date = new Date();
        orderInfo.put("orderInfo", vmsOrderInfoService.getOrderInfo(this.getUser(), orderSearchInfo));
        $debug("this action takes totally " + String.valueOf(new Date().getTime() - date.getTime()) + " milliseconds.");
        return success(orderInfo);
    }

    @RequestMapping(ORDER.ORDER_INFO.CANCEL_ORDER)
    public AjaxResponse cancelOrder(@RequestBody PlatformSubOrderInfoBean item) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsOrderInfoService.cancelOrder(this.getUser(), item));
        return success(result);
    }

    @RequestMapping(ORDER.ORDER_INFO.CANCEL_SKU)
    public AjaxResponse cancelSku(@RequestBody SubOrderInfoBean item) {
        Map<String, Object> result = new HashMap<>();
        // TODO: 16-7-11 对于取消订单前的状态检查尚未考虑完善 vantis
        result.put("success", vmsOrderInfoService.cancelSku(this.getUser(), item));

        return success(result);
    }

    @RequestMapping(ORDER.ORDER_INFO.DOWNLOAD_PICKING_LIST)
    public ResponseEntity<byte[]> downloadPickingList(@RequestParam Map<String, Object> downloadParams) throws
            IOException {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setOrderType(downloadParams.get("orderType").toString());
        return genResponseEntityFromBytes(PICKING_LIST + new Date().getTime() + XLSX,
                vmsOrderInfoService.getExcelBytes(this.getUser(), downloadInfo));
    }
}
