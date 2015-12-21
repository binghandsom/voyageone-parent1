package com.voyageone.web2.cms.views.pop.promotion;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.cms.dao.CmsPromotionCodeDao;
import com.voyageone.web2.cms.model.CmsBtPromotionCodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/21
 * @version 2.0.0
 */
@Service
public class CmsPromotionHistoryService extends BaseAppService{

    @Autowired
    private CmsPromotionCodeDao cmsPromotionCodeDao;

    public List<CmsBtPromotionCodeModel> getPromotionList(Map<String, Object> params) {
        return cmsPromotionCodeDao.getPromotionCodeList(params);
    }

}
