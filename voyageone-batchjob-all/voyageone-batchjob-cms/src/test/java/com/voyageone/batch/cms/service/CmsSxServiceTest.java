package com.voyageone.batch.cms.service;

import com.voyageone.batch.cms.bean.platform.SxWorkLoadBean;
import com.voyageone.batch.cms.service.platform.ali.AliSx;
import com.voyageone.batch.cms.service.platform.jm.JmSx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhujiaye on 15/12/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSxServiceTest {

    @Autowired
    AliSx aliSx;

    @Autowired
    JmSx jmSx;

    @Test
    public void testAliSx() throws Exception {
        SxWorkLoadBean sxWorkLoadBean = new SxWorkLoadBean();

        sxWorkLoadBean.setChannelId("010");
        sxWorkLoadBean.setGroupId(33588L);

        aliSx.doJob(sxWorkLoadBean);
    }

    @Test
    public void testJmSx() throws Exception {
        SxWorkLoadBean sxWorkLoadBean = new SxWorkLoadBean();

        sxWorkLoadBean.setChannelId("010");
        // TODO: 需要设置的其他参数, 比如主数据的prodId之类的

        jmSx.doJob(sxWorkLoadBean);
    }

}