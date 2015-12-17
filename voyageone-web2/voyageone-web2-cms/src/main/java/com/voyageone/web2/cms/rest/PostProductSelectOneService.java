package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
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
        Long pid = params.getProductId();
        if (pid != null) {
            model = cmsProductService.getProductById("001", pid);
        }

        return model;
    }

}
