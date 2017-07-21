package com.voyageone.task2.cms.service.sneakerhead;

import com.voyageone.task2.cms.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by dell on 2017/7/12.
 */
public class CmsSynFeedQtyServiceTest extends BaseTest {

    @Autowired
    CmsSynFeedQtyService cmsSynFeedQtyService;
    @Test
    public void testOnStartup() throws Exception {
        cmsSynFeedQtyService.onStartup(new ArrayList<>());
    }

    @Test
    public void testGetTaskName() throws Exception {

    }

    @Test
    public void testGetSubSystem() throws Exception {

    }
}