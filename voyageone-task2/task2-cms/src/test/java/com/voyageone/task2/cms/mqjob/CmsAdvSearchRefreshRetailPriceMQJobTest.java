package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.configs.MQConfigInitTestUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchRefreshRetailPriceMQJobTest {

    @Autowired
    private CmsAdvSearchRefreshRetailPriceMQJob cmsAdvSearchRefreshRetailPriceMQJob;

    @Test
    public void onStartup() throws Exception {
        MQConfigInitTestUtil.startMQ(cmsAdvSearchRefreshRetailPriceMQJob);
    }

}