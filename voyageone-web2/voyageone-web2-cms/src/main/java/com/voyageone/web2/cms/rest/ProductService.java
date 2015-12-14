package com.voyageone.web2.cms.rest;

import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.web2.base.BaseAppService;
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
public class ProductService extends BaseAppService{

    @Autowired
    private CmsProductService cmsProductService;

    public CmsBtProductModel selectOne (String param) {

        CmsBtProductModel model = cmsProductService.getProductByCode("001", "100001");

        return model;
    }

}
