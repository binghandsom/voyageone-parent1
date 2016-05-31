package com.voyageone.web2.cms.views.channel;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.bean.cms.CmsBtTagBean;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jonas, 12/10/15
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsChannelTagServiceTest {

    @Autowired
    private CmsChannelTagService targetService;

    @Test
    public void testGetTagInfoByChannelId1() throws Exception {
        Map param = new HashMap<>(2);
        param.put("channel_id", "010");
        param.put("tagTypeSelectValue", "1");
        List<CmsBtTagBean> resultInfo = targetService.getTagInfoByChannelId(param);

        Assert.assertNotNull(resultInfo);
        System.out.println("prettyJsonStr: " + JacksonUtil.bean2Json(resultInfo));
    }

    @Test
    public void testGetTagInfoList1() throws Exception {
        Map param = new HashMap<>(2);
        param.put("channel_id", "010");
        param.put("tagTypeSelectValue", "1");
        List<CmsBtTagModel> resultInfo = targetService.getTagInfoList(param);
        Assert.assertNotNull(resultInfo);
        System.out.println("prettyJsonStr: " + JacksonUtil.bean2Json(resultInfo));
    }
}