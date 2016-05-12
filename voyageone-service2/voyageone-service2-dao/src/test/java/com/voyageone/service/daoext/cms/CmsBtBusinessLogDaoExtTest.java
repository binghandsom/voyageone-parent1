package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by Ethan Shi on 2016/4/26.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtBusinessLogDaoExtTest {

    @Autowired
    private CmsBtBusinessLogDaoExt cmsBtBusinessLogDaoExt;

    @Test
    public void testSelectByCondition() throws Exception {

        HashMap map = new HashMap();
        map.put("offset", 1);
        map.put("rows", 10);

        System.out.println(JacksonUtil.bean2Json(cmsBtBusinessLogDaoExt.selectByCondition(map)));
    }

    @Test
    public void testSelectByConditionCnt() throws Exception {
        HashMap map = new HashMap();
        map.put("offset", 1);
        map.put("rows", 10);

        System.out.println(JacksonUtil.bean2Json(cmsBtBusinessLogDaoExt.selectByConditionCnt(map)));
    }

    @Test
    public void testUpdateStatusFinish() throws Exception {

    }
}