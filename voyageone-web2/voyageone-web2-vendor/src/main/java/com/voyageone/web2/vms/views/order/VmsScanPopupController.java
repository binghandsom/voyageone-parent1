package com.voyageone.web2.vms.views.order;

import com.voyageone.web2.base.BaseController;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.vms.bean.order.ScanInfo;
import com.voyageone.web2.vms.bean.order.ScanPopupInitialInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.voyageone.web2.vms.VmsUrlConstants.*;

/**
 * 订单级别扫码弹出窗
 * Created by vantis on 16-7-19.
 */
@RestController
@RequestMapping(POPUP.SCAN.ROOT)
public class VmsScanPopupController extends BaseController {

    private VmsOrderInfoService vmsOrderInfoService;

    @Autowired
    public VmsScanPopupController(VmsOrderInfoService vmsOrderInfoService) {
        this.vmsOrderInfoService = vmsOrderInfoService;
    }

    @RequestMapping(POPUP.SCAN.INIT)
    public AjaxResponse init(@RequestBody ScanPopupInitialInfo scanPopupInitialInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("scannedSkuList", vmsOrderInfoService.getScannedSkuList(this.getUser(),
                scanPopupInitialInfo.getShipment(), scanPopupInitialInfo.getConsolidationOrderId()));

        return success(result);
    }

    @RequestMapping(POPUP.SCAN.SCAN_BARCODE)
    public AjaxResponse checkBarcode(@RequestBody ScanInfo scanInfo) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", vmsOrderInfoService.scanBarcodeInOrder(this.getUser(), scanInfo));
        result.put("scannedSkuList", vmsOrderInfoService.getScannedSkuList(this.getUser(),
                scanInfo.getShipment(), scanInfo.getConsolidationOrderId()));
        result.put("finished", vmsOrderInfoService.orderScanFinished(this.getUser(), scanInfo));

        return success(result);
    }
}
