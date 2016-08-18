package com.voyageone.task2.cms.service.product;

import com.voyageone.common.configs.Enums.CacheKeyEnums;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.configs.dao.ConfigDaoFactory;
import com.voyageone.common.redis.CacheHelper;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason.jiang on 2016/08/03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsGetPlatformStatusServiceTest {

    @Autowired
    CmsGetPlatformStatusService targetService;

    private static final String KEY = CacheKeyEnums.KeyEnum.ConfigData_ShopConfigs.toString();

    @Before
    public void setUp() {
        // 准备参数
        Shops.reload();
        Map<String, ShopBean> shopBeanMap = new HashMap<>();
        // tmall
        ShopBean bean1 = new ShopBean();
        bean1.setCart_id("23");
        bean1.setPlatform_id("1");
        bean1.setOrder_channel_id("010");
        bean1.setApp_url("http://gw.api.taobao.com/router/rest");

        shopBeanMap.put(buildKey(bean1.getCart_id(), bean1.getOrder_channel_id()), bean1);
        // jingdong
        ShopBean bean2 = new ShopBean();
        bean2.setCart_id("28");
        bean2.setPlatform_id("2");
        bean2.setOrder_channel_id("928");
        bean2.setApp_url("https://api.jd.com/routerjson");

        shopBeanMap.put(buildKey(bean2.getCart_id(), bean2.getOrder_channel_id()), bean2);
        CacheHelper.reFreshSSB(KEY, shopBeanMap);
    }

    @Test
    public void testTMPlatform() {
        List<TaskControlBean> paramList = new ArrayList<>();
        TaskControlBean param = new TaskControlBean();
        param.setTask_id("CmsGetPlatformStatusJob");
        param.setCfg_name("run_flg");
        param.setCfg_val1("1");
        paramList.add(param);

        TaskControlBean param2 = new TaskControlBean();
        param2.setTask_id("CmsGetPlatformStatusJob");
        param2.setCfg_name("channel_id");
        param2.setCfg_val1("010");
        paramList.add(param2);

        TaskControlBean param3 = new TaskControlBean();
        param3.setTask_id("CmsGetPlatformStatusJob");
        param3.setCfg_name("cart_id");
        param3.setCfg_val1("23");
        paramList.add(param3);

        try {
            targetService.onStartup(paramList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJDPlatform() {
        List<TaskControlBean> paramList = new ArrayList<>();
        TaskControlBean param = new TaskControlBean();
        param.setTask_id("CmsGetPlatformStatusJob");
        param.setCfg_name("run_flg");
        param.setCfg_val1("1");
        paramList.add(param);

        TaskControlBean param2 = new TaskControlBean();
        param2.setTask_id("CmsGetPlatformStatusJob");
        param2.setCfg_name("channel_id");
        param2.setCfg_val1("928");
        paramList.add(param2);

        TaskControlBean param3 = new TaskControlBean();
        param3.setTask_id("CmsGetPlatformStatusJob");
        param3.setCfg_name("cart_id");
        param3.setCfg_val1("28");
        paramList.add(param3);

        try {
            targetService.onStartup(paramList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * build redis hash Key
     */
    private static String buildKey(String cart_id, String order_channel_id) {
        return cart_id + CacheKeyEnums.SKIP + order_channel_id;
    }

}