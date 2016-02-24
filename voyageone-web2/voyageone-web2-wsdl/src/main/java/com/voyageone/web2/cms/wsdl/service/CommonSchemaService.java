package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.cms.service.dao.mongodb.CmsMtCategorySchemaDao;
import com.voyageone.common.masterdate.schema.factory.SchemaJsonReader;
import com.voyageone.common.masterdate.schema.field.Field;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsMtCommonSchemaDao;
import com.voyageone.web2.sdk.api.request.CommonSchemaGetRequest;
import com.voyageone.web2.sdk.api.response.CommonSchemaGetResponse;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    public CommonSchemaGetResponse selectAll(CommonSchemaGetRequest request) {
        CommonSchemaGetResponse response=new CommonSchemaGetResponse();
        List<Field> fields=cmsMtCommonSchemaDao.selectAll();
        if(!CollectionUtils.isEmpty(fields)) {
            response.setFields(fields);
            response.setTotalCount(Long.parseLong(String.valueOf(fields.size())));
        }
        return response;
    }

}
