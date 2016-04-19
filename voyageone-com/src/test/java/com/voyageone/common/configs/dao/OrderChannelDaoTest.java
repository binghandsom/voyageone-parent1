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

    @Test
    public void testInsert() throws Exception {
        OrderChannelBean bean = new OrderChannelBean();
        bean.setCart_ids("1,2");
//        order_channel_id, company_id, name, full_name, img_url, send_name, send_address, send_tel,
//         send_zip, screct_key, session_key, is_usjoi, cart_ids, created, creater, modified, modifier, status

        bean.setOrder_channel_id("500");
        bean.setCompany_id("1");
        bean.setName("ztw");
        bean.setFull_name("ztw");
        bean.setImg_url("ztw");
        bean.setSend_name("ztw");
        bean.setSend_address("ztw");
        bean.setSend_tel("ztw");
        bean.setSend_zip("ztw");
        bean.setScrect_key("ztw");
        bean.setSession_key("ztw");
        bean.setIs_usjoi(1);
//        bean.setCart_ids("1");
        bean.setCreater("ztw");
        dao.insertChannel(bean);


    }
}
