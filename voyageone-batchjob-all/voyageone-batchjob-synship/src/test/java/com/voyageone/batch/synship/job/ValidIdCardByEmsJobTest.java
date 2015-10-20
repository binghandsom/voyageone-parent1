package com.voyageone.batch.synship.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Jonas on 9/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:syn-ship-test-spring-context.xml")
public class ValidIdCardByEmsJobTest {

    @Autowired
    private SynShipValidIdCardJob validIdCardByEmsJob;

    @Test
    public void testRun() {
        validIdCardByEmsJob.run();
    }
}