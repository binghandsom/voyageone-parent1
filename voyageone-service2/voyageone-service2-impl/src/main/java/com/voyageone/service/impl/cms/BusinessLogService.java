package com.voyageone.service.impl.cms;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.cms.CmsBtBusinessLogDao;
import com.voyageone.service.daoext.cms.CmsBtBusinessLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtBusinessLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/1/20.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class BusinessLogService extends BaseService {

    @Autowired
    private CmsBtBusinessLogDaoExt cmsBtBusinessLogDaoExt;

    @Autowired
    private CmsBtBusinessLogDao cmsBtBusinessLogDao;

    public List<CmsBtBusinessLogModel> getList(Map params){
        return cmsBtBusinessLogDaoExt.selectByCondition(params);
    }

    public int getCount(Map params){
        return cmsBtBusinessLogDaoExt.selectByConditionCnt(params);
    }

    @VOTransactional
    public int updateFinishStatus(Map params){
        return cmsBtBusinessLogDaoExt.updateStatusFinish(params);
    }

    @VOTransactional
    public int insertBusinessLog(CmsBtBusinessLogModel record){
        return cmsBtBusinessLogDao.insert(record);
    }

}
