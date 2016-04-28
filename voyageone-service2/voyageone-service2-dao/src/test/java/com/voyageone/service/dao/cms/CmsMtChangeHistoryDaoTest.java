package com.voyageone.service.dao.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @description
 * @author: holysky.zhao
 * @date: 2016/4/28 11:50
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtChangeHistoryDaoTest {

    @Resource
    CmsBtConfigHistoryDao historyDao;

    @Test
    public void testSave() throws Exception {

//        CmsBtProductModel productOld = new CmsBtProductModel();
//        productOld.setCreater("zhao");
//        productOld.setCatId("dangerous");
//        CmsBtProductModel productNew = new CmsBtProductModel();
//        productNew.setCreater("zhao");
//        productNew.setCatId("dangerousAA");
//        model.setOriginalBean(productOld);
//        model.setNewBean(productNew);
//        model.setOperation("新增");
//        historyDao.insert(model);
    }
}
