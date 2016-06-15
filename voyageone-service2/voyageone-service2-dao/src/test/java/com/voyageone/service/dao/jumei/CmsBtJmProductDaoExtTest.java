package com.voyageone.service.dao.jumei;

import com.voyageone.service.daoext.cms.CmsBtJmProductDaoExt;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtJmProductDaoExtTest {
    @Autowired
    private CmsBtJmProductDaoExt daoExt;

    @Test
    public void getByProductCodeChannelIdTest() {
        CmsBtJmProductModel model = daoExt.selectByProductCodeChannelId("012", "12137BMA-001");
    }
}
