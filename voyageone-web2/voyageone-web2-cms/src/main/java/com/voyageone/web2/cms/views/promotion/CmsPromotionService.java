package com.voyageone.web2.cms.views.promotion;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsPromotionDao;
import com.voyageone.web2.cms.model.CmsBtPromotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<CmsBtPromotionModel> getPromotionList(Map<String,Object> params){
        return cmsPromotionDao.getPromotionList(params);
    }

    public int insertPromotion(CmsBtPromotionModel params){
        return cmsPromotionDao.insertPromotion(params);
    }

    public int updatePromotion(CmsBtPromotionModel params){
        return cmsPromotionDao.updatePromotion(params);
    }
}
