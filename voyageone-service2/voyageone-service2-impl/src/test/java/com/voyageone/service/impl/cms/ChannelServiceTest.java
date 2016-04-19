package com.voyageone.service.impl.cms;

import com.voyageone.BaseTest;
import com.voyageone.common.configs.beans.OrderChannelBean;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 * @author: holysky
 * @date: 2016/4/13 10:47
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
public class ChannelServiceTest extends BaseTest {


    @Resource ChannelService service;

    @Test
    public void testGetChannelList() throws Exception {
        List result = service.getChannelListBy(null, null, null,null);
        Assert.assertTrue(result.size()>0);

        List<OrderChannelBean> result1 = service.getChannelListBy("001", null, null,null);
        Assert.assertTrue(result1.get(0).getOrder_channel_id().equalsIgnoreCase("001"));
        List<OrderChannelBean> result2 = service.getChannelListBy("001", "SN", null,null);
        Assert.assertTrue(result2.get(0).getOrder_channel_id().equals("001") && result2.get(0).getName().equalsIgnoreCase("SN"));


        List<OrderChannelBean> result3 = service.getChannelListBy("001", "SN", -1,null);

        Assert.assertTrue(result3.size()>0);

        List<OrderChannelBean> result4 = service.getChannelListBy("001", "SN", 0,null);
        Assert.assertTrue(result4.size()>0);
        List<OrderChannelBean> result5 = service.getChannelListBy("001", "SN", 1,null);
        Assert.assertTrue(result5.size()==0);


    }
}
