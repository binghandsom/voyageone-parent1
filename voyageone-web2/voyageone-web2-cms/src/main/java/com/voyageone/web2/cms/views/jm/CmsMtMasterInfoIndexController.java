package com.voyageone.web2.cms.views.jm;
import com.voyageone.service.impl.jumei.CmsMtMasterInfoService;
import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping(
        value = CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.ROOT,
        method = RequestMethod.POST
)
public class CmsMtMasterInfoIndexController extends CmsController {
    @Autowired
    private CmsMtMasterInfoService service;

    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.GET_LIST_BY_WHERE)
    public AjaxResponse getListByWhere(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        Map<String, Object> result = new HashMap<>();
        params.put("active", 1);
        result.put("masterInfoList", service.getListByWhere(params));
        result.put("masterInfoListTotal", service.getCountByWhere(params));
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.GetCountByWhere)
    public AjaxResponse getCountByWhere(@RequestBody Map params) {
        String channelId = getUser().getSelChannelId();
        params.put("channelId", channelId);
        return success(service.getCountByWhere(params));
    }
    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.INSERT)
    public AjaxResponse insert(@RequestBody CmsMtMasterInfoModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        params.setCreater(getUser().getUserName());
        params.setCreated(new Date());
        params.setActive(true);
        return success(service.insert(params));
    }

    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.UPDATE)
    public AjaxResponse update(@RequestBody CmsMtMasterInfoModel params) {
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        return success(service.update(params));
    }
    @RequestMapping(CmsUrlConstants.JMPROMOTION.LIST.INDEX.DELETE)
    public AjaxResponse delete(@RequestBody CmsMtMasterInfoModel params) {
        CmsMtMasterInfoModel result = service.select(params.getId());
        result.setModifier(getUser().getUserName());
        result.setActive(false);
        return success(service.update(result));
    }
    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.GET)
    public Object get(@RequestBody int id) {//@RequestParam("id")
        return success(service.select(id));
    }

    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.UPDATEJMIMG)
    public Object updateJMImg(@RequestBody CmsMtMasterInfoModel params) {
        // 先更新一次再刷新图片
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        service.update(params);

        Map<String, Object> map = new HashMap<>();
        map.put("result", true);
        return success(map);
    }
}
