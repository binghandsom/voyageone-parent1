package com.voyageone.components.sneakerhead.service;

import com.voyageone.BaseTest;
import com.voyageone.components.sneakerhead.bean.SneakerHeadFeedInfoRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by vantis on 2016/11/28.
 * 闲舟江流夕照晚 =。=
 */
public class SneakerHeadFeedServiceTest extends BaseTest {
    @Autowired
    SneakerHeadFeedService sneakerHeadFeedService;

    @Test
    public void getFeedInfo() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        $debug(String.valueOf(sneakerHeadFeedService.getFeedCount(calendar.getTime())));
    }

    @Test
    public void getFeedCount() throws Exception {
        SneakerHeadFeedInfoRequest sneakerHeadFeedInfoRequest = new SneakerHeadFeedInfoRequest();
        sneakerHeadFeedInfoRequest.setPageNumber(1);
        sneakerHeadFeedInfoRequest.setPageSize(100);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 1, 1);
        sneakerHeadFeedInfoRequest.setTime(calendar.getTime());
        $debug(String.valueOf(sneakerHeadFeedService.getFeedInfo(sneakerHeadFeedInfoRequest)));
    }

    @Test
    public void getCategory() throws Exception {
        $debug(sneakerHeadFeedService.getCategory(true).toString());
    }

}