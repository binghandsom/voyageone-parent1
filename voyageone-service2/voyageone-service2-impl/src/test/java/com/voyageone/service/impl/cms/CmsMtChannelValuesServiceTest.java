package com.voyageone.service.impl.cms;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.model.cms.CmsMtChannelValuesModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author james.li on 2016/4/19.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsMtChannelValuesServiceTest {

    @Autowired
    CmsMtChannelValuesService cmsMtChannelValuesService;
    @Test
    public void testInsertCmsMtChannelValues() throws Exception {
        CmsMtChannelValuesModel cmsMtChannelValuesModel = new CmsMtChannelValuesModel();
        cmsMtChannelValuesModel.setChannelId("010");
        cmsMtChannelValuesModel.setType(0);
        cmsMtChannelValuesModel.setKey("1111");
        cmsMtChannelValuesModel.setValue("2222");
        cmsMtChannelValuesModel.setModifier("james");
        cmsMtChannelValuesModel.setCreater("james");
        cmsMtChannelValuesModel.setCreatedStr(DateTimeUtil.getNow());
        cmsMtChannelValuesService.insertCmsMtChannelValues(cmsMtChannelValuesModel);
    }
}