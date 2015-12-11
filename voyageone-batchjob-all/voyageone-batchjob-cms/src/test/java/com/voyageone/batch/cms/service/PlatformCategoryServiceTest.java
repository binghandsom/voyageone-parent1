package com.voyageone.batch.cms.service;

;
import com.voyageone.cms.service.dao.mongodb.CmsMtPlatformCategoryDao;
import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lewis on 15-11-28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class PlatformCategoryServiceTest {

    @Autowired
    CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Test
    public void testOnStartup() throws Exception {

        //删除原有cartId =23的类目信息
//        cmsMtPlatformCategoryDao.deletePlatformCategories(23,"001");
//        List<CmsMtPlatformCategoryTreeModel> brforeTreeModels = cmsMtPlatformCategoryDao.selectPlatformCategoriesByCartId(23);
//        Assert.assertFalse(brforeTreeModels.size()>0);

        //插入类目信息
        platformCategoryService.startup();

//        //查询插入后的结果，并断言
//        List<CmsMtPlatformCategoryTreeModel> afterTreeModels = cmsMtPlatformCategoryDao.selectPlatformCategoriesByCartId(23);
//
//        Assert.assertTrue(afterTreeModels.size()>0);


    }

}
