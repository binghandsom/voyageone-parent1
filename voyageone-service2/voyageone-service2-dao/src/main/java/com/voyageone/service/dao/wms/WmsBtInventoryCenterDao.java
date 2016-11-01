package com.voyageone.service.dao.wms;

import com.voyageone.service.model.wms.WmsBtInventoryCenterLogicModel;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeff.duan 16/10/20
 * @version 2.0.0
 */

@Repository
public class WmsBtInventoryCenterDao extends com.voyageone.service.dao.ServiceBaseDao {


    public int updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("itemCodeOld", itemCodeOld);
        params.put("skuList", skuList);
        params.put("itemCodeNew", itemCodeNew);
        params.put("modifier", modifier);
        return update("wms_bt_inventory_center_updateCodeForMove", params);
    }
}
