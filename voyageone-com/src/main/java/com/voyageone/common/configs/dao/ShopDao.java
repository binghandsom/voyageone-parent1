package com.voyageone.common.configs.dao;

import com.google.common.collect.ImmutableMap;
import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.beans.ShopBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
     * 获取未删除的cart
     */
    public List<CartBean> getActiveCarts() {
        List<CartBean> carts = getAllCarts();
        List<CartBean> result = carts.stream().filter(bean -> {
            return "1".equals(bean.getActive());
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取所有cart
     * @return
     */
    public List<CartBean> getAllCarts() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_getAll");
    }

    /**
     * 获取部分cart
     * @return
     */
    public List<CartBean> getCarts(CartBean cartBean) {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_getList", cartBean);
    }

    /**
     * 获取cart
     * @return
     */
    public CartBean getCart(String cartId) {
        CartBean cartBean = new CartBean();
        cartBean.setCart_id(cartId);
        return selectOne(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_getList", cartBean);
    }


    public int insertOrUpdate(CartBean bean) {
        return insert(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_insertOrUpdate", bean);
    }

    public int update(CartBean bean) {
        return update(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_Update", bean);
    }

    public int deleteLogic(String cart_id, String modifier) { //参数均不能为空!
        return update(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_delete",
                ImmutableMap.of("cart_id", cart_id, "modifier", modifier) );
    }

    public void insert(CartBean bean) {
        insert(Constants.DAO_NAME_SPACE_COMMON + "ct_cart_insert", bean);
    }
}
