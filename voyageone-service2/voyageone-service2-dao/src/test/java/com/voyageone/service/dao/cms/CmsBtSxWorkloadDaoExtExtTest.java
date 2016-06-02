package com.voyageone.service.dao.cms;

import com.voyageone.service.daoext.cms.CmsBtSxWorkloadDaoExt;
import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by DELL on 2016/1/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtSxWorkloadDaoExtExtTest {

    @Autowired
    private CmsBtSxWorkloadDaoExt cmsBtSxWorkloadDaoExt;

    @Test
    public void testInsert() throws Exception {
        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
        model.setChannelId("001");
        model.setGroupId(123L);
        model.setPublishStatus(0);
        model.setCreater("liang");
        model.setModifier("liang");
        cmsBtSxWorkloadDaoExt.insertSxWorkloadModel(model);
    }

    @Test
    public void testSelect() throws Exception {
        List<CmsBtSxWorkloadModel> list = cmsBtSxWorkloadDaoExt.selectSxWorkloadModel(10);
        for (CmsBtSxWorkloadModel model : list) {
            System.out.println(model.getChannelId() + "," + model.getGroupId() + "," + model.getPublishStatus());
        }
    }

}
