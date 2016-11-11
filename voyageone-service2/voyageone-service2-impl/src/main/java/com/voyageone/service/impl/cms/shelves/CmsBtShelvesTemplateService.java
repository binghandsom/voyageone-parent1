package com.voyageone.service.impl.cms.shelves;

import com.voyageone.service.dao.cms.CmsBtShelvesTemplateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtShelvesTemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rex.wu on 2016/11/11.
 */
@Service
public class CmsBtShelvesTemplateService extends BaseService {

    @Autowired
    private CmsBtShelvesTemplateDao cmsBtShelvesTemplateDao;

    public void insert(CmsBtShelvesTemplateModel template) {
        String templateName = template.getTemplateName();
        Integer templateType = template.getTemplateType();
        Integer clientType = template.getClientType();
//        String cartId = template.getCartId();


    }


}
