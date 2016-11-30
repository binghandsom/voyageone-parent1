package com.voyageone.task2.cms.service;

import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2016/11/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsShelvesMonitorServiceTest {

    @Autowired
    CmsShelvesMonitorMQService cmsShelvesMonitorMQService;


    @Test
    public void setInfo() throws Exception {
//

        Map<String,Object> map = new HashedMap();
        map.put("shelvesId",6);
        cmsShelvesMonitorMQService.onStartup(map);
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