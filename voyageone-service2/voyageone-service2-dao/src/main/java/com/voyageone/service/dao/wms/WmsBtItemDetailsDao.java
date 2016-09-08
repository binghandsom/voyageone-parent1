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
}
