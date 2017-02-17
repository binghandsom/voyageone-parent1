package com.voyageone.service.impl.cms.mqservice;

import com.voyageone.service.impl.cms.vomqjobservice.CmsProductBIDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by dell on 2017/1/4.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsProcductBIDataServiceTest {

    @Autowired
    CmsProductBIDataService service;

     @Test
    public  void  test() {
        service.sendMessage("010",23,"test");
    }
}
