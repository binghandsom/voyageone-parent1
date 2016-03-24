package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsBtPromotionTaskDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionTaskModel;
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
public class PromotionTaskService extends BaseService {

    @Autowired
    private CmsBtPromotionTaskDao cmsPromotionTaskDao;

    public int insertPromotionTask(CmsBtPromotionTaskModel model){
        return cmsPromotionTaskDao.insertPromotionTask(model);
    }

    public int getPromotionTaskPriceListCnt(Map<String,Object> params){
        return cmsPromotionTaskDao.getPromotionTaskPriceListCnt(params);
    }

    public List<Map<String,Object>> getPromotionTaskPriceList(Map<String,Object> params){
        return cmsPromotionTaskDao.getPromotionTaskPriceList(params);
    }

    public int updatePromotionTask(CmsBtPromotionTaskModel model){
        return cmsPromotionTaskDao.updatePromotionTask(model);
    }
}
