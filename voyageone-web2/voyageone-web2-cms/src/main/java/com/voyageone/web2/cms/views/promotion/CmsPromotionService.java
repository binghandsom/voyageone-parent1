package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsBtTagDao;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import com.voyageone.web2.cms.model.CmsBtTagModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author james
 * @version 2.0.0, 15/12/11
 */
@Service
public class CmsPromotionService extends BaseAppService {

    @Autowired
    private CmsPromotionDao cmsPromotionDao;

    @Autowired
    private CmsBtTagDao cmsBtTagDao;

    public List<CmsBtPromotionModel> getPromotionList(Map<String, Object> params) {
        return cmsPromotionDao.getPromotionList(params);
    }

    @Transactional
    public int insertPromotion(CmsBtPromotionModel params) {
        CmsBtTagModel cmsBtTagModel = new CmsBtTagModel();
        cmsBtTagModel.setCreater(params.getCreater());
        cmsBtTagModel.setTagName(params.getPromotionName());
        cmsBtTagModel.setTagPathName(params.getPromotionName());
        cmsBtTagModel.setTagType(2);
        cmsBtTagModel.setTagStatus(0);
        cmsBtTagModel.setParentTagId(0);
        cmsBtTagModel.setTagPath("");
        cmsBtTagDao.insertCmsBtTag(cmsBtTagModel);
        cmsBtTagModel.setTagPath("-" + cmsBtTagModel.getTagId() + "-");
        cmsBtTagDao.updateCmsBtTag(cmsBtTagModel);

        params.setRefTagId(cmsBtTagModel.getTagId());


        return cmsPromotionDao.insertPromotion(params);
    }

    public int updatePromotion(CmsBtPromotionModel params) {
        return cmsPromotionDao.updatePromotion(params);
    }
}
