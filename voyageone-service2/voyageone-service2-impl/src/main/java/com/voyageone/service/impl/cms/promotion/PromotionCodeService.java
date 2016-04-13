package com.voyageone.service.impl.cms.promotion;

import com.voyageone.service.dao.cms.CmsBtPromotionCodeDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionCodeModel;
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
public class PromotionCodeService extends BaseService {

    @Autowired
    private CmsBtPromotionCodeDao cmsBtPromotionCodeDao;

    public List<CmsBtPromotionCodeModel> getPromotionCodeList(Map<String, Object> param){
        return cmsBtPromotionCodeDao.selectPromotionCodeList(param);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params){
        return cmsBtPromotionCodeDao.selectPromotionCodeListCnt(params);
    }

    public List<CmsBtPromotionCodeModel> getPromotionCodeListByIdOrgChannelId(int promotionId, String orgChannelId){
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId",orgChannelId);
        return cmsBtPromotionCodeDao.selectPromotionCodeSkuList(params);
    }

    public int deletePromotionCode(CmsBtPromotionCodeModel model){
        return cmsBtPromotionCodeDao.deletePromotionCode(model);
    }

}
