package com.voyageone.wms.dao.external;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 外部内容：对应 cms_mt_size
 * Created by Tester on 5/28/2015.
 *
 * @author Jonas
 */
@Repository
public class WmsSizeDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }

    /**
     * 获取指定商品的去重复尺码。返回只有尺码名称的集合
     */
    public List<String> getAllSize(String order_channel_id, int product_type_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);
        params.put("product_type_id", product_type_id);

        return selectList("cms_mt_size_getAllSize", params);
    }
}
