package com.voyageone.web2.vms.views.home;


import com.voyageone.service.model.vms.VmsBtDataAmountModel;
import com.voyageone.web2.base.BaseViewService;
import com.voyageone.web2.vms.VmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.voyageone.service.impl.vms.amount.AmountService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VmsHomeInfoService
 * Created on 2016/5/5.
 * @author jeff.duan
 * @version 1.0
 */
@Service
public class VmsHomeInfoService extends BaseViewService {

    @Autowired
    private AmountService DataAmountService;

    /**
     * 初始化（取得Home页面显示的数据）
     *
     * @param channelId 渠道
     */
    public Map<String, Object> init(String channelId) {
        Map<String, Object> result = new HashMap<>();
        result.put("countOrder", "0");
        result.put("countSku", "0");
        result.put("countReceiveErrorShipment", "0");

        List<VmsBtDataAmountModel> models =  DataAmountService.getDataAmountInfo(channelId);

        for (VmsBtDataAmountModel model : models) {
            if (VmsConstants.DataAmount.NEW_ORDER_COUNT.equals(model.getAmountName())) {
                result.put("countOrder", model.getAmountVal());
            } else if (VmsConstants.DataAmount.NEW_SKU_COUNT.equals(model.getAmountName())) {
                result.put("countSku", model.getAmountVal());
            } else if (VmsConstants.DataAmount.RECEIVE_ERROR_SHIPMENT_COUNT.equals(model.getAmountName())) {
                result.put("countReceiveErrorShipment", model.getAmountVal());
            }
        }

        return result;
    }
}