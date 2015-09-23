package com.voyageone.common.configs.dao;

import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Jack on 4/17/2015.
 */
@Repository
public class ShopDao extends BaseDao {

    /**
     * 获取所有店铺数据
     */
    public List<ShopBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "viw_com_cart_shop_channel_mapping_getAll");
    }

    /**
     * 获取所有Cart数据
     */
    public List<CartBean> getAllCart() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_getAll");
    }
}
