package com.voyageone.common.configs.dao;

import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.dao.CmsChannelConfigDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by lewis on 15-12-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CmsChannelConfigDaoTest {

    @Autowired
    private CmsChannelConfigDao cmsChannelConfigDao;

    @Test
    public void testGetALl() throws Exception {
        List<CmsChannelConfigBean> cmsMtChannelConfigModels = cmsChannelConfigDao.selectALl();
        for (CmsChannelConfigBean cmsMtChannelConfigModel : cmsMtChannelConfigModels) {
            System.out.println(cmsMtChannelConfigModel.getConfigValue1());
        }

    }
}