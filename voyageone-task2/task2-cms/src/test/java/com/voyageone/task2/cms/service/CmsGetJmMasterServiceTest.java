package com.voyageone.task2.cms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.dao.JmCategoryDao;
import com.voyageone.task2.cms.dao.JmMasterDao;
import com.voyageone.components.jumei.bean.JmCategoryBean;
import com.voyageone.common.configs.beans.ShopBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<TaskControlBean> taskControlList = new ArrayList<>();
        cmsGetJmMasterService.onStartup(taskControlList);
    }

    @Test
    public void testInsertCategory() throws Exception {
        String categorPath = "Necklaces & Pendants - Pendants - Mother-of-Pearl";
        List<String> categors = java.util.Arrays.asList(categorPath.split(" - "));
        categorPath = categors.stream().map(s -> s.replace("-", "－")).collect(Collectors.joining("-"));

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
        //jmMasterDao.clearJmMaster();
        jmCategoryDao.insertJmCategory(categorys);
    }

    @Test
    public void testInsertBrand() throws Exception {

    }

    @Test
    public void testInserCurrency() throws Exception {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("131");
        shopBean.setSessionKey("7e059a48c30c67d2693be14275c2d3be");
        shopBean.setAppSecret("0f9e3437ca010f63f2c4f3a216b7f4bc9698f071");
        shopBean.setApp_url("http://openapi.ext.jumei.com/");
        cmsGetJmMasterService.insertBrand(shopBean);

    }

}