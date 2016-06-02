package com.voyageone.task2.cms.mongoDao;

import com.jayway.jsonpath.JsonPath;
import com.voyageone.service.dao.cms.mongo.CmsMtPlatformCategoryDao;
import com.voyageone.service.model.cms.mongo.CmsMtPlatformCategoryTreeModel;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lewis on 15-11-30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class PlatformCategoryDaoTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CmsMtPlatformCategoryDao platformCategoryDao;

    @Test
    public void testSavePlatformCategories() throws Exception {

    }

    @Test
    public void testDeletePlatformCategories() throws Exception {
        platformCategoryDao.deletePlatformCategories(23,"001");
    }


    @Test
    public void findLeavesCategory(){
        List<Map> leaves = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> leafObjs = new ArrayList<>();
        List<CmsMtPlatformCategoryTreeModel> models = platformCategoryDao.selectPlatformCategoriesByCartId(23);

        for (CmsMtPlatformCategoryTreeModel model:models){
            Object jsonObj = JsonPath.parse(model.toString()).json();
            List<Map> nodes = JsonPath.read(jsonObj, "$..children[?(@.isParent == 0)]");
            leaves.addAll(nodes);
        }

        for (Map leafMap:leaves){
            CmsMtPlatformCategoryTreeModel leafObj = new CmsMtPlatformCategoryTreeModel();
            try {
                BeanUtils.populate(leafObj, leafMap);
                leafObjs.add(leafObj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getMessage(), e);
            }
        }

        Assert.assertTrue(leaves.size()>0);

    }
}