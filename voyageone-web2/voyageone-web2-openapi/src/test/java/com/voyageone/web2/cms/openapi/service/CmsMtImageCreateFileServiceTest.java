package com.voyageone.web2.cms.openapi.service;

import com.voyageone.service.bean.openapi.image.AddListParameter;
import com.voyageone.service.bean.openapi.image.CreateImageParameter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtImageCreateFileServiceTest {

    @Autowired
    CmsImageFileService service;

    @Test
    public void test() throws Exception {
        // String url = "http://image.voyageone.net/product/getImage?cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1";//"test-test-1";//
        String vparam = "file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String queryString = "cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        service.getImage(cId, templateId, file, false, vparam, "测试创建");
    }

    @Test
    public  void  testAddList() {
        AddListParameter parameter = new AddListParameter();
        parameter.setData(new ArrayList<CreateImageParameter>());
        for (int i = 1; i < 500; i++) {
            parameter.getData().add(getCreateImageParameter(i));
        }
        service.addList(parameter);
    }

    CreateImageParameter getCreateImageParameter(int fileIndex) {
        String cId = "001";
        int templateId = 15;
        String file = "nike-air-penny-ii-333886005-1" + fileIndex;//"test-test-1";//
        String vparam = "file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";
        String queryString = "cId=001&templateId=15&file=nike-air-penny-ii-333886005-1&vparam=file:bcbg/bcbg-sku.png,file:bcbg/bcbgtupian.jpg,Text String to be rendered";

        CreateImageParameter parameter = new CreateImageParameter();
        parameter.setChannelId(cId);
        parameter.setFile(file);
        parameter.setVParam(vparam);
        parameter.setUploadUsCdn(true);
        parameter.setTemplateId(templateId);
        return parameter;
    }
}
