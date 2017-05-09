package com.voyageone.service.impl.cms.jumei;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/10/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsBtJmPromotionDownloadImageZipServiceTest {

    @Autowired
    CmsBtJmPromotionDownloadImageZipService cmsBtJmPromotionDownloadImageZipService;
    @Test
    public void downImage() throws Exception {
        byte[] image = cmsBtJmPromotionDownloadImageZipService.downImage("http://image.voyageone.com.cn/is/image/sneakerhead/010-51A0HC13E1-00LCNB0-1?wid=2200&hei=2200");
        System.out.println(image.length);
    }

}