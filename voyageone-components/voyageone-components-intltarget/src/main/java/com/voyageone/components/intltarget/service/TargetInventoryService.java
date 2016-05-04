package com.voyageone.components.intltarget.service;

import com.voyageone.components.intltarget.TargetBase;
import com.voyageone.components.intltarget.bean.guest.TargetGuestShippingAddress;
import com.voyageone.components.intltarget.bean.inventory.TargetInventory;
import com.voyageone.components.intltarget.bean.inventory.TargetInventoryRequest;
import org.springframework.stereotype.Service;

/**
 * @author aooer 2016/4/19.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class TargetInventoryService extends TargetBase {

    private static final String Url="/available_to_promise";

    public TargetInventory getTargetInventory(TargetInventoryRequest request) throws Exception {
        return getApiResponseWithKey(Url+"/v2?request_type=availability",request,TargetInventory.class,true);
    }

}
