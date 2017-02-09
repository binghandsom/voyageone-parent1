package com.voyageone.web2.cms.views.biReport.consult;

import com.voyageone.common.util.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Ethan Shi on 2017-02-09.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")

public class BiRepConsultServiceTest {

    @Autowired
    BiRepConsultService biRepConsultService;

    @Test
    public void testDealTheFileCreateRequest() throws Exception {
        HashMap<String, Object> request = new HashMap<>();
        request.put("creatorId", 222);
        request.put("createTime", new Date());
        request.put("fileTypes", new ArrayList<Integer>() { {add(1);}});
        request.put("staDate", "2016-12-01");
        request.put("endDate",  "2016-12-02");
        request.put("channelCode",  "010");

        Map response = biRepConsultService.dealTheFileCreateRequest(request);

        System.out.println(JacksonUtil.bean2Json(response));

    }
}