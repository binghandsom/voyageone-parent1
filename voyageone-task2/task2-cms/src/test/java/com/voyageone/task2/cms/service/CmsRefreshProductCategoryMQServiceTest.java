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

    @Autowired
    private CmsRefreshProductCategoryMQService cmsRefreshProductCategoryMQService;

    @Test
    public void testOnStartup() throws Exception {
        // 指定code的翻译

        String channelId = "928";
        List<String> codeList = new ArrayList<>();
        codeList.add("022-EA3060501754");
        codeList.add("022-EA3060538652");
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
        // 多个产品code的同购翻译,并批量回写数据库

        String channelId = "928";
        String code = "022-EA3060538852";
        String userName = "testUser002";

        // 调用批量设置主类目
        cmsRefreshProductCategoryMQService.doMain(channelId, code, userName);

        System.out.println("testDoMain 测试正常结束!");
    }

}