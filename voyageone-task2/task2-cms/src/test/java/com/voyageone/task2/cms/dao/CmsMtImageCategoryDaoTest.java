package com.voyageone.task2.cms.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsMtImageCategoryDaoTest {
    @Autowired
    private CmsMtImageCategoryDao cmsMtImageCategoryDao;

    @Test
    public void testDelete() {
        cmsMtImageCategoryDao.delete(1);
    }
}