package com.voyageone.web2.cms.views.pop.promotion;

import com.voyageone.web2.cms.views.pop.history.CmsPromotionHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/context-web2.xml")
public class CmsPromotionHistoryServiceTest {

    @Autowired
    private CmsPromotionHistoryService cmsPromotionHistoryService;

    @Test
    public void testGetPriceHistory() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("productId", 15);
        params.put("channelId","010");
        params.put("offset",0);
        params.put("rows",10);
        Map<String, Object> result = cmsPromotionHistoryService.getPromotionList(params);
        assert result.size() > 0;
    }
}
