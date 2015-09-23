package com.voyageone.wms.dao.external;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.core.CoreConstants;
import com.voyageone.wms.modelbean.external.WmsProductBean;
import com.voyageone.wms.modelbean.external.WmsProductTypeBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对应 cms_bt_product 表。
 * <i>外部系统的内容。</i>
 * <br />Created by Tester on 5/26/2015.
 *
 * @author Jonas
 */
@Repository
public class WmsProductDao extends BaseDao {
    @Override
    protected String namespace() {
        return "com.voyageone.wms.sql";
    }

    /**
     * 根据 code 和 order_channel_id 查询商品
     *
     * @param code             商品 code
     * @param order_channel_id 渠道 id
     * @return WmsProductBean
     */
    public WmsProductBean getProductByCode(String code, String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("code", code);
        params.put("order_channel_id", order_channel_id);

        return selectOne("cms_bt_product_getProductByCode", params);
    }

    /**
     * 检查这个 type 数据库中是否存在
     *
     * @param product_type_id  int
     * @param order_channel_id 渠道
     * @return boolean
     */
    public boolean hasProductType(int product_type_id, String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("product_type_id", product_type_id);
        params.put("order_channel_id", order_channel_id);

        int count = selectOne("cms_mt_product_type_hasProductType", params);

        return count > 0;
    }

    public int insertProduct(WmsProductBean productBean) {
        return updateTemplate.insert("cms_bt_product_insertProduct", productBean);
    }

    public int updateProduct(WmsProductBean productBean) {
        return updateTemplate.update("cms_bt_product_updateProduct", productBean);
    }

    public List<WmsProductTypeBean> getAllProductTypes(String order_channel_id) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", order_channel_id);

        return selectList("cms_mt_product_type_getAllProductTypes", params);
    }
}
