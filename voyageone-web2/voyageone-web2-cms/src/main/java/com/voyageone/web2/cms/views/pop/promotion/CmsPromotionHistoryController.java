package com.voyageone.web2.cms.views.pop.promotion;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
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
    public AjaxResponse getPriceHistory(@RequestBody Map<String, Object> params) {
        List<CmsBtPromotionCodeModel> result = cmsPromotionHistoryService.getPromotionList(params);
        return success(result);
    }
}
