package com.voyageone.cms.service.dao.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by lewis on 2016/2/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtFeedInfoDaoTest {

    @Autowired
    CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Test
    public void testUpdateFeedInfoUpdFlg() throws Exception {

       int count = cmsBtFeedInfoDao.updateFeedInfoUpdFlg("013","GAU-80306");

        System.out.println(count);

    }
}