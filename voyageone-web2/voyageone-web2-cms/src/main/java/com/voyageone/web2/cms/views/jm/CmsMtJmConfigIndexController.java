package com.voyageone.web2.cms.views.jm;

import com.voyageone.service.impl.cms.jumei.CmsBtJmMasterPlatService;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.model.jumei.CmsMtJmConfigModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsConstants;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.CMSMTJMCONFIG.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsMtJmConfigIndexController extends CmsController {
    @Autowired
    private CmsMtJmConfigService service;
    @Autowired
    private CmsBtJmMasterPlatService masterPlatService;

    @RequestMapping(CmsUrlConstants.CMSMTJMCONFIG.LIST.INDEX.INIT)
    public AjaxResponse init() {

        Map<String, Object> masterData = new HashMap<>();

        masterData.put("jmShippingStockList", masterPlatService.selectListByCode(CmsConstants.jmMasterPlatCode.STOCK));

        return success(masterData);
    }

    @RequestMapping(CmsUrlConstants.CMSMTJMCONFIG.LIST.INDEX.INSERT)
    public AjaxResponse insert(@RequestBody CmsMtJmConfigModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        params.setCreater(getUser().getUserName());
        params.setCreated(new Date());
        return success(service.insert(params));
    }

    @RequestMapping(CmsUrlConstants.CMSMTJMCONFIG.LIST.INDEX.UPDATE)
    public AjaxResponse update(@RequestBody CmsMtJmConfigModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        return success(service.update(params));
    }

    @RequestMapping(CmsUrlConstants.CMSMTJMCONFIG.LIST.INDEX.GETBYKEY)
    public Object getByKey(@RequestBody CmsMtJmConfigModel params) {//@RequestParam("id")
        String channelId = getUser().getSelChannelId();
        return success(service.getByKey(channelId, params.getKey()));
    }


}
