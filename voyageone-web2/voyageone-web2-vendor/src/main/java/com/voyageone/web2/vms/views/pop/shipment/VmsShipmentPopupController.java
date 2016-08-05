package com.voyageone.web2.vms.views.pop.shipment;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.service.bean.vms.shipment.ShipmentBean;
import com.voyageone.web2.vms.views.order.VmsOrderInfoService;
import com.voyageone.web2.vms.views.shipment.VmsShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.POPUP;

/**
 * order页面popup的shipment
 * Created by vantis on 16-7-14.
 */
@RestController
@RequestMapping(value = POPUP.SHIPMENT.ROOT, method = RequestMethod.POST)
public class VmsShipmentPopupController extends BaseController {

    private VmsShipmentService vmsShipmentService;
    private VmsOrderInfoService vmsOrderInfoService;

    @Autowired
    public VmsShipmentPopupController(VmsShipmentService vmsShipmentService, VmsOrderInfoService vmsOrderInfoService) {
        this.vmsShipmentService = vmsShipmentService;
        this.vmsOrderInfoService = vmsOrderInfoService;
    }

    @RequestMapping(POPUP.SHIPMENT.INIT)
    public AjaxResponse init(@RequestBody Integer shipmentId) {
        Map<String, Object> result = new HashMap<>();
        result.put("shipmentStatusList", vmsShipmentService.getAllStatus());
        result.put("expressCompanies", vmsShipmentService.getAllExpressCompanies());
        result.put("orderCount", vmsOrderInfoService.countScannedOrder(this.getUser(), shipmentId));
        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.GET)
    public AjaxResponse get() {
        Map<String, Object> result = new HashMap<>();
        result.put("currentShipment", vmsShipmentService.getCurrentShipment(this.getUser()));
        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.GET_INFO)
    public AjaxResponse getInfo(@RequestBody Integer shipmentId) {
        Map<String, Object> result = new HashMap<>();
        result.put("shipment", vmsShipmentService.getShipment(this.getUser(), shipmentId));
        return success(result);
    }

    /**
     * 提交shipment修改
     *
     * @param shipmentBean 待修改shipment
     * @return 修改结果
     */
    @RequestMapping(POPUP.SHIPMENT.SUBMIT)
    public AjaxResponse submit(@RequestBody ShipmentBean shipmentBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsShipmentService.submit(this.getUser(), shipmentBean));
        result.put("currentShipment", vmsShipmentService.getCurrentShipment(this.getUser()));
        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.CREATE)
    public AjaxResponse create(@RequestBody ShipmentBean shipmentBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsShipmentService.create(this.getUser(), shipmentBean));
        result.put("currentShipment", vmsShipmentService.getCurrentShipment(this.getUser()));
        return success(result);
    }

    /**
     * 确认当前shipment是否可以直接关闭
     *
     * @param shipmentBean 当前shipment
     * @return 未完整扫描的订单号
     */
    @RequestMapping(POPUP.SHIPMENT.CONFIRM)
    public AjaxResponse confirm(@RequestBody ShipmentBean shipmentBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("invalidOrderList", vmsOrderInfoService.confirmShipment(this.getUser(), shipmentBean));
        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.END)
    public AjaxResponse end(@RequestBody ShipmentBean shipmentBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("result", vmsShipmentService.endShipment(this.getUser(), shipmentBean));
        result.put("currentShipment", vmsShipmentService.getCurrentShipment(this.getUser()));
        return success(result);
    }
}
