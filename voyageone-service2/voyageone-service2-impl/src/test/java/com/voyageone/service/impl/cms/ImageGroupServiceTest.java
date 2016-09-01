package com.voyageone.service.impl.cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ImageGroupServiceTest {
    @Autowired
    ImageGroupService service;
    @Test
    public  void  testGetNoMatchSizeImageGroupList()
    {
        List<Map<String,Object>> list= service.getNoMatchSizeImageGroupList("010");
    }
}
