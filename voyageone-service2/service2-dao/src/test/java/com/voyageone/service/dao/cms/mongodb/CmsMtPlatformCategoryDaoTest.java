package com.voyageone.service.dao.cms.mongodb;

import com.voyageone.cms.service.model.CmsMtPlatformCategoryTreeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2015/12/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtPlatformCategoryDaoTest {

    @Autowired
    CmsMtPlatformCategoryDao cmsMtPlatformCategoryDao;

    @Test
    public void testSelectPlatformCategoriesByCartId() {
        int catId = 23;
        List<CmsMtPlatformCategoryTreeModel> lst = cmsMtPlatformCategoryDao.selectPlatformCategoriesByCartId(catId);
        for (CmsMtPlatformCategoryTreeModel model:lst) {
            System.out.println(model);
        }
    }

    @Test
    public void testSelectById() {
        CmsMtPlatformCategoryTreeModel model = cmsMtPlatformCategoryDao.selectById("5663df5c62b800d4e44e5a10");
        System.out.println(model);
    }
}
