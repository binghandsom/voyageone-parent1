package com.voyageone.web2.cms.views.jm;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.JMPROMOTION.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsJmPromotionIndexController extends CmsController {
    @Autowired
    private CmsBtJmPromotionService cmsPromotionService;
    @RequestMapping(CmsUrlConstants.PROMOTION.LIST.INDEX.INIT)
    public AjaxResponse init() {
        return success(cmsPromotionService.init());
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.GET_LIST_BY_WHERE)
    public AjaxResponse getListByWhere(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(cmsPromotionService.getListByWhere(params));
    }
}
