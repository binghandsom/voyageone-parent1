package com.voyageone.batch.ims.dao;

import com.taobao.api.domain.Brand;
import com.taobao.api.domain.ItemCat;
import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BrandDao extends BaseDao {


    /**
     * 插入店铺的授权品牌信息
     * @param brands
     * @param shop
     * @param jobName
     */
    public  void insertBrands( List<Brand> brands,ShopBean shop, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("brands", brands);
        params.put("order_channel_id", shop.getOrder_channel_id());
        params.put("cart_id", shop.getCart_id());
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertBrands", params);

    }
    /**
     * 按照店铺来删除品牌数据
     * @return List<AllotInventoryDetailBean>
     */
    public  void delBrandsByShop( ShopBean shop) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", shop.getOrder_channel_id());
        params.put("cart_id", shop.getCart_id());

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_delBrandsByShop", params);

    }
}
