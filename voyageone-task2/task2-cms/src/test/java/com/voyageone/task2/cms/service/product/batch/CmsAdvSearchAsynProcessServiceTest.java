package com.voyageone.task2.cms.service.product.batch;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by james on 2016/11/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsAdvSearchAsynProcessServiceTest {

    @Autowired
    CmsAdvSearchAsynProcessService cmsAdvSearchAsynProcessService;
    @Test
    public void onStartup() throws Exception {

        Map<String,Object> map = new HashedMap();
//        param={cartId=27, _option=refreshRetailPrice, productIds=[49896], isSelAll=0, cartIds=[27], _taskName=refreshRetailPrice, _channleId=017, _userName=james}
        map.put("_taskName","refreshRetailPrice");
        map.put("cartId",27);
        map.put("_option","refreshRetailPrice");
        map.put("productIds", Arrays.asList("53356"));
        map.put("isSelAll",0);
        map.put("cartIds",Arrays.asList(27));
        map.put("_channleId","017");
        map.put("_userName","james");

        cmsAdvSearchAsynProcessService.onStartup(map);
    }

}