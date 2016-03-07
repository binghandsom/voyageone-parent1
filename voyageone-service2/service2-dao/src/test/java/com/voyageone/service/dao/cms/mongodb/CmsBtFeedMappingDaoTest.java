package com.voyageone.service.dao.cms.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Jonas, 12/25/15.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtFeedMappingDaoTest {

    @Autowired
    private CmsBtFeedMappingDao feedMappingDao;

    @Test
    public void testSelectByKey() {

        Object mapping = feedMappingDao.selectByKey("013", null, null);

        assert mapping != null;
    }
}