package com.voyageone.web2.cms.views.pop.price;

import com.voyageone.web2.cms.model.CmsBtPriceLogModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
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
        params.put("code", "1");
        params.put("sku", "1");
        List<CmsBtPriceLogModel> result = cmsPriceHistoryService.getPriceHistory(params);
        assert result.size() > 0;
    }
}
