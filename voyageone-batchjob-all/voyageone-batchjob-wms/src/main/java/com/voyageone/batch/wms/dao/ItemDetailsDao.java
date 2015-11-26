package com.voyageone.batch.wms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.wms.modelbean.ItemDetailsBean;
import com.voyageone.common.Constants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDetailsDao extends BaseDao {

    /**
     * 取得该渠道下的所有SKU
     * @param OrderChannelID 订单渠道
     * @return List<ItemDetailsBean>
     */
    public List<ItemDetailsBean> getItemDetaiInfo(String OrderChannelID) {

        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", OrderChannelID);

        return selectList(Constants.DAO_NAME_SPACE_WMS + "wms_getItemDetaiInfo", params);
    }

}
