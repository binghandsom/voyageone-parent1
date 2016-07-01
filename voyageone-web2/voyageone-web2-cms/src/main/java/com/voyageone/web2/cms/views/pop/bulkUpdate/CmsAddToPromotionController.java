package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.service.model.cms.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value  = CmsUrlConstants.POP.ADD_TO_PROMOTION.ROOT,
        method = RequestMethod.POST
)
public class CmsAddToPromotionController extends CmsController {

    @Autowired
    private CmsAddToPromotionService promotionSelectService;

    @RequestMapping(CmsUrlConstants.POP.ADD_TO_PROMOTION.GET_PROM_TAGS)
    public AjaxResponse getPromotionTags(@RequestBody Map<String, Object> params){
        List<CmsBtTagModel> result = promotionSelectService.getPromotionTags(params);
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.POP.ADD_TO_PROMOTION.ADD_TO_PROMOTION)
    public AjaxResponse addToPromotion(@RequestBody Map<String, Object> params) {
        promotionSelectService.addToPromotion(params, getUser(), getCmsSession());
        return success(true);
    }

    /**
     * 检查所选择的产品是否已存在同级别的promotion tag
     * @param params
     * @return AjaxResponse
     */
    @RequestMapping(CmsUrlConstants.POP.ADD_TO_PROMOTION.CHECK_PROM_TAGS)
    public AjaxResponse checkPromotionTags(@RequestBody Map<String, Object> params){
        return success(promotionSelectService.checkPromotionTags(getUser().getSelChannelId(), params));
    }
}
