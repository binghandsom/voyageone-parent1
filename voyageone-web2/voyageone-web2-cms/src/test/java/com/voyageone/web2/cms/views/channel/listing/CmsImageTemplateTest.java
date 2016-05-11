package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.CmsMtImageCreateTemplateDao;
import com.voyageone.service.dao.cms.mongo.CmsBtImageTemplateDao;
import com.voyageone.service.impl.cms.CmsImageTemplateService;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.CmsMtImageCreateTemplateModel;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsImageTemplateTest {

    @Autowired
    private CmsImageTemplateService service;

    @Autowired
    private CmsBtImageTemplateDao dao;

    @Autowired
    CmsMtImageCreateTemplateDao daoCmsMtImageCreateTemplate;

    @Test
    public void loadCmsImageTemplate() {

        List<CmsBtImageTemplateModel> list=dao.selectAll();

        for (CmsBtImageTemplateModel model:list) {
            if(model.getImageTemplateId()!=null&&model.getImageTemplateId()>30) {
                model.setImageTemplateType(2);
                service.save(model, "system");
            }
        }
//        CmsBtImageTemplateModel modelCmsBtImageTemplate=null;
//        List<CmsMtImageCreateTemplateModel> modelList = daoCmsMtImageCreateTemplate.selectList(null);
//        for (CmsMtImageCreateTemplateModel model:modelList) {
//            if(model.getId()>31)
//            {
//                 modelCmsBtImageTemplate=new CmsBtImageTemplateModel();
//                modelCmsBtImageTemplate.setImageTemplateId((long) model.getId());
//                modelCmsBtImageTemplate.setChannelId(model.getChannelId());
//                modelCmsBtImageTemplate.setImageTemplateContent(model.getContent());
//                modelCmsBtImageTemplate.setImageTemplateName(model.getName());
//                modelCmsBtImageTemplate.setCartId(20);
//                modelCmsBtImageTemplate.setViewType(1);
//                service.save(modelCmsBtImageTemplate,"system");
//            }
//
//        }
    }

}
