package com.voyageone.cms.service.dao.mongodb;

import com.voyageone.cms.service.model.CmsMtCategorySchemaModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by lewis on 15-12-15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtCategorySchemaDaoTest {

    @Autowired
    private CmsMtCategorySchemaDao cmsMtCategorySchemaDao;

    @Test
    public void testGetMasterSchemaModelByCatId() throws Exception {

        String catId = "5omL6KGoPueRnuWjq+iFleihqA==";

        CmsMtCategorySchemaModel schemaModel = cmsMtCategorySchemaDao.getMasterSchemaModelByCatId(catId);

        System.out.println("");

    }

    @Test
    public void testGetMasterSchemaModelByCatIdEx() throws Exception {
        System.out.println("");
    }
}