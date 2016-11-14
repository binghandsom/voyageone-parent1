package com.voyageone.web2.cms.views.pop.bulkUpdate;

import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.InitParameter;
import com.voyageone.service.bean.cms.businessmodel.CmsAddProductToPromotion.AddProductSaveParameter;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author gubuchun 15/12/14
 * @version 2.0.0
 */
@RestController
@RequestMapping(
        value  = CmsUrlConstants.POP.AddProductToPromotion.ROOT,
        method = RequestMethod.POST
)
public class CmsAddProductToPromotionController extends CmsController {

    @Autowired
    private CmsAddProductToPromotionService cmsAddProductToPromotionService;
    @RequestMapping(CmsUrlConstants.POP.AddProductToPromotion.Save)
    public AjaxResponse save(@RequestBody AddProductSaveParameter params) {
        cmsAddProductToPromotionService.save(params, getUser().getSelChannelId(),getUser().getUserName(), getCmsSession());
        // TagTreeNode
        return success(true);
    }
    @RequestMapping(CmsUrlConstants.POP.AddProductToPromotion.Init)
    public AjaxResponse init(@RequestBody InitParameter params) {
        Map<String, Object> data = cmsAddProductToPromotionService.init(params, getUser().getSelChannelId(), getCmsSession());
        return success(data);
    }
}
