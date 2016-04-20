package com.voyageone.web2.cms.views.jm;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.impl.cms.jumei.CmsBtJmMasterBrandService;
import com.voyageone.service.impl.cms.jumei.CmsMtMasterInfoService;
import com.voyageone.service.impl.cms.jumei.platefrom.JuMeiUploadImageService;
import com.voyageone.service.model.jumei.CmsMtMasterInfoModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private JuMeiUploadImageService serviceJuMeiUploadImage;
    @Autowired
    CmsBtJmMasterBrandService serviceCmsBtJmMasterBrand;
    private static final Logger LOG = LoggerFactory.getLogger(CmsMtMasterInfoIndexController.class);
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
    public Object get(@RequestBody CmsMtMasterInfoModel params) {//@RequestParam("id")
        return success(service.select(params.getId()));
    }

    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.UPDATEJMIMG)
    public Object updateJMImg(@RequestBody CmsMtMasterInfoModel params) {
        // 先更新一次再刷新图片
        CallResult result = new CallResult();
        String channelId = getUser().getSelChannelId();
        params.setChannelId(channelId);
        params.setModifier(getUser().getUserName());
        service.update(params);
        try {
            serviceJuMeiUploadImage.uploadImage(params);
        } catch (Exception ex) {
            params.setErrorMessage(ExceptionUtil.getErrorMsg(ex));
            params.setSynFlg(3);
            service.update(params);
            result.setMsg("上传图片失败!");
            result.setResult(false);
        }
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CMSMTMASTERINFO.LIST.INDEX.LoadJmMasterBrand)
    public AjaxResponse  loadJmMasterBrand() {
        CallResult result = new CallResult();
        try {
            String userName = getUser().getUserName();
            serviceCmsBtJmMasterBrand.loadJmMasterBrand(userName);
        } catch (Exception ex) {
            LOG.error("loadJmMasterBrand", ex);
            result.setMsg("聚美同步失败");
            result.setResult(false);
        }
        return success(result);
    }
}
