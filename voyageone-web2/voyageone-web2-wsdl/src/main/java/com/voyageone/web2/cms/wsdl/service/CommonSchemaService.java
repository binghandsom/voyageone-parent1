package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.cms.service.dao.mongodb.CmsMtCommonSchemaDao;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.response.CommonSchemaGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aooer 2016/2/24.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class CommonSchemaService extends BaseService {

    @Autowired
    private CmsMtCommonSchemaDao cmsMtCommonSchemaDao;

    public CommonSchemaGetResponse selectAll() {

        CommonSchemaGetResponse response = new CommonSchemaGetResponse();

        List fieldList = cmsMtCommonSchemaDao.findAllProps();

        response.setFields(fieldList);
        response.setTotalCount((long) fieldList.size());

        return response;
    }

}
