package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsProductAddUpdateMQMessageBody;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/7/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsProductAddUpdateMQJobTest {

    @Autowired
    CmsProductAddUpdateMQJob cmsProductAddUpdateMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsProductAddUpdateMQMessageBody cmsProductAddUpdateMQMessageBody = new CmsProductAddUpdateMQMessageBody();
//        cmsProductAddUpdateMQMessageBody.setChannelId("001");
//        cmsProductAddUpdateMQMessageBody.setCode("james001");
//        cmsProductAddUpdateMQMessageBody.setColor("red");
//        cmsProductAddUpdateMQMessageBody.setName("jamestest1");
//        cmsProductAddUpdateMQMessageBody.setStatus(0);
//        cmsProductAddUpdateMQMessageBody.setSkuList(new ArrayList<>());
//        CmsProductAddUpdateMQMessageBody.SkuModel sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
//        sku.setBarcode("11111");
//        sku.setSku("james001-001");
//        sku.setSize("10");
//        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);
//        sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
//        sku.setBarcode("2222");
//        sku.setSku("james001-002");
//        sku.setSize("20");
//        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);


        cmsProductAddUpdateMQMessageBody.setChannelId("001");
        cmsProductAddUpdateMQMessageBody.setCode("james001");
        cmsProductAddUpdateMQMessageBody.setColor("red");
        cmsProductAddUpdateMQMessageBody.setName("jamestest1");
        cmsProductAddUpdateMQMessageBody.setStatus(1);
        cmsProductAddUpdateMQMessageBody.setSkuList(new ArrayList<>());
        CmsProductAddUpdateMQMessageBody.SkuModel sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
        sku.setBarcode("11111");
        sku.setSku("68220-gem-xxxl");
        sku.setSize("xxxl");
        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);
        sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
        sku.setBarcode("2222");
        sku.setSku("68220-gem-xxl");
        sku.setSize("xxl");
        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);
//        sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
//        sku.setBarcode("3333");
//        sku.setSku("68220-gem-xxxxl");
//        sku.setSize("xxxxl");
//        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);
//        sku = new CmsProductAddUpdateMQMessageBody.SkuModel();
//        sku.setBarcode("333");
//        sku.setSku("james001-003");
//        sku.setSize("30");
//        cmsProductAddUpdateMQMessageBody.getSkuList().add(sku);
        cmsProductAddUpdateMQJob.onStartup(cmsProductAddUpdateMQMessageBody);
    }
}