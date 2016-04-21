package com.voyageone.service.impl.cms.jumei.platform;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.reponse.HtDealUpdateDealEndTimeResponse;
import com.voyageone.components.jumei.request.HtDealUpdateDealEndTimeRequest;
import com.voyageone.service.dao.jumei.CmsBtJmPromotionProductDao;
import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class JuMeiDealService {
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    JuMeiProductUpdateService serviceJuMeiProductUpdate;
    @Autowired
    JumeiHtDealService serviceJumeiHtDeal;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    public   void updateDealEndTime(int promotionId) throws Exception {
        Map<String, Object> parameterPromotionProduct = new HashMap<>();

        parameterPromotionProduct.put("promotionId", promotionId);
        parameterPromotionProduct.put("dealEndTimeState", 1);
        List<CmsBtJmPromotionProductModel> listPromotionProduct = daoCmsBtJmPromotionProduct.selectList(parameterPromotionProduct);
        if(listPromotionProduct.size()==0) return;;
        ShopBean shopBean = serviceJMShopBean.getShopBean(listPromotionProduct.get(0).getChannelId());
        for (CmsBtJmPromotionProductModel model : listPromotionProduct) {
            updateDealEndTime((ShopBean) shopBean, (CmsBtJmPromotionProductModel) model);
        }
    }
    private void updateDealEndTimeSave(ShopBean shopBean, CmsBtJmPromotionProductModel model) {
        updateDealEndTime(shopBean, model);
        daoCmsBtJmPromotionProduct.update(model);
    }

    public void updateDealEndTime(ShopBean shopBean, CmsBtJmPromotionProductModel model) {
        try {
            HtDealUpdateDealEndTimeRequest request = new HtDealUpdateDealEndTimeRequest();
            request.setEnd_time(getTime(model.getDealEndTime()));
            request.setJumei_hash_id(model.getJmHashId());
            HtDealUpdateDealEndTimeResponse response = serviceJumeiHtDeal.updateDealEndTime(shopBean, request);
            if (response.is_Success()) {
                model.setDealEndTimeState(2);
                model.setActivityEnd(model.getDealEndTime());
            } else {
                model.setDealEndTimeState(3);
                model.setErrorMsg("延迟Deal结束时间失败" + response.getBody());
            }
        } catch (Exception ex) {
            model.setDealEndTimeState(3);
            model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
        }
    }

    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }
}
