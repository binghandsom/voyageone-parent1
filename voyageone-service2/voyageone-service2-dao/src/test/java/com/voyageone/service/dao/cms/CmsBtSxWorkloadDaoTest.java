package com.voyageone.service.dao.cms;

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
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsBtSxWorkloadDaoTest {

    @Autowired
    private CmsBtSxWorkloadDao cmsBtSxWorkloadDao;

    @Test
    public void testInsert() throws Exception {
        CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
        model.setChannelId("001");
        model.setGroupId(123L);
        model.setPublishStatus(0);
        model.setCreater("liang");
        model.setModifier("liang");
        cmsBtSxWorkloadDao.insertSxWorkloadModel(model);
    }

    @Test
    public void testSelect() throws Exception {
        List<CmsBtSxWorkloadModel> list = cmsBtSxWorkloadDao.selectSxWorkloadModel(10);
        for (CmsBtSxWorkloadModel model : list) {
            System.out.println(model.getChannelId() + "," + model.getGroupId() + "," + model.getPublishStatus());
        }
    }

}
