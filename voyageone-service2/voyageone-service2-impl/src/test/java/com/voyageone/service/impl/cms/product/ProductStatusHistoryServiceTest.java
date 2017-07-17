package com.voyageone.service.impl.cms.product;

import com.voyageone.common.PageQueryParameters;
import com.voyageone.service.bean.cms.product.EnumProductOperationType;
import com.voyageone.service.model.util.MapModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2016/7/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-context-service2.xml")
public class ProductStatusHistoryServiceTest {

    @Autowired
    ProductStatusHistoryService service;

    @Test
    public void testGetPage() {
        PageQueryParameters pageQueryParameters = new PageQueryParameters();
        pageQueryParameters.setPageIndex(1);
        pageQueryParameters.setPageRowCount(50);
        pageQueryParameters.put("channelId", "010");
        pageQueryParameters.put("cartId", "27");
        pageQueryParameters.put("code", "DIBRHCRST/RHGAR8.5");
        List<MapModel> list = service.getPage(pageQueryParameters);
        long count = service.getCount(pageQueryParameters);
    }

    @Test
    public void InsertList() {
        service.insertList("010", getCodeList("b"), "status", 27, EnumProductOperationType.Add, "测试", "system");
        testGetPage();
    }

    public List<String> getCodeList(String pre) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 301; i++) {
            list.add(pre + (1000 + i));
        }
        return list;
    }

    @Test
    public void testInsert() {
        service.insert("010", "123", "status", 27, EnumProductOperationType.Add, "测试", "system");
        testGetPage();
    }
}
