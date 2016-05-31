package com.voyageone.service.daoext.cms;

import com.voyageone.service.bean.cms.CmsBtTasksBean;
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
 * Created by jonasvlag on 16/5/24.
 *
 * @version 2.1.0
 * @since 2.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class CmsBtTasksDaoExtTest {

    @Autowired
    private CmsBtTasksDaoExt tasksDaoExt;

    @Test
    public void select01ListWithPromotion() throws Exception {

        CmsBtTasksBean tasksBean = tasksDaoExt.selectWithPromotion(1);

        Assert.assertTrue(tasksBean.getTaskName().equals("库存隔离1"));
        Assert.assertTrue(tasksBean.getChannelId().equals("010"));

        List<CmsBtTasksBean> cmsBtTasksBeanList = tasksDaoExt.selectWithPromotion(1, "库存隔离1", "010", 2);

        Assert.assertTrue(cmsBtTasksBeanList.size() == 1);

        Map<String, Object> params = new HashMap<>();

        params.put("promotion_id", 1);

        List<CmsBtTasksBean> cmsBtTasksBeanList2 = tasksDaoExt.selectWithPromotion(params);

        Assert.assertTrue(cmsBtTasksBeanList2.size() == 1);
    }

    @Test
    public void update02Config() throws Exception {

        CmsBtTasksBean tasksBean = tasksDaoExt.selectWithPromotion(1);

        tasksBean.setConfig("{}");

        int count = tasksDaoExt.updateConfig(tasksBean);

        Assert.assertTrue(count == 1);
    }
}