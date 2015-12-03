package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.mongoDao.PlatformCategoryDao;
import com.voyageone.batch.cms.mongoModel.CmsMtPlatformCategoryTreeModel;
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
    PlatformCategoryDao platformCategoryDao;

    @Autowired
    PlatformCategoryService platformCategoryService;

    @Test
    public void testOnStartup() throws Exception {

        //删除原有cartId =23的类目信息
        platformCategoryDao.deletePlatformCategories(23);
        List<CmsMtPlatformCategoryTreeModel> brforeTreeModels = platformCategoryDao.selectPlatformCategoriesByCartId(23);
        Assert.assertFalse(brforeTreeModels.size()>0);

        //插入类目信息
        platformCategoryService.startup();

        //查询插入后的结果，并断言
        List<CmsMtPlatformCategoryTreeModel> afterTreeModels = platformCategoryDao.selectPlatformCategoriesByCartId(23);

        Assert.assertTrue(afterTreeModels.size()>0);


    }

}
