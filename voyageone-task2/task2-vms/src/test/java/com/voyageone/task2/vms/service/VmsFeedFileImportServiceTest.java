package com.voyageone.task2.vms.service;

import com.voyageone.service.impl.com.mq.MqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeff.duan on 16/06/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-vms-test.xml")
public class VmsFeedFileImportServiceTest {

    @Autowired
    private MqSender sender;

    @Test
    public void testOnStartup() throws Exception {
        Map<String, Object> message = new HashMap<>();
        message.put("channelId", "088");
        message.put("fileName", "C:/usr/web/contents/vms/feed/upload/088/Feed_djsjeff088_20160705_170309.csv");
        sender.sendMessage("voyageone_mq_vms_feed_file_import", message);
    }
}