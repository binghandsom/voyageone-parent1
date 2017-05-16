package com.voyageone.service.impl.cms.promotion;

import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.DateTimeUtilBeijing;
import com.voyageone.service.bean.cms.CmsBtPromotionCodesBean;
import com.voyageone.service.dao.cms.CmsBtPromotionCodesDao;
import com.voyageone.service.daoext.cms.CmsBtPromotionCodesDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPromotionCodesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aooer 2016/2/18.
 * @author jonas
 * @version 2.7.1
 * @since 2.0.0
 */
@Service
public class PromotionCodeService extends BaseService {
    private final CmsBtPromotionCodesDao cmsBtPromotionCodesDao;
    private final CmsBtPromotionCodesDaoExt cmsBtPromotionCodesDaoExt;

    @Autowired
    public PromotionCodeService(CmsBtPromotionCodesDaoExt cmsBtPromotionCodesDaoExt, CmsBtPromotionCodesDao cmsBtPromotionCodesDao) {
        this.cmsBtPromotionCodesDaoExt = cmsBtPromotionCodesDaoExt;
        this.cmsBtPromotionCodesDao = cmsBtPromotionCodesDao;
    }

    public List<CmsBtPromotionCodesBean> getPromotionCodeList(Map<String, Object> param) {
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeList(param);
    }

    public int getPromotionCodeListCnt(Map<String, Object> params) {
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeListCnt(params);
    }

    public List<CmsBtPromotionCodesBean> getPromotionCodeListByIdOrgChannelId(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId", orgChannelId);
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeSkuList(params);
    }
    public List<CmsBtPromotionCodesBean> getPromotionCodeListByIdOrgChannelId2(int promotionId, String orgChannelId) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionId", promotionId);
        params.put("orgChannelId", orgChannelId);
        return cmsBtPromotionCodesDaoExt.selectPromotionCodeSku2List(params);
    }

    public List<Map<String, Object>> getPromotionCodesByPromotionIds(List<String> promotionIdList) {
        return cmsBtPromotionCodesDaoExt.selectCmsBtPromotionAllCodeByPromotionIdS(promotionIdList);
    }
    /**
     *
     * 判断是否存在现在时点，指定Code正处于没有结束的活动中。
     */
    public String getExistCodeInActivePromotion(String channelId, String productCode, Integer cartId) {
        Map<String, Object> param = new HashMap<>();
        param.put("channelId", channelId);
        param.put("productCode", productCode);
        param.put("cartId", cartId);
        param.put("now", DateTimeUtil.format(DateTimeUtilBeijing.getCurrentBeiJingDate(), DateTimeUtil.DEFAULT_DATETIME_FORMAT));
        return cmsBtPromotionCodesDaoExt.selectCodeInActivePromotionName(param);
    }

    /**
     * @since 2.7.1
     */
    public List<CmsBtPromotionCodesModel> getPromotionCodeList(int promotionId, String productModel) {
        CmsBtPromotionCodesModel parameter = new CmsBtPromotionCodesModel();
        parameter.setPromotionId(promotionId);
        parameter.setProductModel(productModel);
        return cmsBtPromotionCodesDao.selectList(parameter);
    }

    public int getCmsBtPromotionCodeInPromtionCnt(String code, List<Integer> promotionIds){
        return cmsBtPromotionCodesDaoExt.selectCmsBtPromotionCodeInPromtionCnt(code, promotionIds);
    }
}
