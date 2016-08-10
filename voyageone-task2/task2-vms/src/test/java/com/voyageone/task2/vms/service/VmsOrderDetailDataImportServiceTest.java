package com.voyageone.task2.vms.service;

import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeff.duan on 16/06/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-vms-test.xml")
public class VmsOrderDetailDataImportServiceTest {

    @Autowired
    private VmsOrderDetailDataImportService vmsOrderDetailDataImportService;
    @Test
    public void testOnStartup() throws Exception {
        vmsOrderDetailDataImportService.main("088", "ORDER");
//        vmsOrderDetailDataImportService.main("091", "SKU");
    }
}