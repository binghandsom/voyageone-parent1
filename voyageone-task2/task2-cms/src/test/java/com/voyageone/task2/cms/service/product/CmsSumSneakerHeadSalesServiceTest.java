package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.cms.service.product.sales.CmsImportOrdersHisInfoService;
import com.voyageone.task2.cms.service.product.sales.CmsSumSneakerHeadSalesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gjl on 2016/11/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsSumSneakerHeadSalesServiceTest {
    @Autowired
    CmsSumSneakerHeadSalesService cmsSumSneakerHeadSalesService;
    @Test
    public void testOnStartup() throws Exception {
        cmsSumSneakerHeadSalesService.onStartup(null);
    }
}
