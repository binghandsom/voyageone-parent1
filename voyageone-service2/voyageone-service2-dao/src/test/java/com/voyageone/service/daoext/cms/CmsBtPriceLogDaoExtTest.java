package com.voyageone.service.daoext.cms;

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
public class CmsBtPriceLogDaoExtTest {


    @Autowired
    private CmsBtPriceLogDaoExt cmsBtPriceLogDaoExt;

    @Test
    public void testSelectPriceLogByCode() throws Exception {

        HashMap map = new HashMap();
        map.put("code", "1");
        map.put("offset", 1);
        map.put("rows", 1);

        cmsBtPriceLogDaoExt.selectPriceLogByCode(map);

    }
}