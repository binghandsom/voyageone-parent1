package com.voyageone.web2.cms.views.product;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * @author james.li on 2016/6/14.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsProductDetailServiceTest {

    @Autowired
    CmsProductDetailService cmsProductDetailService;
    @Test
    public void testGetMastProductInfo() throws Exception {
        Map<String, Object> result =  cmsProductDetailService.getMastProductInfo("010", 5938L, "cn");
        System.out.println(JacksonUtil.bean2Json(result));
    }
}