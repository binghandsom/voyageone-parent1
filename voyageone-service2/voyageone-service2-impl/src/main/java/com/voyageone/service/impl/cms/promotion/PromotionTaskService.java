package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.dao.cms.CmsBtTaskTejiabaoDao;
import com.voyageone.service.daoext.cms.CmsBtTaskTejiabaoDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private CmsBtTaskTejiabaoDaoExt cmsPromotionTaskDaoExt;
    @Autowired
    private CmsBtTaskTejiabaoDao cmsPromotionTaskDao;

    public int getPromotionTaskPriceListCnt(Map<String,Object> params){
        return cmsPromotionTaskDaoExt.selectPromotionTaskPriceListCnt(params);
    }

    public List<Map<String,Object>> getPromotionTaskPriceList(Map<String,Object> params){
        return cmsPromotionTaskDaoExt.selectPromotionTaskPriceList(params);
    }

    @VOTransactional
    public int addPromotionTask(CmsBtTaskTejiabaoModel model) {
        return cmsPromotionTaskDaoExt.insertPromotionTask(model);
    }

    @VOTransactional
    public int updatePromotionTask(CmsBtTaskTejiabaoModel model) {
        return cmsPromotionTaskDaoExt.updatePromotionTask(model);
    }

    public CmsBtTaskTejiabaoModel get(Integer promotionId, String code){
        Map<String, Object> params = new HashMap();
        params.put("promotionId",promotionId);
        params.put("key",code);
        return cmsPromotionTaskDao.selectOne(params);
    }
}
