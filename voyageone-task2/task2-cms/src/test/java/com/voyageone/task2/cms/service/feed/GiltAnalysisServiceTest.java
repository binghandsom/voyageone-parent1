package com.voyageone.task2.cms.service.feed;

import com.voyageone.components.gilt.bean.GiltSale;
import com.voyageone.components.gilt.service.GiltSalesService;
import com.voyageone.components.gilt.service.GiltSkuService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jonas, 2/3/16.
 * @version 2.0.0
 * @since 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class GiltAnalysisServiceTest {

    @Autowired
    private GiltAnalysisService giltAnalysisService;

    @Autowired
    private GiltSalesService giltSalesService;

    @Test
    public void testOnStartup() throws Exception {
        List<GiltSale> sale = giltSalesService.getAllSales();

        giltAnalysisService.onStartup(new ArrayList<TaskControlBean>());
    }
}