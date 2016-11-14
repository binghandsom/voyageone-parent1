package com.voyageone.task2.cms.service;

import com.voyageone.service.model.cms.CmsBtShelvesProductModel;
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
    CmsShelvesMonitorMQService cmsShelvesMonitorMQService;

    @Test
    public void setInfo() throws Exception {
//        "15447387-BEEHIVEYELLOWACCENT",
//                "15447387-GRAPEFIZZACCENT",
//                "15447387-HONEYSUCKLEACCENT",
                List< CmsShelvesMonitorMQService.SkuBean > resultList = new ArrayList<>();
        CmsShelvesMonitorMQService.SkuBean skuBean = new CmsShelvesMonitorMQService.SkuBean("15097328",1);

        resultList.add(skuBean);

        skuBean = new CmsShelvesMonitorMQService.SkuBean("15097330",3);
        resultList.add(skuBean);
        skuBean = new CmsShelvesMonitorMQService.SkuBean("15097335",4);
        resultList.add(skuBean);
        skuBean = new CmsShelvesMonitorMQService.SkuBean("15097333",1);
        resultList.add(skuBean);
        skuBean = new CmsShelvesMonitorMQService.SkuBean("15097338",2);
        resultList.add(skuBean);

        List<CmsBtShelvesProductModel> cmsBtShelvesProductModels = new ArrayList<>();
        CmsBtShelvesProductModel cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
        cmsBtShelvesProductModel.setProductCode("15447387-BEEHIVEYELLOWACCENT");
        cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
        cmsBtShelvesProductModel = new CmsBtShelvesProductModel();
        cmsBtShelvesProductModel.setProductCode("15447387-GRAPEFIZZACCENT");
        cmsBtShelvesProductModels.add(cmsBtShelvesProductModel);
        cmsShelvesMonitorMQService.setInfo("018","1",resultList,cmsBtShelvesProductModels);
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
        cmsShelvesMonitorMQService.syuPlatformInfo("018",23,"536204642617",cmsBtShelvesProductModels);
    }

}