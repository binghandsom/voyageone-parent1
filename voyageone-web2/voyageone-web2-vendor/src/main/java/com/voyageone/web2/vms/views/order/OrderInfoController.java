package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.VmsUrlConstants;
import com.voyageone.web2.vms.bean.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.*;
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
    @RequestMapping(ORDER.ORDER_INFO.INIT)
    public AjaxResponse init() {
        Map<String, Object> initialInfo = new HashMap<>();
        initialInfo.put("channelConfigs", orderInfoService.getChannelConfigs(this.getUser()));
        initialInfo.put("searchOrderStatus", orderInfoService.getAllOrderStatusesList());
        initialInfo.put("currentShipment", orderInfoService.getCurrentShipment(this.getUser()));
        return success(initialInfo);
    }

    @RequestMapping(ORDER.ORDER_INFO.SEARCH)
    public AjaxResponse search(@RequestBody OrderSearchInfo orderSearchInfo) {
        Map<String, Object> orderInfo = new HashMap<>();
        orderInfo.put("orderInfo", orderInfoService.getOrderInfo(this.getUser(), orderSearchInfo));
        return success(orderInfo);
    }

    @RequestMapping(ORDER.ORDER_INFO.CANCEL_ORDER)
    public AjaxResponse cancelOrder(@RequestBody PlatformSubOrderInfoBean item) {
        Map<String, Object> result = new HashMap<>();

        result.put("success", orderInfoService.cancelOrder(this.getUser(), item));

        return success(result);
    }

    @RequestMapping(ORDER.ORDER_INFO.CANCEL_SKU)
    public AjaxResponse cancelSku(@RequestBody SubOrderInfoBean item) {
        Map<String, Object> result = new HashMap<>();
        // TODO: 16-7-11 对于取消订单前的状态检查尚未考虑完善 vantis
        result.put("success", orderInfoService.cancelSku(this.getUser(), item));

        return success(result);
    }

    /**
     * 下载拣货单
     * @param downloadInfo 搜索条件
     * @return 表格
     */
    @RequestMapping(ORDER.ORDER_INFO.DOWNLOAD_PICKING_LIST)
    public ResponseEntity<byte[]> downloadPickingList(@RequestBody DownloadInfo downloadInfo) throws IOException {

        return genResponseEntityFromBytes("test.xlsx", orderInfoService.getExcelBytes(this.getUser(), downloadInfo));
    }
}
