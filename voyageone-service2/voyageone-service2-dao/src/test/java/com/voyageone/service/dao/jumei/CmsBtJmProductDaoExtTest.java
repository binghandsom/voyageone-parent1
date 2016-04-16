package com.voyageone.service.dao.jumei;

import com.voyageone.service.daoext.jumei.CmsBtJmProductDaoExt;
import com.voyageone.service.model.jumei.CmsBtJmProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtJmProductDaoExtTest {
    @Autowired
    CmsBtJmProductDaoExt daoExt;

    @Test
    public void  getByProductCodeChannelIdTest()
    {
        CmsBtJmProductModel model= daoExt.getByProductCodeChannelId("012","12137BMA-001");
    }
}
