package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.imagetemplate.GetDownloadUrlParamter;
import com.voyageone.service.bean.cms.imagetemplate.ImageTempateParameter;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(
        value = CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.ROOT,
        method = RequestMethod.POST
)
public class CmsImageTemplateController extends CmsController {
    @Autowired
    private ImageTemplateService service;
    @Autowired
    CmsImageTemplateService serviceCmsImageTemplate;
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Init)
    public AjaxResponse init(@RequestBody Map<String, Object> param) {
        param.put("channelId", this.getUser().getSelChannelId());
        param.put("lang", this.getLang());
        // 初始化（取得检索条件信息)
        Map<String, Object> resultBean = serviceCmsImageTemplate.init(param);
        //返回数据的类型
        return success(resultBean);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetPage)
    public AjaxResponse getPage(@RequestBody ImageTempateParameter param) {
        String selChannelId = param.getChannelId();
        if (StringUtils.isEmpty(selChannelId)) {
            selChannelId = this.getUser().getSelChannelId();
        }
        Object result = service.getPage(param, selChannelId, this.getLang());
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetCount)
    public AjaxResponse getCount(@RequestBody ImageTempateParameter param) {
        //param.put("channelId", );
        Object result = service.getCount(param,this.getUser().getSelChannelId());
        return success(result);
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.Save)
    public AjaxResponse save(@RequestBody CmsBtImageTemplateModel model) {
        CallResult result=new CallResult();
        if(StringUtil.isEmpty(model.getChannelId())) {
            model.setChannelId(this.getUser().getSelChannelId());
        }
       serviceCmsImageTemplate.save(model, this.getUser().getUserName());
        return success(result);
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
        //根据模板 获取生成的模板参数
        return success(service.getTemplateParameter(templateContent));
    }
    @RequestMapping(CmsUrlConstants.CHANNEL.CHANNEL_IMAGE_TEMPLATE.GetDownloadUrl)
    public AjaxResponse getDownloadUrl(@RequestBody GetDownloadUrlParamter paramter) throws Exception {
        //根据模板内容和模板参数 获取图片下载地址
        String str = service.getDownloadUrl(paramter);
        return success(str);
    }
}
