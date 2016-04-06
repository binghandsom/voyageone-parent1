package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.impl.jumei.CmsBtJmPromotionProductService;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionDetailController extends CmsController {
    @Autowired
    private CmsBtJmPromotionProductService serviceCmsBtJmPromotionProduct;
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.DETAIL.GET_PROMOTION_PRODUCT_INFO_LIST_BY_WHERE)
    public AjaxResponse getPromotionProductInfoListByWhere(@RequestBody Map params) {

        return success(serviceCmsBtJmPromotionProduct.getPromotionProductInfoListByWhere(params));
    }
}
