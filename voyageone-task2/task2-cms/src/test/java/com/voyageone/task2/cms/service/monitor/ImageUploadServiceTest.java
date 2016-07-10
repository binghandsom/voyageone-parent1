package com.voyageone.task2.cms.service.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author aooer 2016/6/2.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class ImageUploadServiceTest {

    @Autowired
    ImageUploadService imageUploadService;

//    @Test
//    public void testOnModify() throws Exception {
////        TimeUnit.DAYS.sleep(1);
//        imageUploadService.onModify("/Users/linanbin/.m2/repository/com/voyageone/task2-cms/2.0.0-SNAPSHOT/ImageUpload/images4/", "20160606.zip", "018");
//    }

    @Test
    public void testOnEvent() {
        String event = "close_write";
        String watchPath = "/usr/ImageUpload";
        String strPath = "/usr/ImageUpload/images6/";
        String strFileName = "11111.zip";

        String channelId = "018";
        File file = new File(strPath + strFileName);
        System.out.println((new File(file.getPath())).getName());
        System.out.println(file.getName());
        imageUploadService.onEvent(event, new File(strPath + strFileName), new FileMonitorBean(watchPath, channelId));
    }
}