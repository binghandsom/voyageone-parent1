package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.CmsMtChannelConfigService;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by rex.wu on 2016/11/7.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.MtChannelConfig.ROOT, method = {RequestMethod.POST})
public class CmsMTChannelConfigController extends CmsController {
    @Autowired
    private CmsMtChannelConfigService cmsMtChannelConfigService;

    @RequestMapping(value = CmsUrlConstants.CHANNEL.MtChannelConfig.Search)
    public AjaxResponse search(@RequestBody Map<String, Object> map) {
        UserSessionBean user = getUser();
        map.put("channelId", user.getSelChannelId());
        return success(cmsMtChannelConfigService.search(map));
    }

}
