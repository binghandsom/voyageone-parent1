package com.voyageone.service.impl.cms;

import com.mysql.jdbc.StringUtils;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.CartDao;
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
    CartDao cartDao;

    public List<CartBean> getListBy(CartBean con) {
        List<CartBean> carts = cartDao.getAll();
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
            if (con.getStatus()!=null) {
                flg = flg && con.getStatus()==bean.getStatus();
            }
            return flg;

        };
        return carts.stream().filter(filter)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public int saveOrUpdate(CartBean bean) {
        return cartDao.saveOrUpdate(bean);
    }

    /**
     * 逻辑删除,将status设为0
     * @return
     */
    public int deleteLogic(CartBean bean) {
        if ( bean ==null || StringUtils.isNullOrEmpty(bean.getCart_id())) {

            return 0;
        }
        return cartDao.deleteLogic(bean.getCart_id(),bean.getModifier());
    }

    public CartBean getById(String cart_id) {
        CartBean con = new CartBean();
        con.setCart_id(cart_id);
        List<CartBean> results = getListBy(con);
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    public void save(CartBean bean) {
        try {
            cartDao.insert(bean);
        } catch (Exception e) {
            if(e.getMessage()!=null && e.getMessage().contains("Duplicate entry")){
                throw new BusinessException("CART_ID重复!");
            }
            throw new BusinessException("保存Cart失败!", e);
        }
    }
}
