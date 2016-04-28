package com.voyageone.service.impl.cms;

import com.mysql.jdbc.StringUtils;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.service.dao.cms.CmsMtChangeHistoryDao;
import com.voyageone.service.model.cms.mongo.CmsMtChangeHistoryModel;
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
    @Resource
    CmsMtChangeHistoryDao historyDao;

    public List<CartBean> getAll(CartBean con) {
        List<CartBean> carts = cartDao.getAllCarts();
        Predicate<CartBean> filter = (bean) -> {

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
            if (con.getActive() != null) {
                flg = flg && con.getActive().equalsIgnoreCase(bean.getActive());
            }
            return flg;

        };
        return carts.stream().filter(filter)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public int saveOrUpdate(CartBean bean) {

        boolean isAdd = StringUtils.isEmptyOrWhitespaceOnly(bean.getCart_id());
        CmsMtChangeHistoryModel<CartBean> history = null;
        if (isAdd) {
            history = CmsMtChangeHistoryModel.build(null, bean, "CART Add", bean.getCreater());
        }else{
            history = CmsMtChangeHistoryModel.build(Carts.getCart(bean.getCart_id()), bean, "CART MODIFY", bean.getModifier());
        }

        int result=cartDao.insertOrUpdate(bean);
        historyDao.insert(history);
        return result;
    }

    /**
     * 逻辑删除,将active设为0
     *
     * @return
     */
    public int deleteLogic(CartBean bean) {
        if (bean == null || StringUtils.isNullOrEmpty(bean.getCart_id())) {

            return 0;
        }
        int result = cartDao.deleteLogic(bean.getCart_id(), bean.getModifier());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());

        CmsMtChangeHistoryModel<CartBean> history = CmsMtChangeHistoryModel.build(bean, null, "CART DELETE LOGIC", bean.getModifier());
        historyDao.insert(history);

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
            CartBean originCart = Carts.getCart(bean.getCart_id());
            cartDao.insert(bean);
            CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());
            CmsMtChangeHistoryModel<CartBean> history = CmsMtChangeHistoryModel.build(originCart, bean, "CART ADD", bean.getCreater());
            historyDao.insert(history);
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                throw new BusinessException("CART_ID重复!");
            }
            throw new BusinessException("保存Cart失败!", e);
        }
    }
}
