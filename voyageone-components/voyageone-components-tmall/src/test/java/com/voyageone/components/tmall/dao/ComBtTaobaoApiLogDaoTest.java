package com.voyageone.components.tmall.dao;

import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.dao.com.ComBtTaobaoApiLogDao;
import com.voyageone.service.model.com.ComBtTaobaoApiLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jonasvlag on 16/3/14.
 *
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class ComBtTaobaoApiLogDaoTest {

    @Autowired
    private ComBtTaobaoApiLogDao apiLogDao;

    @Test
    public void testInsert() throws Exception {

        ShopBean shopBean = new ShopBean();

        shopBean.setOrder_channel_id("999");
        shopBean.setCart_id("999");
        shopBean.setAppSecret("Test-AppSecret");
        shopBean.setAppKey("Test-AppKey");
        shopBean.setSessionKey("Test-SessionKey");

        ComBtTaobaoApiLogModel model = new ComBtTaobaoApiLogModel("Test Api Method", shopBean);

        int count = apiLogDao.insert(model);

        assert count > 0;
    }
}