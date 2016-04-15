package com.voyageone.common.configs.dao;

import com.google.common.collect.Lists;
import com.voyageone.base.dao.BaseDao;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.beans.CartBean;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/13 16:44
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@Repository
public class CartDao extends BaseDao {

    public CartBean getById(String id) {
        List<CartBean> result = getByIds(Lists.newArrayList(id));
        return result.size() > 0 ? result.get(0):null;
    }

    public List<CartBean> getByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_cart_getByIds", ids);
    }

    public List<CartBean> getAll() {
        return selectList(Constants.DAO_NAME_SPACE_COMMON + "tm_cart_getByIds", null);
    }
}
