package com.voyageone.web2.cms.views.pop.promotion;

import com.voyageone.web2.cms.model.CmsBtPriceLogModel;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
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
        params.put("promotionId", 15);
        List<CmsBtPromotionCodeModel> result = cmsPromotionHistoryService.getPromotionList(params);
        assert result.size() > 0;
    }
}
