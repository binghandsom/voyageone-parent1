package com.voyageone.task2.cms.service.platform.uj;

import com.voyageone.service.model.cms.CmsBtSxWorkloadModel;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

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

        CmsBtSxWorkloadModel sxWorkLoadBean = new CmsBtSxWorkloadModel();
        sxWorkLoadBean.setChannelId("017");
        sxWorkLoadBean.setGroupId(138906L);
        sxWorkLoadBean.setModifier("james");

        uploadToUSJoiService.upload(sxWorkLoadBean);
    }

    @Test
    public void testOnStartup() throws Exception {

        uploadToUSJoiService.onStartup(new ArrayList<TaskControlBean>());
    }

    @Test
    public void testOnStartup1() throws Exception {

    }
}