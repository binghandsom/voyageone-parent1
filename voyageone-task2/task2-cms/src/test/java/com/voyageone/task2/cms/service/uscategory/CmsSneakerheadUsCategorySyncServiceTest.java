package com.voyageone.task2.cms.service.uscategory;

import com.voyageone.task2.cms.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by vantis on 2016/11/29.
 * 闲舟江流夕照晚 =。=
 */
public class CmsSneakerheadUsCategorySyncServiceTest extends BaseTest {

    @Autowired
    private CmsSneakerheadUsCategorySyncService cmsSneakerheadUsCategorySyncService;
    @Test
    public void onStartup() throws Exception {
        cmsSneakerheadUsCategorySyncService.onStartup(new ArrayList<>());
    }

}