package com.voyageone.task2.cms.service.tools;

import com.voyageone.service.bean.cms.tools.RefreshProductsBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

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

    private static final RefreshProductsBean testCase1 = new RefreshProductsBean() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId("sell_points");
        setAllProduct(false);
    }};

    private static final RefreshProductsBean testCase2 = new RefreshProductsBean() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId("sell_points");
        setAllProduct(true);
    }};

    private static final RefreshProductsBean testCase3 = new RefreshProductsBean() {{
        setCartId(23);
        setCategoryPath("珠宝/钻石/翡翠/黄金>彩色宝石/贵重宝石>手饰");
        setCategoryType(2);
        setChannelId("010");
        setFieldId(null);
        setAllProduct(false);
    }};

    private static final RefreshProductsBean testCase4 = new RefreshProductsBean() {{
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
        map.put("refreshProductsBean", testCase1);
        cmsRefreshProductsJobService.onStartup(map);
    }
}