package com.voyageone.service.impl.cms;

import com.taobao.api.ApiException;
import com.taobao.api.domain.ScItemMap;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.service.TbScItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author tom.zhu on 2016/10/08.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class TaobaoScItemServiceTest {

    @Autowired
    TaobaoScItemService taobaoScItemService;

    @Autowired
    TbScItemService tbScItemService;

    private ShopBean getShopBean() {
        ShopBean shopBean = Shops.getShop("018", 23);
        shopBean.setApp_url("http://gw.api.taobao.com/router/rest");
        shopBean.setAppKey("");
        shopBean.setAppSecret("");
        shopBean.setSessionKey("");

        return shopBean;
    }

    /**
     * 主逻辑测试
     * @throws Exception
     */
    @Test
    public void testTaobaoScItem() throws Exception {
        taobaoScItemService.doSetScItem(getShopBean(), 532900631201L);
    }

    /**
     * 获取关联信息
     * @throws Exception
     */
    @Test
    public void testGetScItemMap() throws Exception {

        try {
            List<ScItemMap> mapList = tbScItemService.getScItemMap(getShopBean(), 532900631201L, null);
            System.out.println(mapList.size());
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

}