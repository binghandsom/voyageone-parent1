package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.bean.ComResourceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by Ethan Shi on 2016-08-17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")

public class AdminResServiceTest {

    @Autowired
    AdminResService adminResService;

    @Test
    public void testSearchRes() throws Exception {

        List<ComResourceBean> result = adminResService.searchRes("cms");

        System.out.println(JacksonUtil.bean2Json(result));
    }
}