package com.voyageone.cms.service;


import com.voyageone.cms.service.dao.CmsBtCategoryMappingDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtCategoryMappingModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCategoryTreeServiceTest {

    @Autowired
    CmsMtCategoryTreeService cmsMtCategoryTree;

    @Autowired
    CmsBtCategoryMappingDao cmsBtCategoryMappingDao;

    @Test
    public void testCreateCmsMtCategoryTreeFromPlatform1() throws Exception {
        boolean ret = cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform("001", 23);
        System.out.println(ret);
    }

    @Test
    public void testCreateCmsMtCategoryTreeFromPlatform2() throws Exception {
        boolean ret = cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform("001", 23, "50510002");

    }

}

