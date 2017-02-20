package com.voyageone.task2.cms.service.jumei;

import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by dell on 2016/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class JmMallPromotionPriceSyncServiceTest {

    @Autowired
    JmMallPromotionPriceSyncService service;

    @Autowired
    CmsBtJmPromotionProductDaoExt cmsBtJmPromotionProductDaoExt;

    @Autowired
    CmsBtJmPromotionProductDao cmsBtJmPromotionProductDao;
    @Test
    public void testOnStartup() throws Exception {
//        service.onStartup(null);

        CmsBtJmPromotionProductModel cmsBtJmPromotionProductModel = cmsBtJmPromotionProductDaoExt.selectByProductCodeChannelIdCmsBtJmPromotionId("101-091:SZ7", "010",1);
        cmsBtJmPromotionProductModel.setErrorMsg("eeeeeee");
        cmsBtJmPromotionProductModel.setModifier("ffff");
        cmsBtJmPromotionProductModel.setModified(new Date());
        cmsBtJmPromotionProductDao.update(cmsBtJmPromotionProductModel);}
}
