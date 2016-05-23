package com.voyageone.service.impl.com.mq;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aooer 2016/2/29.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class MqSenderTest extends TestCase {

    @Autowired
    private MqSender sender;

    @Test
    public void testSendMessage() throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("id", "111");
        for (int i=0;i<100;i++)
        sender.sendMessage("voyageone_mq_error_handle_testing", message);
    }

}