package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.dao.JmCategoryDao;
import com.voyageone.batch.cms.dao.JmMasterDao;
import com.voyageone.common.components.jumei.Bean.JmCategoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author james.li on 2016/1/25.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsGetJmMasterServiceTest {

    @Autowired
    private JmCategoryDao jmCategoryDao;
    @Autowired
    private JmMasterDao jmMasterDao;

    @Autowired
    CmsGetJmMasterService cmsGetJmMasterService;
    @Test
    public void testOnStartup() throws Exception {

    }

    @Test
    public void testInsertCategory() throws Exception {
        List<JmCategoryBean> categorys = new ArrayList<>();
        JmCategoryBean cat= new JmCategoryBean();
        cat.setCategory_id(1);
        cat.setLevel("1");
        cat.setName("c");
        cat.setParent_category_id(0);
        cat.setCreater("james");
        cat.setModifier("james");
        categorys.add(cat);
        cat= new JmCategoryBean();
        cat.setCategory_id(2);
        cat.setLevel("2");
        cat.setName("d");
        cat.setParent_category_id(1);
        cat.setCreater("james");
        cat.setModifier("james");
        categorys.add(cat);
        jmCategoryDao.clearJmCategory();
        jmCategoryDao.insertJmCategory(categorys);
    }
    @Test
    public void testInsertMaster() throws Exception {
        List<JmCategoryBean> categorys = new ArrayList<>();
        JmCategoryBean cat= new JmCategoryBean();
        cat.setCategory_id(1);
        cat.setLevel("1");
        cat.setName("c");
        cat.setParent_category_id(0);
        cat.setCreater("james");
        cat.setModifier("james");
        categorys.add(cat);
        cat= new JmCategoryBean();
        cat.setCategory_id(2);
        cat.setLevel("2");
        cat.setName("d");
        cat.setParent_category_id(1);
        cat.setCreater("james");
        cat.setModifier("james");
        categorys.add(cat);
        jmMasterDao.clearJmMaster();
        jmCategoryDao.insertJmCategory(categorys);
    }
}