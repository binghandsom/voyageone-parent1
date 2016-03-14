package com.voyageone.task2.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.task2.cms.bean.InventoryForCmsBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryDao extends BaseDao {

    /**
     * 获取code级别的库存
     * @param order_channel_id
     * @return
     */
    public List<InventoryForCmsBean> selectInventoryCode(String order_channel_id, String modified) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("modified", modified);
        params.put("creater", modified);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "cms_selectInventoryCode", params);
    }


}
