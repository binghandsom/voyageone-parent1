package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.task2.cms.bean.platform.SxWorkLoadBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author james.li on 2016/4/7.
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class UploadToUSJoiServiceTest {

    @Autowired
    UploadToUSJoiService uploadToUSJoiService;
    @Test
    public void testUpload() throws Exception {

        SxWorkLoadBean sxWorkLoadBean = new SxWorkLoadBean();
        sxWorkLoadBean.setChannelId("010");
        sxWorkLoadBean.setGroupId(84618L);
        sxWorkLoadBean.setModifier("james");

        uploadToUSJoiService.upload(sxWorkLoadBean);
    }
}