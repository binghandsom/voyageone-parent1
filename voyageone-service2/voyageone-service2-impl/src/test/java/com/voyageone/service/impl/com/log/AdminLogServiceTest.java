package com.voyageone.service.impl.com.log;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.user.ComLogModel;
import com.voyageone.service.bean.com.PaginationResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016-08-24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class AdminLogServiceTest {

    @Autowired
    AdminLogService adminLogService;

    @Test
    public void testSearchLog() throws Exception {

        PaginationResultBean<ComLogModel> result = adminLogService.searchLog(new ComLogModel(), 10000L, null, 1, 10);
        System.out.println(JacksonUtil.bean2Json(result));

    }

    @Test
    public void testGetLog() throws Exception {
        System.out.println(JacksonUtil.bean2Json(adminLogService.getLog(489)));

    }
}