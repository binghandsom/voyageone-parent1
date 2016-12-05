package com.voyageone.components.sneakerhead.service;

import com.voyageone.BaseTest;
import com.voyageone.components.sneakerhead.SneakerHeadBase;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
public class SneakerheadApiServiceTest extends BaseTest {
    @Autowired
    SneakerheadApiService sneakerheadApiService;

    @Test
    public void getFeedCount() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        $debug(String.valueOf("getFeedCount works well: " +
                sneakerheadApiService.getFeedCount(calendar.getTime(), "127.0.0.1:52233")));
    }

    @Test
    public void getFeedInfo() throws Exception {
        SneakerHeadFeedInfoRequest sneakerHeadFeedInfoRequest = new SneakerHeadFeedInfoRequest();
        sneakerHeadFeedInfoRequest.setPageNumber(1);
        sneakerHeadFeedInfoRequest.setPageSize(100);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.FEBRUARY, 1);
        sneakerHeadFeedInfoRequest.setTime(calendar.getTime());
        $debug(String.valueOf("getFeedInfo works well: " +
                sneakerheadApiService.getFeedInfo(sneakerHeadFeedInfoRequest, "10.0.0.91:52233")));
    }

    @Test
    public void getCategory() throws Exception {
        $debug("getCategory works well: " + sneakerheadApiService.getCategory(true, "10.0.0.91:52233").toString());
    }

}