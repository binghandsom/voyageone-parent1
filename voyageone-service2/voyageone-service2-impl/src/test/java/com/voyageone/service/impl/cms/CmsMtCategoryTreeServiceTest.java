package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCategoryTreeServiceTest {

    @Autowired
    CategoryTreeService cmsMtCategoryTree;

//    @Test
//    public void testCreateCmsMtCategoryTreeFromPlatform1() throws Exception {
//        boolean ret = cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform("010", 23);
//        System.out.println(ret);
//    }
//
//    @Test
//    public void testCreateCmsMtCategoryTreeFromPlatform2() throws Exception {
//        boolean ret = cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform("001", 23, "50510002");
//
//    }
//
//    @Test
//    public void testCreateCmsMtCategoryTreeFromPlatform3() throws Exception {
//        boolean ret = cmsMtCategoryTree.createCmsMtCategoryTreeFromPlatform(23);
//        System.out.println(ret);
//    }

    @Test
    public void testAddCategory() throws Exception {
        cmsMtCategoryTree.addCategory("不知如何匹配的类目","james");
    }
}

