package com.voyageone.service.daoext.vms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by vantis on 16-7-7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class VmsBtOrderDetailDaoExtTest {

    @Autowired
    VmsBtOrderDetailDaoExt vmsBtOrderDetailDaoExt;

    @Test
    public void selectListLimitedByTime() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("channelId", "001");
    }

}