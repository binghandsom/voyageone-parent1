package com.voyageone.web2.cms.views.pop.price;

import com.voyageone.web2.cms.views.pop.history.CmsPriceHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPriceHistoryServiceTest {

    @Autowired
    private CmsPriceHistoryService cmsPriceHistoryService;

    @Test
    public void testGetPriceHistory() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("flag", true);
        params.put("code", "0");
        params.put("sku", "1");
        params.put("offset", 0);
        params.put("rows", 30);
        params.put("priceType", "msrp");
//        Map<String, Object> result = cmsPriceHistoryService.getPriceHistory(params, "010", "en");
//        assert result.size() > 0;
    }
}
