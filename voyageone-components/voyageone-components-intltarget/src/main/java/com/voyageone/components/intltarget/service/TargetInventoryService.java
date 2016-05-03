package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryRequest;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryResponse;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetInventoryService extends TargetBase {

    private static final String Url = "/available_to_promise";

    public TargetInventoryResponse getTargetInventory(TargetInventoryRequest request) throws Exception {
        return getApiResponseWithKey(Url + "/v2/" + request.getProduct_id() + "?multichannel_option=" + request.getMultichannel_option() + "&inventory_type=" + request.getInventory_type() + "&field_groups=" + request.getField_groups() + "", request, TargetInventoryResponse.class, false);
    }

}
