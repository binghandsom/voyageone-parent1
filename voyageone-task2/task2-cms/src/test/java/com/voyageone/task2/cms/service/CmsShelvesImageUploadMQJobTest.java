package com.voyageone.task2.cms.service;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsShelvesImageUploadMQMessageBody;
import com.voyageone.task2.cms.mqjob.CmsShelvesImageUploadMQJob;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
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

    @Test
    public void downImage(){
        String img = "http://s7d5.scene7.com/is/image/sneakerhead/sn%2D20170511%2D240x360?$sn-240x360$&$price=880&$product-name=Air+Jordan+Varsity+Pants+AJ%E7%94%B7%E5%AD%90%E8%BF%90%E5%8A%A8%E9%95%BF%E8%A3%A4+%E6%97%B6%E5%B0%9A%E6%94%B6%E8%84%9A%E8%A3%A4+%E6%9D%9F%E5%8F%A3%E8%A3%A4&$layer_1_src=sneakerhead%2Fsn%2Dbackground－70&$product=air-jordan-varsity-pants-653437063-1";
        img="http://s7d5.scene7.com/is/image/sneakerhead/sn-jd-20170511-240x342-1?$sn_240x342$&$img=adidas-stan-smith-retro-tennis-shoes-m20324-1&$name=%E9%98%BF%E8%BF%AA%E8%BE%BE%E6%96%AFAdidas+Stan+Smith+%E4%B8%89%E5%8F%B6%E8%8D%89%E7%94%B7%E5%AD%90%E4%BC%91%E9%97%B2%E6%9D%BF%E9%9E%8B+%E5%B0%8F%E7%BB%BF%E5%B0%BE&$price1=599&layer=4&originN=0,.5&pos=0,30";
        byte[]bytes =  cmsShelvesImageUploadMQJob.downImage(img);



        File a = new File("h:\\aaaa.png");
        try {
            FileUtils.writeByteArrayToFile(a, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}