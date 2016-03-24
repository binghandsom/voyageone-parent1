package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsBtPromotionModelDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionGroupModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
public class PromotionModelService extends BaseService {

    @Autowired
    private CmsBtPromotionModelDao cmsPromotionModelDao;

    public List<Map<String,Object>> getPromotionModelDetailList(Map<String,Object> param) {
        return cmsPromotionModelDao.selectPromotionModelDetailList(param);
    }

    public int getPromotionModelDetailListCnt(Map<String,Object> param){
        return cmsPromotionModelDao.selectPromotionModelDetailListCnt(param);
    }

    public int deleteCmsPromotionModel(CmsBtPromotionGroupModel model){
        return cmsPromotionModelDao.deleteCmsPromotionModel(model);
    }

}
