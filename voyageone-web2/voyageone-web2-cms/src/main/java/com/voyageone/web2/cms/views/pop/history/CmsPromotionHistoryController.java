package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.service.bean.cms.CmsBtPromotionHistoryBean;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.POP.PROMOTION.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionHistoryController extends CmsController {

    @Autowired
    private CmsPromotionHistoryService cmsPromotionHistoryService;

    @RequestMapping(CmsUrlConstants.POP.PROMOTION.GET_PROMOTION_HISTORY)
    public AjaxResponse getPromotionHistory(@RequestBody Map<String, Object> params) {
        Map<String, Object> result = cmsPromotionHistoryService.getPromotionList(params, getUser(), getLang());
        return success(result);
    }
    
    @RequestMapping(CmsUrlConstants.POP.PROMOTION.GET_UNDUE_PROMOTION)
    public AjaxResponse getUnduePromotion(@RequestBody Map<String, Object> params) {
    	Map<String, List<CmsBtPromotionHistoryBean>> promotions = cmsPromotionHistoryService.getUnduePromotion(params,
    			getUser(), getLang());
    	return success(promotions);
    }
}
