package com.voyageone.batch.cms.mq;

import com.voyageone.common.mq.MqSender;
import com.voyageone.common.mq.enums.MqRoutingKey;
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
public class MqTest extends TestCase {

    @Autowired
    private MqSender sender;

    @Test
    public void testSendMessage() throws Exception {
        Map<String,Object> message=new HashMap<String,Object>();
        message.put("fromBundle","voyageone-batch");
        message.put("message","vooo");
        sender.sendMessage(MqRoutingKey.KEY1, message);
    }

}