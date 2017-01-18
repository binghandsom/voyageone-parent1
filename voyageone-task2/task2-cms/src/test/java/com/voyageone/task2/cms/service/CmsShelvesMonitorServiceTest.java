package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesMonitorMQMessageBody;
import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import com.voyageone.task2.cms.mqjob.CmsShelvesMonitorMQJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2016/11/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsShelvesMonitorServiceTest {

    @Autowired
    CmsShelvesMonitorMQJob cmsShelvesMonitorMQJob;


    @Test
    public void setInfo() throws Exception {
//

        CmsShelvesMonitorMQMessageBody map = new CmsShelvesMonitorMQMessageBody();
        map.setShelvesId(6);
        cmsShelvesMonitorMQJob.onStartup(map);
    }

    @Test
    public void syuPlatformInfo(){
        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = new ArrayList<>();
        CmsBtShelvesProductModel cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
        cmsBtShelvesProductModel.setProductCode("15447387-BEEHIVEYELLOWACCENT");
        cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
        cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
        cmsBtShelvesProductModel.setProductCode("15447387-GRAPEFIZZACCENT");
        cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
//        cmsShelvesMonitorMQService.syuPlatformInfo("018",23,"536204642617",cmsBtShelvesProductModels);
    }

}