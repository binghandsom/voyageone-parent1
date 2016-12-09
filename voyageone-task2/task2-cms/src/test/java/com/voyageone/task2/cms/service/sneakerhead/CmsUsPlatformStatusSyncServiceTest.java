package com.voyageone.task2.cms.service.sneakerhead;

import com.voyageone.task2.cms.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by vantis on 2016/12/2.
 * 闲舟江流夕照晚 =。=
 */
public class CmsUsPlatformStatusSyncServiceTest extends BaseTest {

    @Autowired
    CmsUsPlatformStatusSyncService cmsUsPlatformStatusSyncService;
    @Test
    public void getTaskName() throws Exception {
        $debug(cmsUsPlatformStatusSyncService.getTaskName());
    }

    @Test
    public void getSubSystem() throws Exception {
        $debug(cmsUsPlatformStatusSyncService.getSubSystem().name());
    }

    @Test
    public void onStartup() throws Exception {
        cmsUsPlatformStatusSyncService.onStartup(new ArrayList<>());
    }

}