package com.voyageone.service.dao.cms.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 2016/2/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtFeedInfoDaoTest {

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    @Test
    public void testUpdateFeedInfoUpdFlg() throws Exception {

        String[] models = new String[]{};
        models[0] = "GAU-80306";
       int count = cmsBtFeedInfoDao.updateFeedInfoUpdFlg("013",models);

        System.out.println(count);

    }
}