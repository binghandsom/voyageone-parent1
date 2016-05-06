package com.voyageone.service.dao.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/22.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtPlatformDictDaoTest {

    @Autowired
    private CmsMtPlatformDictDao cmsMtPlatformDictDao;

    @Test
    public void testSelect() throws Exception {

        cmsMtPlatformDictDao.select(1);

    }
}