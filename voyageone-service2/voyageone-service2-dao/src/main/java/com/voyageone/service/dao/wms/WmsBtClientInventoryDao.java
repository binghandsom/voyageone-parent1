package com.voyageone.service.dao.wms;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeff 16/09/08
 * @version 2.0.0
 */

@Repository
public class WmsBtClientInventoryDao extends com.voyageone.service.dao.ServiceBaseDao {

    public int insertClientInventory(Map<String, Object> param) {
        return insert("wms_bt_client_inventory_insert", param);
    }

    public int updateClientInventorySynFlag(String channelId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        return update("wms_bt_client_inventory_update_synflg_by_client_sku", param);
    }
}
