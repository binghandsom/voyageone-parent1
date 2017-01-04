package com.voyageone.task2.cms.service.product;

import com.voyageone.task2.cms.service.product.sales.CmsCopyOrdersInfoService;
import com.voyageone.task2.cms.service.product.sales.CmsImportOrdersHisInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jason.jiang on 2016/05/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:context-cms-test.xml")
public class CmsImportOrdersHisInfoServiceTest {

    @Autowired
    CmsImportOrdersHisInfoService cmsImportOrdersHisInfoService;

    @Autowired
    CmsCopyOrdersInfoService cmsCopyOrdersInfoService;
    @Test
    public void testOnStartup() {
        // 保存操作过程，用于邮件通知
        Map<String, Object> statusMap = new ConcurrentHashMap<>();

        cmsCopyOrdersInfoService.copyOrdersInfo("CmsImportOrdersHisInfoJob", statusMap);
    }
}