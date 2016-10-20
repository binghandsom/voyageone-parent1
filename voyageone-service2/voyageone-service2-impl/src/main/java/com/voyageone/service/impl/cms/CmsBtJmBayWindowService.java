package com.voyageone.service.impl.cms;

import com.mongodb.WriteResult;
import com.voyageone.service.dao.cms.mongo.CmsBtJmBayWindowDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtTagJmModuleExtensionModel;
import com.voyageone.service.model.cms.mongo.jm.promotion.CmsBtJmBayWindowModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by james on 2016/10/17.
 *
 * @version 2.8.0
 * @since 2.8.0
 */
@Service
public class CmsBtJmBayWindowService extends BaseService {

    private final CmsBtJmBayWindowDao cmsBtJmBayWindowDao;

    @Autowired
    public CmsBtJmBayWindowService(CmsBtJmBayWindowDao cmsBtJmBayWindowDao) {
        this.cmsBtJmBayWindowDao = cmsBtJmBayWindowDao;
    }

    public CmsBtJmBayWindowModel getBayWindowByJmPromotionId(Integer JmPromotionId) {
        return cmsBtJmBayWindowDao.selectOneWithQuery("{'jmPromotionId':" + JmPromotionId + "}");
    }

    public void insert(CmsBtJmBayWindowModel cmsBtJmBayWindowModel) {
        cmsBtJmBayWindowDao.insert(cmsBtJmBayWindowModel);
    }

    public CmsBtJmBayWindowModel createByPromotion(CmsBtJmPromotionModel jmPromotionModel, String username) {
        CmsBtJmBayWindowModel jmBayWindowModel = new CmsBtJmBayWindowModel();

        jmBayWindowModel.setChannelId(jmPromotionModel.getChannelId());
        jmBayWindowModel.setJmPromotionId(jmPromotionModel.getId());
        jmBayWindowModel.setFixed(true);
        jmBayWindowModel.setCreater(username);
        jmBayWindowModel.setModifier(username);

        return jmBayWindowModel;
    }
}
