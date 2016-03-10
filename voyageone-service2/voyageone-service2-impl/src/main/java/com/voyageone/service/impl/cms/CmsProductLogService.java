package com.voyageone.service.impl.cms;

import com.voyageone.common.util.BeanUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductLogDao;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
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
