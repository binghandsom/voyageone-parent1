package com.voyageone.web2.cms.views.channel;

import com.voyageone.service.bean.cms.mt.channel.config.SaveListInfo;
import com.voyageone.service.impl.cms.CmsFeedConfigService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by gjl on 2016/12/20.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.ROOT, method = {RequestMethod.POST})
public class CmsFeedConfigController extends CmsController {
    @Autowired
    private CmsFeedConfigService cmsFeedConfigService;

    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SEARCH)
    public AjaxResponse search(@RequestBody Map<String, Object> map) {
        UserSessionBean user = getUser();
        return success(cmsFeedConfigService.search(map,user.getSelChannelId(),this.getLang()));
    }
    @RequestMapping(value = CmsUrlConstants.CHANNEL.FEED_CONFIG.SAVE)
    public AjaxResponse saveList(@RequestBody SaveListInfo saveInfo) {
        UserSessionBean user = getUser();
        cmsFeedConfigService.saveList(saveInfo, user.getSelChannelId(), user.getUserName());
        return success(null);
    }
}


