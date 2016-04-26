package com.voyageone.service.daoext.cms;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by shiqin on 2016-04-21.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsMtChannelConfigDaoExtTest {

    @Autowired
    private CmsMtChannelConfigDaoExt cmsMtChannelConfigDaoExt;

    @Test
    public void testGetActionModelList() throws Exception {
        List<CmsMtChannelConfigModel> lst = cmsMtChannelConfigDaoExt.selectByConfigKey("010", "AUTO_APPROVE_PRICE");
        System.out.println(JacksonUtil.bean2Json(lst));
    }
}