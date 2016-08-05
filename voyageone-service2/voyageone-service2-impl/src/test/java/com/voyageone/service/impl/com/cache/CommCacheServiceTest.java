package com.voyageone.service.impl.com.cache;

import com.voyageone.service.impl.cms.sx.SxProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CommCacheServiceTest {

    @Autowired
    private CommCacheService commCacheService;

    @Test
    public void testGet() throws Exception {
        List<String> codeList = commCacheService.getCache("CommCacheServiceTest", "cacheKey1");
        if (codeList == null) {
            System.out.println("OK");
        }
    }

    @Test
    public void testGet1() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        commCacheService.setCache("CommCacheServiceTest", "cacheKey1", codeList);
        codeList = commCacheService.getCache("CommCacheServiceTest", "cacheKey1");
        if (codeList == null) {
            System.out.println("NG");
        } else {
            System.out.println(codeList);
        }
    }

    @Test
    public void testGet3() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        commCacheService.setCache("CommCacheServiceTest", "cacheKey1", codeList);
        commCacheService.deleteCache("CommCacheServiceTest", "cacheKey1");
        codeList = commCacheService.getCache("key1", "cacheKey1");
        if (codeList == null) {
            System.out.println("OK");
        } else {
            System.out.println(codeList);
        }
    }

    @Test
    public void testGet4() throws Exception {
        List<String> codeList = new ArrayList<>();
        codeList.add("16238170-HELLOKITTYCLASSICDOT");
        commCacheService.setCache("CommCacheServiceTest", "cacheKey1", codeList);
        commCacheService.deleteAllCache();
        codeList = commCacheService.getCache("CommCacheServiceTest", "cacheKey1");
        codeList = commCacheService.getCache("CommCacheServiceTest", "cacheKey1");
        if (codeList == null) {
            System.out.println("OK");
        } else {
            System.out.println(codeList);
        }
    }
}