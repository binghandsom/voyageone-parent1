package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.shipment.ShipmentBean;
import com.voyageone.web2.vms.views.shipment.VmsShipmentService;
import org.apache.commons.collections.map.HashedMap;
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

    private VmsShipmentService shipmentService;
    private VmsOrderInfoService vmsOrderInfoService;

    @Autowired
    public VmsShipmentPopupController(VmsShipmentService shipmentService, VmsOrderInfoService vmsOrderInfoService) {
        this.shipmentService = shipmentService;
        this.vmsOrderInfoService = vmsOrderInfoService;
    }

    @RequestMapping(POPUP.SHIPMENT.INIT)
    public AjaxResponse init() {
        Map<String, Object> result = new HashMap<>();
        result.put("searchStatuses", shipmentService.getAllStatus());
        result.put("expressCompanies", shipmentService.getAllExpressCompanies());

        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.GET)
    public AjaxResponse get() {
        Map<String, Object> result = new HashMap<>();
        result.put("currentShipment", shipmentService.getCurrentShipment(this.getUser()));
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
        result.put("success", shipmentService.submit(this.getUser(), shipmentBean));
        result.put("currentShipment", shipmentService.getCurrentShipment(this.getUser()));
        return success(result);
    }

    @RequestMapping(POPUP.SHIPMENT.CREATE)
    public AjaxResponse create(@RequestBody ShipmentBean shipmentBean) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", shipmentService.create(this.getUser(), shipmentBean));
        result.put("currentShipment", shipmentService.getCurrentShipment(this.getUser()));
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
        result.put("success", vmsOrderInfoService.endShipment(this.getUser(), shipmentBean));
        return success(result);
    }
}
