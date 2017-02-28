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

    private final CmsBtTranslateDao cmsBtTranslateDao;

    @Autowired
    public CmsBtTranslateService(CmsBtTranslateDao cmsBtTranslateDao) {
        this.cmsBtTranslateDao = cmsBtTranslateDao;
    }

    public CmsBtTranslateModel get(String channelId, Integer customPropType, String name, String valueEn){
        return cmsBtTranslateDao.get(channelId,customPropType,name,valueEn.toLowerCase());
    }

    public void insertOrUpdate(CmsBtTranslateModel model){
        CmsBtTranslateModel cmsBtTranslateModel = cmsBtTranslateDao.get(model.getChannelId(),model.getType(), model.getName(), model.getValueEn().toLowerCase());
        if(cmsBtTranslateModel == null){
            model.set_id(null);
            cmsBtTranslateDao.insert(model);
        }else{
            model.set_id(cmsBtTranslateModel.get_id());
            cmsBtTranslateDao.save(model);
        }
    }

    public void create(String channelId, Integer customPropType, String name, String valueEn, String valueCn){
        CmsBtTranslateModel cmsBtTranslateModel = get(channelId, customPropType, name, valueEn);
        if(cmsBtTranslateModel == null) {
            cmsBtTranslateModel = new CmsBtTranslateModel();
        }
        cmsBtTranslateModel.setChannelId(channelId);
        cmsBtTranslateModel.setType(customPropType);
        cmsBtTranslateModel.setName(name);
        cmsBtTranslateModel.setValueEn(valueEn.toLowerCase());
        cmsBtTranslateModel.setValueCn(valueCn);
        insertOrUpdate(cmsBtTranslateModel);
    }
}
