package com.voyageone.service.impl.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformSxImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by james on 2017/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsMtPlatformSxImageTemplateServiceTest {

    @Autowired
    CmsMtPlatformSxImageTemplateService cmsMtPlatformSxImageTemplateService;
    @Test
    public void getSxImageTemplateByChannelAndCart() throws Exception {
        List<CmsMtPlatformSxImageTemplateModel> cmsMtPlatformSxImageTemplateModels =  cmsMtPlatformSxImageTemplateService.getSxImageTemplateByChannelAndCart("001",23);
        System.out.println(JacksonUtil.bean2Json(cmsMtPlatformSxImageTemplateModels));
    }

}