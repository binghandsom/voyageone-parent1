package com.voyageone.web2.cms.views.pop.tag.promotion;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.model.CmsBtTagModel;
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
        value  = CmsUrlConstants.POP.PROM_SELECT.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionSelectController extends CmsController {

    @Autowired
    private CmsPromotionSelectService promotionSelectService;

    @RequestMapping(CmsUrlConstants.POP.PROM_SELECT.GET_PROM_TAGS)
    public AjaxResponse getPromotionTags(@RequestBody Map<String, Object> params){
        List<CmsBtTagModel> result = promotionSelectService.getPromotionTags(params);
        return success(result);
    }

    @RequestMapping(CmsUrlConstants.POP.PROM_SELECT.ADD_TO_PROMOTION)
    public AjaxResponse addToPromotion(@RequestBody Map<String, Object> params) {
        String channel_id = getUser().getSelChannelId();
        String user_name = getUser().getUserName();
        Map<String, Object> result = promotionSelectService.addToPromotion(params, channel_id, user_name);
        return success(result);
    }

}
