package com.voyageone.service.impl.cms;

import com.voyageone.service.dao.cms.mongo.CmsBtTranslateDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.CmsBtCustomPropModel.CustomPropType;
import com.voyageone.service.model.cms.mongo.CmsBtTranslateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by james on 2017/2/27.
 */
@Service
public class CmsBtTranslateService extends BaseService {

    @Autowired
    private CmsBtTranslateDao cmsBtTranslateDao;

    public CmsBtTranslateModel get(String channelId, Integer customPropType, String name){
        return cmsBtTranslateDao.get(channelId,customPropType,name);
    }

    public void insertOrUpdate(CmsBtTranslateModel cmsBtTranslateModel){
        cmsBtTranslateDao.
    }
}
