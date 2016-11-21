package com.voyageone.components.sneakerhead.service;

import com.voyageone.components.sneakerhead.bean.SneakerHeadCodeModel;
import com.voyageone.components.sneakerhead.bean.SneakerHeadRequest;
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
public class sneakerHeadFeedServiceTest {
    @Autowired
    private SneakerHeadFeedService sneakerHeadFeedService;
    @Test
    public void testSneakerHeadResponse() throws Exception {
        SneakerHeadRequest sneakerHeadRequest = new SneakerHeadRequest();
        sneakerHeadRequest.setPageNumber(1);
        sneakerHeadRequest.setPageSize(100);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        sneakerHeadRequest.setTime(calendar.getTime());
        List<SneakerHeadCodeModel> sneakerHeadCodeModels = sneakerHeadFeedService.sneakerHeadResponse(sneakerHeadRequest);
        System.out.print(sneakerHeadCodeModels.size());
    }
}