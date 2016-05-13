package com.voyageone.web2.cms.views.channel.listing;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.Constants;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.Types;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.impl.cms.ImageTemplateService;
import com.voyageone.service.model.cms.mongo.channel.CmsBtImageTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CmsImageTemplateService {
    @Autowired
    ImageTemplateService service;
    /**
     * 取得检索条件信息
     *
     * @param param 客户端参数
     * @return 检索条件信息
     */
    public Map<String, Object> init(Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        // 取得当前channel, 有多少个platform(Approve平台)
        result.put("platformList", TypeChannels.getTypeListSkuCarts((String) param.get("channelId"), "A", (String) param.get("lang")));
        // 品牌下拉列表
        result.put("brandNameList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.BRAND_41, (String) param.get("channelId"), (String) param.get("lang")));
        // 产品类型下拉列表
        result.put("productTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_57, (String) param.get("channelId"), (String) param.get("lang")));
        // 尺寸类型下拉列表
        result.put("sizeTypeList", TypeChannels.getTypeWithLang(Constants.comMtTypeChannel.PROUDCT_TYPE_58, (String) param.get("channelId"), (String) param.get("lang")));

        result.put("imageTemplateList", Types.getTypeList(Constants.comMtTypeChannel.Image_Template_Type.toString(), (String) param.get("lang")));
        return result;
    }
   public void save(CmsBtImageTemplateModel model,String userName)
   {
       check(model);
       service.save(model,userName);
   }
    void check(CmsBtImageTemplateModel model) {
        CallResult result = new CallResult();
        if (model.getBrandName() == null || model.getBrandName().size() == 0) {
            throw new BusinessException("7000080");
        }
        //7000080  必填
        if (StringUtils.isEmpty(model.getImageTemplateName())) {
            throw new BusinessException("7000080");
        }
        if (model.getCartId() == null || model.getCartId() == 0) {
            throw new BusinessException("7000080");
        }
        if (model.getViewType() == null || model.getViewType() == 0) {
            throw new BusinessException("7000080");
        }
        if (model.getImageTemplateType() == null || model.getImageTemplateType() == 0) {
            throw new BusinessException("7000080");
        }
        if (StringUtils.isEmpty(model.getImageTemplateContent())) {
            throw new BusinessException("7000080");
        }
        long ImageTemplateId = 0;
        if (model.getImageTemplateId() != null) {
            ImageTemplateId = model.getImageTemplateId();
        }
        if (service.EXISTSName(model.getImageTemplateName(), ImageTemplateId)) {
            //名称已经存在
            throw new BusinessException("4000009");
        }
    }
}
