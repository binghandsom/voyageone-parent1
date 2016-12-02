package com.voyageone.task2.cms.service.sneakerhead.uscategory;

import com.voyageone.task2.cms.BaseTest;
import com.voyageone.task2.cms.service.sneakerhead.CmsUsCategorySyncService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by vantis on 2016/11/29.
 * 闲舟江流夕照晚 =。=
 */
public class CmsUsCategorySyncServiceTest extends BaseTest {

    @Autowired
    private CmsUsCategorySyncService cmsUsCategorySyncService;
    @Test
    public void onStartup() throws Exception {
        cmsUsCategorySyncService.onStartup(new ArrayList<>());
    }

}