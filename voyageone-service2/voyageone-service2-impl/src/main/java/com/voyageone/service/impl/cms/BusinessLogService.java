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
 * 该组件当 cms2 中出现业务错误时, 用来记录这些错误。并可查询
 * <p>
 * 这些记录将保存在 cms_bt_business_log 表中
 *
 * @author aooer 2016/1/20.
 * @version 2.4.0
 * @since 2.0.0
 */
@Service
public class BusinessLogService extends BaseService {

    private final CmsBtBusinessLogDaoExt cmsBtBusinessLogDaoExt;

    private final CmsBtBusinessLogDao cmsBtBusinessLogDao;

    @Autowired
    public BusinessLogService(CmsBtBusinessLogDaoExt cmsBtBusinessLogDaoExt, CmsBtBusinessLogDao cmsBtBusinessLogDao) {
        this.cmsBtBusinessLogDaoExt = cmsBtBusinessLogDaoExt;
        this.cmsBtBusinessLogDao = cmsBtBusinessLogDao;
    }

    public List<CmsBtBusinessLogModel> getList(Map params) {
        return cmsBtBusinessLogDaoExt.selectByCondition(params);
    }

    public int getCount(Map params) {
        return cmsBtBusinessLogDaoExt.selectByConditionCnt(params);
    }

    @VOTransactional
    public int updateFinishStatus(Map params) {
        return cmsBtBusinessLogDaoExt.updateStatusFinish(params);
    }

    @VOTransactional
    public int insertBusinessLog(CmsBtBusinessLogModel record) {
        return cmsBtBusinessLogDao.insert(record);
    }
}
