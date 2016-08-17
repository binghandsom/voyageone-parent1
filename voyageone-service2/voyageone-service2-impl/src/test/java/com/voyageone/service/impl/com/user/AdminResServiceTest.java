package com.voyageone.service.impl.com.user;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.security.bean.ComResourceBean;
import com.voyageone.security.model.ComResourceModel;
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

    @Test
    public void testAddRes() throws Exception {

        ComResourceModel model = new ComResourceModel();
        model.setApplication("cms");
        model.setResKey("test_key");
        model.setResName("test_name");
        model.setIcon("");
        model.setCreater("test");
        model.setParentId(1);
        model.setDescription("xxxxx");
        model.setResType(1);

        adminResService.addRes(model);

    }
}