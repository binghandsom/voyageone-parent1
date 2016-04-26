package com.voyageone.service.impl.com.transaction;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.impl.com.mq.MqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/4/25.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TransactionalTest {

    @Autowired
    private TranTest tranTest;

    @Test
    public void testTran() throws Exception {
        Thread.sleep(100000);
    }
}

@Service
class TranTest{

    @VOTransactional
    public void doing(){

    }
}
