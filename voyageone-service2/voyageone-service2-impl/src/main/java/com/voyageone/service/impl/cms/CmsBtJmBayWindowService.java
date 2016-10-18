package com.voyageone.service.impl.cms;

import com.mongodb.WriteResult;
import com.voyageone.service.dao.cms.mongo.CmsBtJmBayWindowDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by james on 2016/10/17.
 */
@Service
public class CmsBtJmBayWindowService extends BaseService {

    @Autowired
    CmsBtJmBayWindowDao cmsBtJmBayWindowDao;
    public CmsBtJmBayWindowModel getBayWindowByJmPromotionId(Integer JmPromotionId){
        return cmsBtJmBayWindowDao.selectOneWithQuery("{'jmPromotionId':"+JmPromotionId+"}");
    }

    public WriteResult insert(CmsBtJmBayWindowModel cmsBtJmBayWindowModel){
        return cmsBtJmBayWindowDao.insert(cmsBtJmBayWindowModel);
    }
}
