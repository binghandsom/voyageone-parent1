package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsPromotionCodeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
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
public class PromotionCodeService extends BaseService {

    @Autowired
    private CmsPromotionCodeDao cmspromotionCodeDao;

    public List<CmsBtPromotionCodeModel> getPromotionCodeList(Map<String, Object> param){
        return cmspromotionCodeDao.getPromotionCodeList(param);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params){
        return cmspromotionCodeDao.getPromotionCodeListCnt(params);
    }

    public int deletePromotionCode(CmsBtPromotionCodeModel model){
        return cmspromotionCodeDao.deletePromotionCode(model);
    }

}
