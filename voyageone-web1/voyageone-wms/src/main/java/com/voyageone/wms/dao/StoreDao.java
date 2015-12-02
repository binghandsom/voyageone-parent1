package com.voyageone.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.modelbean.ChannelStoreBean;
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
public class StoreDao extends BaseDao {
    /**
     * 获取仅包含 id 和 name 的 StoreBean 数据
     *
     * @return ChannelStoreBean 的 List
     */
    public List<ChannelStoreBean> getSimpleAll() {
        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_mt_stores_get_simple_all");
    }

    /**
     * 获取 store 的 channel id
     * @param store_id int
     * @return String
     */
    public String getChannel_id(int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("store_id", store_id);

        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_mt_stores_get_channel_id", params);
    }

    public String getStoreName(int store_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("store_id", store_id);

        return selectOne(Constants.DAO_NAME_SPACE_WMS + "wms_mt_stores_get_store_name", params);
    }
}
