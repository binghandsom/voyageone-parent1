package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.service.bean.cms.jumei.UpdateSkuDealPriceParameter;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionSku3Service {
    @Autowired
    CmsBtJmPromotionSkuDao dao;
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    CmsBtJmPromotionProductDaoExt daoExtCmsBtJmPromotionProduct;

    public CmsBtJmPromotionSkuModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionSkuModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionSkuModel entity) {
        return dao.insert(entity);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    @VOTransactional
    public int updateDealPrice(UpdateSkuDealPriceParameter parameter, String modifier) {
        CmsBtJmPromotionSkuModel model = dao.select(parameter.getPromotionSkuId());
        model.setDealPrice(BigDecimal.valueOf(parameter.getDealPrice()));
        model.setMarketPrice(BigDecimal.valueOf(parameter.getMarketPrice()));
        model.setDiscount(BigDecimalUtil.divide(model.getDealPrice(), model.getMarketPrice(), 4));
        model.setModifier(modifier);
        int result = update(model);
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct = daoCmsBtJmPromotionProduct.select(model.getCmsBtJmPromotionProductId());
        if (modelCmsBtJmPromotionProduct.getUpdateStatus() != 1) {
            modelCmsBtJmPromotionProduct.setUpdateStatus(1);
            daoCmsBtJmPromotionProduct.update(modelCmsBtJmPromotionProduct);
        }
        daoExtCmsBtJmPromotionProduct.updateAvgPriceByPromotionProductId(model.getCmsBtJmPromotionProductId());//更新平均价格
        return result;
    }
}

