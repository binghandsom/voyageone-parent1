package com.voyageone.web2.cms.views.promotion.task;

import com.voyageone.service.impl.cms.promotion.PromotionTaskService;
import com.voyageone.service.model.cms.CmsBtTaskTejiabaoModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author james.li on 2015/12/15.
 * @version 2.0.0
 */
@Service
public class CmsTaskPriceService extends BaseAppService {

    @Autowired
    private PromotionTaskService promotionTaskService;

   public List<Map<String,Object>> getPriceList(Map<String,Object> param){
       return promotionTaskService.getPromotionTaskPriceList(param);
   }

    public int getPriceListCnt(Map<String,Object> param){
        return promotionTaskService.getPromotionTaskPriceListCnt(param);
    }

    public int updateTaskStatus(CmsBtTaskTejiabaoModel param) {
        return promotionTaskService.updatePromotionTask(param);
    }
}
