package com.voyageone.common.configs.dao;

import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.OrderChannelBean;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/13 14:58
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class OrderChannelDaoTest extends BaseTest{

    @Resource
    OrderChannelDao dao;

    @Test
    public void testGetAll() throws Exception {

    }

    @Test
    public void testUpdateById() throws Exception {
        OrderChannelBean bean = new OrderChannelBean();
        bean.setOrder_channel_id("001");
        bean.setModifier("1234");
        bean.setIs_usjoi(2);
        bean.setCart_ids("1,2,3,4,5,6");
        dao.updateById(bean);
    }
}
