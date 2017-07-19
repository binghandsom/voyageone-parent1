package com.voyageone.service.impl.cms.usa;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class UsaProductDetailServiceTest {
    @Test
    public void getMastProductInfo() throws Exception {

    }

    @Test
    public void getProductPlatform1() throws Exception {

    }

    @Test
    public void updateCommonProductInfo() throws Exception {

    }

    @Test
    public void updateFreeTag() throws Exception {

    }

    @Test
    public void updateProductPlatform() throws Exception {

    }

    @Autowired
    UsaProductDetailService usaProductDetailService;
    @Test
    public void getProductPlatform() throws Exception {

        Map<String, Object> o= usaProductDetailService.getMastProductInfo("001",4361020L);
        System.out.println(JacksonUtil.bean2Json(o));
    }

}