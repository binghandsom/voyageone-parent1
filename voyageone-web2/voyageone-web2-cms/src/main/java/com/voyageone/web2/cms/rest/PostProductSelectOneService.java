package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */

@Service
public class PostProductSelectOneService extends BaseAppService{

    @Autowired
    private CmsProductService cmsProductService;

    public CmsBtProductModel selectOne(PostProductSelectOneRequest params) {
        CmsBtProductModel model = null;

        if (params == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //ChannelId
        String channelId = params.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //getProductById
        Long pid = params.getProductId();
        if (pid != null) {
            return cmsProductService.getProductById(channelId, pid);
        }

        //getProductByCode
        String productCode = params.getProductCode();
        if (!StringUtils.isEmpty(productCode)) {
            return cmsProductService.getProductByCode(channelId, productCode);
        }

        return model;
    }

}
