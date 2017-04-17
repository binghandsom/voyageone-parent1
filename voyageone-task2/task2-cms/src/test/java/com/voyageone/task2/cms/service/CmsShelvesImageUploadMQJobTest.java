package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesImageUploadMQMessageBody;
import com.voyageone.task2.cms.mqjob.CmsShelvesImageUploadMQJob;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by james on 2016/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsShelvesImageUploadMQJobTest {

    @Autowired
    CmsShelvesImageUploadMQJob cmsShelvesImageUploadMQJob;
    @Test
    public void onStartup() throws Exception {
        CmsShelvesImageUploadMQMessageBody map = new CmsShelvesImageUploadMQMessageBody();
        map.setShelvesId(29);
        cmsShelvesImageUploadMQJob.onStartup(map);
    }
    @Test
    public void aaa(){
        byte[] aa = cmsShelvesImageUploadMQJob.downImage("http://s7d5.scene7.com/is/image/sneakerhead/20161026-240x342-jiarugouwuche?$sn_240x342$&$yuanjia=1217&$shiyijia=1217&$product=010-H36899-1&$name=Jewelry.com+925%E9%93%B614K%E9%87%91%E9%95%B60.33%E5%85%8B%E6%8B%89%E9%92%BB%E7%9F%B3%E5%8F%8C%E5%BF%83%E5%A5%B3%E6%AC%BE%E6%97%B6%E5%B0%9A%E5%BF%83%E7%9B%B8%E5%8D%B0%E6%89%8B%E9%93%BE");
        Map<String,Object> map = new HashedMap();
        map.put("shelvesId",1);
    }
}