package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:META-INF/context-web2.xml","classpath*:META-INF/context-web2-mvc.xml"})
public class CmsImageTemplateTest {

    @Autowired
    private ImageTemplateService service;

//    @Autowired
//    private CmsBtImageTemplateDao dao;

//    @Autowired
//    CmsMtImageCreateTemplateDao daoCmsMtImageCreateTemplate;

    @Test
    public void loadCmsImageTemplate() {

//        List<CmsBtImageTemplateModel> list=dao.selectAll();
//
//        for (CmsBtImageTemplateModel model:list) {
//            if(model.getImageTemplateId()!=null&&model.getImageTemplateId()>30) {
//                model.setImageTemplateType(2);
//                service.save(model, "system");
//            }
//        }
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

//    @Test
//    public void testSelectOne() {
//        CmsBtImageTemplateModel model = dao.selectByTemplateId(30);
//        System.out.println(model);
//        model.setComment(model.getComment() + "_1");
//        dao.update(model);
//        model = dao.selectByTemplateId(30);
//        System.out.println(model);
//    }


}
