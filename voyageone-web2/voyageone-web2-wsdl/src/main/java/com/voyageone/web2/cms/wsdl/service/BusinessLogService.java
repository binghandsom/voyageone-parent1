package com.voyageone.web2.cms.wsdl.service;

import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtBusinessLogDao;
import com.voyageone.web2.sdk.api.domain.CmsBtBusinessLogModel;
import com.voyageone.web2.sdk.api.request.BusinessLogGetRequest;
import com.voyageone.web2.sdk.api.request.BusinessLogUpdateRequest;
import com.voyageone.web2.sdk.api.response.BusinessLogGetResponse;
import com.voyageone.web2.sdk.api.response.BusinessLogUpdateResponse;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class BusinessLogService extends BaseService {

    @Autowired
    private CmsBtBusinessLogDao cmsBtBusinessLogDao;

    public BusinessLogGetResponse findList(BusinessLogGetRequest request){
        request.check();
        List<CmsBtBusinessLogModel> models=cmsBtBusinessLogDao.findByCondition(new BeanMap(request));
        BusinessLogGetResponse response =new BusinessLogGetResponse();
        response.setCmsBtBusinessLogModels(models);
        response.setTotalCount(Long.parseLong(String.valueOf(cmsBtBusinessLogDao.findByConditionCnt(new BeanMap(request)))));
        return response;
    }

    public BusinessLogUpdateResponse updateFinishStatus(BusinessLogUpdateRequest request){
        request.check();
        BusinessLogUpdateResponse response =new BusinessLogUpdateResponse();
        response.setModifiedCount(cmsBtBusinessLogDao.updateStatusFinish(new BeanMap(request)));
        return response;
    }

}
