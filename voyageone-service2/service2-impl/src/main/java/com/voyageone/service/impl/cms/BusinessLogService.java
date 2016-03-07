package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.CmsBtBusinessLogDao;
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
