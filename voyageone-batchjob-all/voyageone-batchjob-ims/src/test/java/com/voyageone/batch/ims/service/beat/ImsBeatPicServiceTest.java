package com.voyageone.batch.ims.service.beat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Jonas on 10/30/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ImsBeatPicServiceTest {

    @Autowired
    private ImsBeatPicService imsBeatPicService;

    @Test
    public void startupTest() {
        imsBeatPicService.startup();
    }
}