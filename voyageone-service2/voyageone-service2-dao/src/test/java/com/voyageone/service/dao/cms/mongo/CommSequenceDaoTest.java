package com.voyageone.service.dao.cms.mongo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by DELL on 2015/12/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CommSequenceDaoTest {

    @Autowired
    private CommSequenceDao commSequenceDao;

    @Test
    public void testGetNextSequence() {
        System.out.println(commSequenceDao.getNextSequence("userid"));
    }

    @Test
    public void testGetNextSequence1() {
        System.out.println(commSequenceDao.getNextSequence("userid1"));
    }

}
