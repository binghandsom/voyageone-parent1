package com.voyageone.web2.cms.views.task;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.*;
import com.voyageone.web2.cms.model.*;
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
    protected CmsPromotionTaskDao cmsPromotionTaskDao;

   public List<Map<String,Object>> getPriceList(Map<String,Object> param){

       return cmsPromotionTaskDao.getPromotionTaskPriceList(param);
   }

    public int getPriceListCnt(Map<String,Object> param){

        return cmsPromotionTaskDao.getPromotionTaskPriceListCnt(param);
    }

    public int updateTaskStatus(CmsBtPromotionTaskModel param){
        return cmsPromotionTaskDao.updatePromotionTask(param);
    }
}
