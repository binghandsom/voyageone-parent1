package com.voyageone.service.impl.cms.product;

import com.voyageone.common.util.BeanUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductLogDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductLogModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductLogService extends BaseService {

    private final CmsBtProductLogDao cmsBtProductLogDao;

    @Autowired
    public ProductLogService(CmsBtProductLogDao cmsBtProductLogDao) {
        this.cmsBtProductLogDao = cmsBtProductLogDao;
    }

    /**
     * CmsBtProductModel变更接口留下日志
     */
    public void insertProductHistory(CmsBtProductModel product) {
        CmsBtProductLogModel logModel = new CmsBtProductLogModel();
        BeanUtils.copy(product, logModel);
        logModel.set_id(null);
        cmsBtProductLogDao.insert(logModel);
    }

}
