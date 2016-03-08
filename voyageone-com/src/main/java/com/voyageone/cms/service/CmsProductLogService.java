package com.voyageone.cms.service;

import com.voyageone.cms.service.dao.mongodb.CmsBtProductLogDao;
import com.voyageone.cms.service.model.CmsBtProductLogModel;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsProductLogService {
    @Autowired
    private CmsBtProductLogDao cmsBtProductLogDao;

    /**
     * CmsBtProductModel变更接口留下日志
     */
    public void insertProductHistory(CmsBtProductModel product) {
        CmsBtProductLogModel logModel = new CmsBtProductLogModel();
        BeanUtil.copy(product, logModel);
        logModel.set_id(null);
        cmsBtProductLogDao.insert(logModel);
    }

}
