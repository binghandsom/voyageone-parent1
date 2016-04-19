package com.voyageone.service.impl.cms;

import com.mysql.jdbc.StringUtils;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.redis.CacheHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/15 10:45
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Service
public class CartService {

    @Resource
    ShopDao cartDao;

    public List<CartBean> getAll(CartBean con) {
        List<CartBean> carts = cartDao.getAllCarts();
        Predicate<CartBean> filter=(bean)->{

            boolean flg = true;

            if (org.apache.commons.lang3.StringUtils.isNotBlank(con.getCart_id())) {
                flg = flg && con.getCart_id().equalsIgnoreCase(bean.getCart_id());
            }

            if (org.apache.commons.lang3.StringUtils.isNotBlank(con.getName())) {
                flg = flg && con.getName().equalsIgnoreCase(bean.getName());
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(con.getCart_type())) {
                flg = flg && con.getCart_type().equalsIgnoreCase(bean.getCart_type());
            }
            if (con.getActive()!=null) {
                flg = flg && con.getActive().equalsIgnoreCase(bean.getActive());
            }
            return flg;

        };
        return carts.stream().filter(filter)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public int saveOrUpdate(CartBean bean) {
        return cartDao.insertOrUpdate(bean);
    }

    /**
     * 逻辑删除,将active设为0
     * @return
     */
    public int deleteLogic(CartBean bean) {
        if ( bean ==null || StringUtils.isNullOrEmpty(bean.getCart_id())) {

            return 0;
        }
        int result = cartDao.deleteLogic(bean.getCart_id(), bean.getModifier());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());
        return result;
    }

    public CartBean getById(String cart_id) {
        CartBean con = new CartBean();
        con.setCart_id(cart_id);
        List<CartBean> results = getAll(con);
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    public void save(CartBean bean) {
        try {
            cartDao.insert(bean);
            CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());
        } catch (Exception e) {
            if(e.getMessage()!=null && e.getMessage().contains("Duplicate entry")){
                throw new BusinessException("CART_ID重复!");
            }
            throw new BusinessException("保存Cart失败!", e);
        }
    }
}
