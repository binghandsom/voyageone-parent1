package com.voyageone.cms.service;

import com.voyageone.cms.service.model.CmsBtCategoryMappingModel;
import com.voyageone.cms.service.model.CmsMtCategoryTreeModel;
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
public class CmsBtCategoryMappingServiceTest {

    @Autowired
    CmsBtCategoryMappingService cmsBtCategoryMappingService;

    @Test
    public void testGeCategorysByChannelId() {
        String channelId = "001";
        List<CmsMtCategoryTreeModel> lst = cmsBtCategoryMappingService.getCategorysByChannelId(channelId);
        for (CmsMtCategoryTreeModel model:lst) {
            System.out.println(model);
        }
    }

    @Test
    public void testCmsBtCategoryMappingDaoInsert() throws Exception {
        List<CmsBtCategoryMappingModel> lst = new ArrayList<>();
        lst.add(new CmsBtCategoryMappingModel("001", "122684003", "123"));
        lst.add(new CmsBtCategoryMappingModel("001", "50012029", "123"));
        cmsBtCategoryMappingService.saveWithList(lst);
    }
}
