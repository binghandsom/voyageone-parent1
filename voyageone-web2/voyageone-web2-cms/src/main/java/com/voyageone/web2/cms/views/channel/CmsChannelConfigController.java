package com.voyageone.web2.cms.views.channel;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.impl.cms.CmsMtChannelConfigService;
import com.voyageone.service.model.cms.CmsMtChannelConfigModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by rex.wu on 2016/11/7.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.ROOT, method = {RequestMethod.POST})
public class CmsChannelConfigController extends CmsController {

    @Autowired
    private CmsMtChannelConfigService cmsMtChannelConfigService;

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.INIT)
    public AjaxResponse init(){
        UserSessionBean user = getUser();
        return success(cmsMtChannelConfigService.init(user.getSelChannelId(), user.getUserName()));
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.LOAD_BY_CHANNEL)
    public AjaxResponse loadByChannel(@RequestBody Map<String, String> params) {
        String channelId = params.get("channelId");
        return success(cmsMtChannelConfigService.loadByChannel(channelId));
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.ADD_CHANNEL_CONFIG)
    public AjaxResponse addChannelConfig(@RequestBody CmsMtChannelConfigModel channelConfigModel){
        channelConfigModel.setCreater(getUser().getUserName());
        cmsMtChannelConfigService.insert(channelConfigModel);
        return success(channelConfigModel.getChannelId());
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.LOAD_CHANNEL_CONFIG_DETAIL)
    public AjaxResponse loadChannelConfigDetail(@RequestBody Map<String, String> params){
        String channelConfigIdVal = params.get("channelConfigId");
        if (StringUtils.isNotBlank(channelConfigIdVal)) {
            Integer id = Integer.parseInt(channelConfigIdVal);
            return success(cmsMtChannelConfigService.selectById(id));
        }else {
            throw new BusinessException("请先选择要修改的店铺配置记录！");
        }
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.EDIT_CHANNEL_CONFIG)
    public AjaxResponse editChannelConfig(@RequestBody CmsMtChannelConfigModel channelConfigModel){
        channelConfigModel.setModifier(getUser().getUserName());
        cmsMtChannelConfigService.edit(channelConfigModel);
        return success(channelConfigModel.getChannelId());
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CONFIG.DEL_CHANNEL_CONFIG)
    public AjaxResponse delChannelConfig(@RequestBody Map<String, String> params) {
        String channelConfigId = params.get("channelConfigId");
        cmsMtChannelConfigService.delete(channelConfigId);
        return success(null);
    }

}
