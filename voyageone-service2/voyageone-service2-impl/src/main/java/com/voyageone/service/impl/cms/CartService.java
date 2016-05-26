package com.voyageone.service.impl.cms;


import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.beans.CartBean;
import com.voyageone.common.configs.dao.ShopDao;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtConfigHistoryDao;
import com.voyageone.service.model.cms.mongo.CmsBtConfigHistory;
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
    CmsBtConfigHistoryDao historyDao;

    public List<CartBean> getCarts(CartBean con) {
        return cartDao.getCarts(con);
    }


    public int saveOrUpdate(CartBean bean) {

        boolean isAdd = com.mysql.jdbc.StringUtils.isEmptyOrWhitespaceOnly(bean.getCart_id());
        CmsBtConfigHistory<CartBean> history = null;
        if (isAdd) {
            history = CmsBtConfigHistory.build(null, bean, "CART Add", bean.getCreater());
        }else{
            history = CmsBtConfigHistory.build(Carts.getCart(bean.getCart_id()), bean, "CART MODIFY", bean.getModifier());
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
        if (bean == null || com.mysql.jdbc.StringUtils.isNullOrEmpty(bean.getCart_id())) {

            return 0;
        }
        int result = cartDao.deleteLogic(bean.getCart_id(), bean.getModifier());
        CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());

        CmsBtConfigHistory<CartBean> history = CmsBtConfigHistory.build(bean, null, "CART DELETE LOGIC", bean.getModifier());
        historyDao.insert(history);

        return result;
    }

    public void save(CartBean bean) {
        try {
            CartBean oldCart = cartDao.getCart(bean.getCart_id());
            if (oldCart != null) {
                if ("1".equals(oldCart.getActive())) {
                    // 主键存在,并且非逻辑删除的情况
                    throw new BusinessException("CART_ID重复!");
                } else {
                    // 主键存在,但是逻辑删除的情况
                    bean.setActive("1");
                    cartDao.update(bean);
                }
            } else {
                // 主键不存在的情况（包括逻辑删除也不存在）
                cartDao.insert(bean);
            }
            CacheHelper.delete(CacheKeyEnums.KeyEnum.ConfigData_CartConfigs.toString());
            CmsBtConfigHistory<CartBean> history = CmsBtConfigHistory.build(null, bean, "CART ADD", bean.getCreater());
            historyDao.insert(history);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("保存Cart失败!", e);
        }
    }
}
