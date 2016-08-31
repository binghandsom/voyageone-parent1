package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @description cms_bt_business_log表查询，更新服务的测试程序
 * @author: desmond
 * @date: 2016/8/22
 * COPYRIGHT © 2001 - 2016 VOYAGE ONE GROUP INC. ALL RIGHTS RESERVED.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BusinessLogServiceTest {


    @Autowired
    BusinessLogService businessLogService;

    @Test
    public void testUpdateFinishStatusByCondition() throws Exception {
        String channelId = "010";
        Integer cartId = 27;
//        String groupId = "27214";
        String groupId = null;
        String code = "00341";
        String modifier = "desmond";

        int updateCnt = businessLogService.updateFinishStatusByCondition(channelId, cartId, groupId,
                null, code, modifier);
        System.out.println("effect count = " + updateCnt);
    }

    @Test
    public void testUpdateFinishStatusByCondition2() throws Exception {
        String channelId = "010";
        Integer cartId = 27;
        String groupId = null;
        String code = null;
        String modifier = "desmond";

        int updateCnt = businessLogService.updateFinishStatusByCondition(channelId, cartId, groupId,
                null, code, modifier);
        System.out.println("effect count = " + updateCnt);
    }

    @Test
    public void testUpdateFinishStatusByCondition3() throws Exception {
        String channelId = "010";
        Integer cartId = null;
        String groupId = null;
        String code = null;
        String modifier = "desmond";

        int updateCnt = businessLogService.updateFinishStatusByCondition(channelId, cartId, groupId,
                null, code, modifier);
        System.out.println("effect count = " + updateCnt);
    }
}
