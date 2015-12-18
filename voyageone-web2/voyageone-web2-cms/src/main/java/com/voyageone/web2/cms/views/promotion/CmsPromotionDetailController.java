package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = PROMOTION.DETAIL.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionDetailController extends CmsController {

    @Autowired
    private CmsPromotionDetailService cmsPromotionDetailService;

    @RequestMapping(PROMOTION.DETAIL.GET_PROMOTION_GROUP)
    public AjaxResponse getPromotionGroup(@RequestBody Map params) {

        List<Map<String,Object>> resultBean = cmsPromotionDetailService.getPromotionGroup(params);

        // 返回用户信息
        return success(resultBean);
    }


}
