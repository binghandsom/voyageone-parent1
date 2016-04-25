package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsMtCustomWordDaoExtTest {

    @Autowired
    private CmsMtCustomWordDaoExt cmsMtCustomWordDaoExt;

    @Test
    public void testSelectWithParam() throws Exception {
        System.out.println(JacksonUtil.bean2Json(cmsMtCustomWordDaoExt.selectWithParam()));

    }


}