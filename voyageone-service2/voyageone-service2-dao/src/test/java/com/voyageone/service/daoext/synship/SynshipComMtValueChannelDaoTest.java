package com.voyageone.service.daoext.synship;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class SynshipComMtValueChannelDaoTest {

    @Autowired
    private SynshipComMtValueChannelDao synshipComMtValueChannelDao;

    @Test
    public void test() {
        System.out.println(synshipComMtValueChannelDao.selectName("08020000,珍珠首饰,套", 43, "en", "010"));
    }


}
