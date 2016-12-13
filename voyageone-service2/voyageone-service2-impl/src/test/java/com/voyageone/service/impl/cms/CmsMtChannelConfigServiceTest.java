package com.voyageone.service.impl.cms;
import com.voyageone.service.bean.cms.mt.channel.config.CmsMtChannelConfigInfo;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class CmsMtChannelConfigServiceTest {

    @Autowired
    CmsMtChannelConfigService service;

     @Test
    public  void  searchTest()
    {
        Map<String,Object> map=new HashedMap();
        map.put("channelId","010");
       List<CmsMtChannelConfigInfo> list= service.search(map);
    }
}
