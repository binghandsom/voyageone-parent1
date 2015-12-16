package com.voyageone.web2.cms.views.pop.prom_select;

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
        value  = CmsUrlConstants.PROM.SELECT.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionSelectController extends CmsController {

    @Autowired
    private CmsPromotionSelectService promotionSelectService;

    @RequestMapping(CmsUrlConstants.PROM.SELECT.GET_PROM_TAGS)
    public AjaxResponse getPromotionTags(@RequestBody Map<String, Object> params){
        List<CmsBtTagModel> result = promotionSelectService.getPromotionTags(params);
        return success(result);
    }

}
