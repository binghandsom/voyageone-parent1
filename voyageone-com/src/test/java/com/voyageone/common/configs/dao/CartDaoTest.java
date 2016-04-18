package com.voyageone.common.configs.dao;

import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.CartBean;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/13 17:01
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class CartDaoTest extends BaseTest{
    @Resource
    ShopDao cartDao;




    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testSaveOrUpdate() throws Exception {

        CartBean cartBean = new CartBean();
        cartBean.setCart_id("109");
        cartBean.setName("zhaotianwu222");
        cartBean.setShort_name("ztw");
        cartBean.setDescription("test");
        cartBean.setPlatform_id("1");
        cartBean.setCart_type("1");
        cartDao.insertOrUpdate(cartBean);


    }

    @Test
    public void testDeleteLogic() throws Exception {

    }
}
