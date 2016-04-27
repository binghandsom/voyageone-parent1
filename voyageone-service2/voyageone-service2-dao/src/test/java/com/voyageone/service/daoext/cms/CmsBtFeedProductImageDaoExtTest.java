package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.CmsBtFeedProductImageModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Ethan Shi on 2016/4/25.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")

public class CmsBtFeedProductImageDaoExtTest {

    @Autowired
    private CmsBtFeedProductImageDaoExt cmsBtFeedProductImageDao;

    @Test
    public void testSelectImagebyUrl() throws Exception {

        CmsBtFeedProductImageModel cmsBtFeedProductImageModel = new CmsBtFeedProductImageModel();

        cmsBtFeedProductImageModel.setId(1);
        cmsBtFeedProductImageModel.setChannelId("11");

        System.out.println(JacksonUtil.bean2Json(cmsBtFeedProductImageDao.selectImagebyUrl(cmsBtFeedProductImageModel)));


    }
}