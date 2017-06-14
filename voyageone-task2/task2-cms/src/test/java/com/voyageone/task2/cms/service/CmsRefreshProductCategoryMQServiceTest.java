package com.voyageone.task2.cms.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 批量重刷主类目MQ服务测试
 *
 * Created by desmond on 2016/12/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsRefreshProductCategoryMQServiceTest {
    @Test
    public void onStartup() throws Exception {

    }

    @Autowired
    private CmsRefreshProductCategoryMQService cmsRefreshProductCategoryMQService;

    @Test
    public void testOnStartup() throws Exception {
        // 指定code的翻译

        String channelId = "928";
        List<String> codeList = new ArrayList<>();
        codeList.add("1127642188");
        String userName = "testUser001";

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("channelId", channelId);
        messageMap.put("codeList", codeList);
        messageMap.put("userName", userName);

        cmsRefreshProductCategoryMQService.onStartup(messageMap);

        System.out.println("testOnStartup 测试正常结束!");
    }

    @Test
    public void testDoMain() throws Exception {
        // 单个产品code的主类目设置

        String channelId = "928";
        String code = "366650_111";
        String userName = "testUser003";

        // 调用批量设置主类目
        cmsRefreshProductCategoryMQService.doMain(channelId, code, userName);

        System.out.println("testDoMain 测试正常结束!");
    }

}