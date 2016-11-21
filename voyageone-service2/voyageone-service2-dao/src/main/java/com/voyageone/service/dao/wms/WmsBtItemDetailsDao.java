package com.voyageone.service.dao.wms;

import com.voyageone.common.Constants;
import com.voyageone.service.bean.wms.ItemDetailsBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeff 16/09/08
 * @version 2.0.0
 */

@Repository
public class WmsBtItemDetailsDao extends com.voyageone.service.dao.ServiceBaseDao {
    public List<ItemDetailsBean> selectByClientSku(String channelId, String sku) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("clientSku", sku);

        return selectList("wms_bt_item_details_select_by_client_sku", params);
    }
    public int update(String channelId,String clientSku, int isSale) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("clientSku", clientSku);
        params.put("isSale", isSale);
        return update("wms_bt_item_details_updateIsSale", params);
    }

    public int updateCodeForMove(String channelId, String itemCodeOld, List<String> skuList, String itemCodeNew, String modifier) {
        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("itemCodeOld", itemCodeOld);
        params.put("skuList", skuList);
        params.put("itemCodeNew", itemCodeNew);
        params.put("modifier", modifier);
        return update("wms_bt_item_details_updateCodeForMove", params);
    }

    public List<ItemDetailsBean> selectByCode(String channelId,String code) {
        return selectList("wms_bt_item_details_selectSkusOfCode", parameters("channelId", channelId, "code", code));
    }
}
