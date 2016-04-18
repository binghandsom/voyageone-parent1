package com.voyageone.common.configs.dao;

import com.google.common.collect.Lists;
import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.CartBean;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/13 17:01
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class CartDaoTest extends BaseTest{
    @Resource
    CartDao cartDao;


    @Test
    public void testGetById() throws Exception {
        CartBean byId = cartDao.getById("20");
        Assert.assertNotNull(byId);
        CartBean nullObj = cartDao.getById("xxxxffaffaf");
        Assert.assertNull(nullObj);


    }

    @Test
    public void testGetByIds() throws Exception {
        List<CartBean> result = cartDao.getByIds(Lists.newArrayList("20","21","22","23"));
        Assert.assertTrue(result.size()==4);
    }

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
        cartDao.saveOrUpdate(cartBean);
        CartBean bean = cartDao.getById("108");
        Assert.assertNotNull(bean);
        Assert.assertNotNull(bean.getModified());


    }

    @Test
    public void testDeleteLogic() throws Exception {

    }
}
