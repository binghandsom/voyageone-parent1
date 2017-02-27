package com.voyageone.web2.cms.views.channel;

import com.voyageone.service.bean.cms.CmsMtFeedConfigBean;
import com.voyageone.service.impl.cms.CmsBtCustomPropService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/2/23.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.ROOT, method = RequestMethod.POST)
public class CmsCustomPropController extends CmsController {

    final
    CmsBtCustomPropService cmsBtCustomPropService;

    @Autowired
    public CmsCustomPropController(CmsBtCustomPropService cmsBtCustomPropService) {
        this.cmsBtCustomPropService = cmsBtCustomPropService;
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SEARCH)
    public AjaxResponse search(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.getCustomPropByCatChannelExtend(getUser().getSelChannelId(),orgChannelId,cat);
        return success(cmsBtCustomPropModel);
    }

    // 设置打勾
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SET_CUSTOMSH_IS_DISPPLAY)
    public AjaxResponse doSetCustomshIsDispPlay(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity((Map) param.get("entity")) ;
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.setCustomshIsDispPlay(getUser().getSelChannelId(),orgChannelId,cat, entity);
        return success(cmsBtCustomPropModel);
    }

    // 更新一个entity的value
    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.UPDATE_ENTITY)
    public AjaxResponse doUpdateEntity(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel.Entity entity = new CmsBtCustomPropModel.Entity((Map) param.get("entity")) ;
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.updateEntity(getUser().getSelChannelId(),orgChannelId,cat, entity);
        return success(cmsBtCustomPropModel);
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SET_SORT)
    public AjaxResponse doSetSort(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        List<String> sort = (List<String>) param.get("sort");
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.setSort(getUser().getSelChannelId(),orgChannelId,cat, sort);
        return success(cmsBtCustomPropModel);
    }
}
