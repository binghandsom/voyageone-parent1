package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.service.dao.cms.CmsMtPlatformSpecialFieldDao;
import com.voyageone.service.model.cms.CmsMtPlatformSpecialFieldModel;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Platform Special Field Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class PlatformSpecialFieldService extends BaseService {

    @Autowired
    private CmsMtPlatformSpecialFieldDao platformSpecialFieldDao;

    /**
     * get
     * @param request PlatformSpecialFieldsGetRequest
     * @return PlatformSpecialFieldsGetResponse
     */
    public PlatformSpecialFieldsGetResponse get(PlatformSpecialFieldsGetRequest request) {
        checkCommRequest(request);
        request.check();

        PlatformSpecialFieldsGetResponse result = new PlatformSpecialFieldsGetResponse();
        List<CmsMtPlatformSpecialFieldModel> list = platformSpecialFieldDao.select(request.getCartId(),
                                                                                   request.getCatId(),
                                                                                   request.getFieldId(),
                                                                                   request.getType());

        result.setFields(list);
        if (list != null) {
            result.setTotalCount((long)list.size());
        }
        return result;
    }

    /**
     * put insert
     * @param request PlatformSpecialFieldsPutRequest
     * @return PlatformSpecialFieldsPutResponse
     */
    public PlatformSpecialFieldsPutResponse put(PlatformSpecialFieldsPutRequest request) {
        checkCommRequest(request);
        request.check();

        PlatformSpecialFieldsPutResponse result = new PlatformSpecialFieldsPutResponse();
        platformSpecialFieldDao.insertWithList(request.getSpecialFields());
        result.setInsertedCount(request.getSpecialFields().size());
        return result;
    }

    /**
     * delete
     * @param request PlatformSpecialFieldDeleteRequest
     * @return PlatformSpecialFieldDeleteResponse
     */
    public PlatformSpecialFieldDeleteResponse delete(PlatformSpecialFieldDeleteRequest request) {
        checkCommRequest(request);
        request.check();

        PlatformSpecialFieldDeleteResponse result = new PlatformSpecialFieldDeleteResponse();

        CmsMtPlatformSpecialFieldModel model = new CmsMtPlatformSpecialFieldModel();
        model.setCartId(request.getCartId());
        model.setCatId(request.getCatId());
        model.setFieldId(request.getFieldId());
        model.setType(request.getType());

        platformSpecialFieldDao.delete(model);
        result.setRemovedCount(1);

        return result;
    }
}
