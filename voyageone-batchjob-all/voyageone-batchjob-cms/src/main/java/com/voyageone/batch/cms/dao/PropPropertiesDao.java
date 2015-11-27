package com.voyageone.batch.cms.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.cms.model.PropPropertiesModel;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PropPropertiesDao extends BaseDao {

    /**
     * 插入店铺的授权类目信息
     * @return List<AllotInventoryDetailBean>
     */
    public  void insertProps( List<PropPropertiesModel> props) {
        Map<String, Object> params = new HashMap<>();

        params.put("props", props);
        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_insertProps", params);

    }

    /**
     * 按照店铺来删除类目数据
     * @return List<AllotInventoryDetailBean>
     */
    public  void delPropsByShop( ShopBean shop) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", shop.getOrder_channel_id());
        params.put("cart_id", shop.getCart_id());

        updateTemplate.update(Constants.DAO_NAME_SPACE_CMS + "ims_delCatsByShop", params);

    }

    /**
     * 根据分类和品牌查询属性
     * @param category_id 分类ID
     * @param brand_id 品牌ID
     */
    public List<PropPropertiesModel> selectPropsByCategoryAndBrand(long category_id, long brand_id, boolean is_product)
    {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("category_id", category_id);
        dataMap.put("brand_id", brand_id);
        if (is_product)
            dataMap.put("is_product", 1);
        else
            dataMap.put("is_product", 0);

        return selectList(Constants.DAO_NAME_SPACE_CMS + "ims_select_props_by_category_and_brand", dataMap);
    }

}
