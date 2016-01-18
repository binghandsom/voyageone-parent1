package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.sdk.api.domain.CmsBtPromotionModel;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.voyageone.web2.cms.CmsUrlConstants.PROMOTION;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james.li on 2015/12/11.
 */
@RestController
@RequestMapping(
        value = CmsUrlConstants.PROMOTION.LIST.ROOT,
        method = RequestMethod.POST
)
public class CmsPromotionListController extends CmsController {

    @Autowired
    private CmsPromotionService cmsPromotionService;

    @RequestMapping(PROMOTION.LIST.GET_PROMOTION_LIST)
    public AjaxResponse queryList(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(cmsPromotionService.queryByCondition(params));
    }

    @RequestMapping({PROMOTION.LIST.INSERT_PROMOTION,PROMOTION.LIST.UPDATE_PROMOTION})
    public AjaxResponse insertOrUpdate(@RequestBody CmsBtPromotionModel cmsBtPromotionModel) {
        String channelId = getUser().getSelChannelId();
        cmsBtPromotionModel.setChannelId(channelId);
        cmsBtPromotionModel.setCreater(getUser().getUserName());
        return success(cmsPromotionService.addOrUpdate(cmsBtPromotionModel));
    }

}
