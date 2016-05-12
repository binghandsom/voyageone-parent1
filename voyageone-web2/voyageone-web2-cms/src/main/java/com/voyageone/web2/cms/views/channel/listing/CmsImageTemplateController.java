package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.service.bean.cms.CmsBtImageTemplateBean;
import com.voyageone.service.bean.cms.CmsBtImageTemplateBean;
import com.voyageone.service.bean.cms.imagetemplate.GetDownloadUrlParamter;
import com.voyageone.service.impl.cms.CmsImageTemplateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
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

@RestController
@RequestMapping(
        value = CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.ROOT,
        method = RequestMethod.POST
)
public class CmsImageTemplateController extends CmsController {
    @Autowired
    private CmsImageTemplateService service;

    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Init)
    public AjaxResponse init(@RequestBody Map<String, Object> param) {
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        // 初始化（取得检索条件信息)
        Map<String, Object> resultBean = service.init(param);
        //返回数据的类型
        return success(resultBean);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetPage)
    public AjaxResponse getPage(@RequestBody Map<String, Object> param) {
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        Object result = service.getPage(param);
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetCount)
    public AjaxResponse getCount(@RequestBody Map<String, Object> param) {
        param.put("channelId", this.getUser().getSelChannelId());
        Object result = service.getCount(param);
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Save)
    public AjaxResponse save(@RequestBody CmsBtImageTemplateModel model) {
        model.setChannelId(this.getUser().getSelChannelId());
        service.save(model, this.getUser().getUserName());
        return success(null);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Delete)
    public AjaxResponse delete(@RequestBody Long imageTemplateId) {
        service.delete(imageTemplateId);
        return success(null);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Get)
    public AjaxResponse get(@RequestBody Long imageTemplateId) {
        CmsBtImageTemplateModel model = service.get(imageTemplateId);
        return success(model);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetTemplateParameter)
    public AjaxResponse getTemplateParameter(@RequestBody String templateContent) {
        return success(service.getTemplateParameter(templateContent));
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetDownloadUrl)
    public AjaxResponse getDownloadUrl(@RequestBody GetDownloadUrlParamter paramter) throws Exception {
        String str = service.getDownloadUrl(paramter);
        return success(str);
    }
}
