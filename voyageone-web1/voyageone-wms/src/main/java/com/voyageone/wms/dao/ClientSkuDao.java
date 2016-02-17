package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.ChannelStoreBean;
import com.voyageone.wms.modelbean.ClientSkuBean;
import com.voyageone.wms.modelbean.TransferItemBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tester on 5/3/2015.
 *
 * @author Jonas
 */
@Repository
public class ClientSkuDao extends BaseDao {

    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }
    /**
     * 判定ClientSku是否存在 根据 barcode
     * @param order_channel_id
     * @param barcode
     * @return int
     */
    public int getRecCount(String order_channel_id, String barcode) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("barcode", barcode);

        return (int)selectOne("wms_bt_client_sku_get_rec_count", params);
    }

    /**
     * 判定ClientSku是否存在 根据 item_code，size
     * @param order_channel_id
     * @param item_code
     * @param color
     * @param size
     * @return int
     */
    public int getRecCountByItemCodeAndSize(String order_channel_id, String item_code, String color, String size) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("item_code", item_code);
        params.put("color", color);
        params.put("size", size);

        return (int)selectOne("wms_bt_client_sku_get_rec_countByItemCodeAndSize", params);
    }

    public int insertItem(ClientSkuBean item) {
        return updateTemplate.insert("wms_bt_client_sku_insert", item);
    }

    /**
     * 取得相关的SKU信息
     * @param order_channel_id
     * @param barcode
     * @return ClientSkuBean
     */
    public ClientSkuBean getSkuInfo(String order_channel_id, String barcode) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("barcode", barcode);

        return selectOne("wms_bt_client_sku_getSkuInfo", params);
    }
}
