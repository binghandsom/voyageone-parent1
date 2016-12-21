package com.voyageone.components.sneakerhead.service;

import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.List;

/**
 * Created by gjl on 2016/11/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context.xml")
public class SnFeedServiceTest {
    @Autowired
    private SneakerheadApiService sneakerheadApiService;
    @Test
    public void testSneakerHeadResponse() throws Exception {
        SneakerHeadFeedInfoRequest sneakerHeadRequest = new SneakerHeadFeedInfoRequest();
        sneakerHeadRequest.setPageNumber(1);
        sneakerHeadRequest.setPageSize(100);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        sneakerHeadRequest.setTime(calendar.getTime());
        List<SneakerHeadCodeModel> sneakerHeadCodeModels = sneakerheadApiService.getFeedInfo(sneakerHeadRequest, SneakerHeadBase.DEFAULT_DOMAIN);
        System.out.print(sneakerHeadCodeModels.size());
    }
}