package com.voyageone.cms.service;


import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsProductLogServiceTest {
    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Autowired
    CmsProductLogService productLogService;

    @Test
    public void testInsertCmsBtProduct() throws Exception {
        CmsBtProductModel ret = cmsBtProductDao.selectProductByCode("001", "00341");
        productLogService.insertProductHistory(ret);
    }
}