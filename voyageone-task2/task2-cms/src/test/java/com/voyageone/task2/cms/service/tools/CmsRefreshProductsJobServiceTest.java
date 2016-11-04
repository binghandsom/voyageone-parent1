package com.voyageone.task2.cms.service.tools;

import com.voyageone.service.model.cms.CmsBtRefreshProductTaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * MQ 任务 CmsRefreshProductsJobService 的单元测试
 * Created by jonas on 2016/11/3.
 *
 * @version 2.9.0
 * @since 2.9.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsRefreshProductsJobServiceTest {

    @Autowired
    private CmsRefreshProductsJobService cmsRefreshProductsJobService;

    private static final CmsBtRefreshProductTaskModel testCase1 = new CmsBtRefreshProductTaskModel() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId("sell_points");
        setAllProduct(false);
    }};

    private static final CmsBtRefreshProductTaskModel testCase2 = new CmsBtRefreshProductTaskModel() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId("sell_points");
        setAllProduct(true);
    }};

    private static final CmsBtRefreshProductTaskModel testCase3 = new CmsBtRefreshProductTaskModel() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId(null);
        setAllProduct(false);
    }};

    private static final CmsBtRefreshProductTaskModel testCase4 = new CmsBtRefreshProductTaskModel() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId(null);
        setAllProduct(true);
    }};

    @Test
    public void onStartup() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("CmsBtRefreshProductTaskModel", testCase1);
        cmsRefreshProductsJobService.onStartup(map);
    }
}