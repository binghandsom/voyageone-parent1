package com.voyageone.service.impl.cms;

import com.voyageone.service.model.cms.CmsBtChannelCategoryModel;
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
    ChannelCategoryService cmsBtChannelCategoryService;

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
        List<CmsBtChannelCategoryModel> lst = new ArrayList<>();
//        lst.add(new CmsBtChannelCategoryModel("001", "50012029", "123"));
        CmsBtChannelCategoryModel obj = new CmsBtChannelCategoryModel();
        obj.setCategoryId("112233");
        obj.setChannelId("445566");
        lst.add(obj);

        cmsBtChannelCategoryService.saveWithList(lst);
    }
}
