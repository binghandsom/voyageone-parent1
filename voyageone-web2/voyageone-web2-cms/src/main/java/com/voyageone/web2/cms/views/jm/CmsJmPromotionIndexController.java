package com.voyageone.web2.cms.views.jm;
import com.voyageone.service.impl.jumei.CmsBtJmPromotionService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.cms.views.promotion.list.CmsPromotionIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.INSERT)
    public AjaxResponse insert(@RequestBody CmsBtJmPromotionModel params) {
        String channelId = getUser().getSelChannelId();
       // params.put("channelId", channelId);
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        params.setCreater(getUser().getUserName());
        params.setCreated(new Date());
        return success(cmsPromotionService.insert(params));
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.UPDATE)
    public AjaxResponse update(@RequestBody CmsBtJmPromotionModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        return success(cmsPromotionService.update(params));
    }
}
