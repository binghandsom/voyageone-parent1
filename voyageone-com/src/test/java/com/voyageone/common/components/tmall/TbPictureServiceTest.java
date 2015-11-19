package com.voyageone.common.components.tmall;

import com.taobao.api.ApiException;
import com.taobao.api.response.PictureCategoryGetResponse;
import com.voyageone.common.components.tmall.bean.TbGetPicCategoryParam;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 淘宝图片接口单元测试
 * Created by Jonas on 11/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:test-context.xml")
public class TbPictureServiceTest {
    @Autowired
    private TbPictureService tbPictureService;

    @Test
    public void testMain() throws ApiException {
        ShopBean shopBean = new ShopBean(){{

        }};
        TbGetPicCategoryParam param = new TbGetPicCategoryParam() {{
            setPictureCategoryId(181141114316991400L);
        }};
        PictureCategoryGetResponse response = tbPictureService.getCategories(shopBean, param);
        System.out.println(response.getMsg());
    }
}