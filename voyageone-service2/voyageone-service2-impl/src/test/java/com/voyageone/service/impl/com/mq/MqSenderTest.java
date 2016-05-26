package com.voyageone.service.impl.com.mq;

import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
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

    private int[] taskIds = {42965};

    @Test
    public void testSendMessage() throws Exception {
//        for (int i=0;i<10;i++) {
//            Map<String, Object> message = new HashMap<>();
//            message.put("id", String.valueOf(i));
//            sender.sendMessage("voyageone_mq_error_handle_testing", message);
//        }

        for (int i : taskIds) {
            Map<String, Object> message = new HashMap<>();
            message.put("id", i);
            sender.sendMessage(MqRoutingKey.CMS_BATCH_CmsMtImageCreateTaskJob, message);
        }
    }

}