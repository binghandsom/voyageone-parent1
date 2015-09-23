package com.voyageone.batch.ims.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.batch.ims.modelbean.PlatformCategories;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.components.tmall.bean.ItemSchema;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlatformCategoryDao extends BaseDao {

    /**
     * 插入店铺的授权类目信息（第三方平台）
     * @return List<AllotInventoryDetailBean>
     */
    public  void insertPlatformCats( List<PlatformCategories> cats,ShopBean shop, String jobName) {
        Map<String, Object> params = new HashMap<>();

        params.put("cats", cats);
        params.put("order_channel_id", shop.getOrder_channel_id());
        params.put("cart_id", shop.getCart_id());
        params.put("task_name", jobName);

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_insertPlatformCats", params);

    }

    /**
     * 按照店铺来删除类目数据（第三方平台）
     * @return List<AllotInventoryDetailBean>
     */
    public  void delPlatformCatsByShop( ShopBean shop) {
        Map<String, Object> params = new HashMap<>();

        params.put("order_channel_id", shop.getOrder_channel_id());
        params.put("cart_id", shop.getCart_id());

        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_delPlatformCatsByShop", params);

    }

    /***
     * 根据第三方平台类目id，删除指定的类目
     * @param delCats
     */
    public  void delPlatformCatsByCid( List<ItemSchema> delCats) {
        // 如果没有指定类目的场合，那就不用做下去了
        if (delCats == null || delCats.size() == 0) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("cats", delCats);
        updateTemplate.update(Constants.DAO_NAME_SPACE_IMS + "ims_delPlatformCatsByCid", params);

    }


    /**
     * 获取指定渠道（天猫，天猫国际，京东）所有（第三方平台）店铺叶子类目
     * @param cart_id 渠道
     * @return List<PlatformCategories>
     */
    public  List<PlatformCategories> getPlatformSubCatsWithoutShop( String cart_id ) {

        Map<String, Object> params = new HashMap<>();
        params.put("cart_id", Integer.parseInt(cart_id));

        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformSubCatsWithoutShop", params);
    }

    /**
     * 获取所有（第三方平台）店铺类目
     * @param cart_id 渠道
     * @return List<PlatformCategories>
     */
    public  List<PlatformCategories> getPlatformCatsWithoutShop( String cart_id ) {

        Map<String, Object> params = new HashMap<>();
        params.put("cart_id", Integer.parseInt(cart_id));

        return selectList(Constants.DAO_NAME_SPACE_IMS + "ims_selectPlatformCatsWithoutShop", params);
    }

}
