package com.voyageone.service.impl.cms;

import com.voyageone.service.model.cms.CmsMtChannelCategoryConfigModel;
import com.voyageone.service.model.cms.mongo.CmsMtCategoryTreeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2015/12/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtChannelCategoryServiceTest {

    @Autowired
    private ChannelCategoryService cmsBtChannelCategoryService;

    @Test
    public void testGeCategorysByChannelId() {
        String channelId = "013";
        List<CmsMtCategoryTreeModel> lst = cmsBtChannelCategoryService.getCategoriesByChannelId(channelId);
        for (CmsMtCategoryTreeModel model:lst) {
            System.out.println(model);
        }
    }

    @Test
    public void testCmsBtCategoryMappingDaoInsert() throws Exception {
        List<CmsMtChannelCategoryConfigModel> lst = new ArrayList<>();
//        lst.add(new CmsBtChannelCategoryModel("001", "50012029", "123"));
        CmsMtChannelCategoryConfigModel obj = new CmsMtChannelCategoryConfigModel();
        obj.setCategoryId("112233");
        obj.setChannelId("456");
        obj.setCreater("System");
        lst.add(obj);

        obj = new CmsMtChannelCategoryConfigModel();
        obj.setCategoryId("112234");
        obj.setChannelId("456");
        obj.setCreater("System");
        lst.add(obj);

        obj = new CmsMtChannelCategoryConfigModel();
        obj.setCategoryId("112235");
        obj.setChannelId("456");
        obj.setCreater("System");
        lst.add(obj);

        cmsBtChannelCategoryService.saveWithListOne(lst);
    }
}
