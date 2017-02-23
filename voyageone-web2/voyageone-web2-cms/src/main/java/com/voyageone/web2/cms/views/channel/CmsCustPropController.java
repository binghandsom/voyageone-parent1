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
public class CmsCustPropController extends CmsController {

    final
    CmsBtCustomPropService cmsBtCustomPropService;

    @Autowired
    public CmsCustPropController(CmsBtCustomPropService cmsBtCustomPropService) {
        this.cmsBtCustomPropService = cmsBtCustomPropService;
    }

    @RequestMapping(value = CmsUrlConstants.CHANNEL.CUSTOM.SEARCH)
    public AjaxResponse search(@RequestBody Map param){
        String orgChannelId = (String) param.get("orgChannelId");
        String cat = (String) param.get("cat");
        CmsBtCustomPropModel cmsBtCustomPropModel = cmsBtCustomPropService.getCustomPropByCatChannelExtend(getUser().getSelChannelId(),orgChannelId,cat);
        return success(cmsBtCustomPropModel);
    }
}
