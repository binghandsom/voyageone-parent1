package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.jumei2.BatchCopyDealParameter;
import com.voyageone.service.bean.cms.jumei2.BatchDeleteProductParameter;
import com.voyageone.service.bean.cms.jumei2.BatchSynchPriceParameter;
import com.voyageone.service.bean.cms.jumei2.BatchUpdatePriceParameterBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.cms.jumei.CmsMtJmConfigService;
import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductPlatformService;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionProduct3Service {
    @Autowired
    CmsBtJmPromotionProductDao dao;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    JuMeiProductPlatformService serviceJuMeiProductPlatform;
    @Autowired
    CmsMtJmConfigService serviceCmsMtJmConfig;
    @Autowired
    JMShopBeanService serviceJMShopBean;

    public CmsBtJmPromotionProductModel select(int id) {
        return dao.select(id);
    }

    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        return daoExt.getPageByWhere(map);
    }

    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.getCountByWhere(map);
    }

    public int delete(int id) {
        return dao.delete(id);
    }

    @VOTransactional
    public int updateDealPrice(BigDecimal dealPrice, int id, String userName) {
        CmsBtJmPromotionProductModel model = dao.select(id);
        model.setDealPrice(dealPrice);
        model.setModifier(userName);
        dao.update(model);
        return daoExtCmsBtJmPromotionSku.updateDealPrice(dealPrice, model.getId());
    }

    @VOTransactional
    public void deleteByPromotionId(int promotionId) {
        daoExt.deleteByPromotionId(promotionId);
        daoExtCmsBtJmPromotionSku.deleteByPromotionId(promotionId);
    }

    @VOTransactional
    public void deleteByProductIdList(ProductIdListInfo parameter) {
        daoExt.deleteByProductIdListInfo(parameter);
        daoExtCmsBtJmPromotionSku.deleteByProductIdListInfo(parameter);
    }

    //批量更新价格
    @VOTransactional
    public void batchUpdateDealPrice(BatchUpdatePriceParameterBean parameter) {
        if (parameter.getListPromotionProductId().size() == 0) return;
        String price = "";
        if (parameter.getPriceValueType() == 1) {//价格
            price = Double.toString(parameter.getPrice());
        } else//折扣 0：市场价 1：团购价
        {
            if (parameter.getPriceType() == 1)//团购价 deal_price
            {
                price = "deal_price*" + Double.toString(parameter.getDiscount());
            } else //市场价 market_price
            {
                price = "market_price*" + Double.toString(parameter.getDiscount());
            }
        }
        daoExt.batchUpdateDealPrice(parameter.getListPromotionProductId(), price);
        daoExtCmsBtJmPromotionSku.batchUpdateDealPrice(parameter.getListPromotionProductId(), price);
    }

    //批量同步价格
    public void batchSynchPrice(BatchSynchPriceParameter parameter) {
        if (parameter.getListPromotionProductId().size() == 0) return;
        daoExt.batchSynchPrice(parameter.getListPromotionProductId());
    }

    //全量同步价格
    public void synchAllPrice(int promotionId) {
        daoExt.synchAllPrice(promotionId);
    }

    //批量再售
    public void batchCopyDeal(BatchCopyDealParameter parameter) {
        if (parameter.getListPromotionProductId().size() == 0) return;
        daoExt.batchCopyDeal(parameter.getListPromotionProductId());
    }

    //全部再售
    public void copyDealAll(int promotionId) {
        daoExt.copyDealAll(promotionId);
    }

    //批量删除 product  已经再售的不删
    @VOTransactional
    public void batchDeleteProduct(BatchDeleteProductParameter parameter) {
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.batchDeleteSku(parameter.getListPromotionProductId());
        daoExt.batchDeleteProduct(parameter.getListPromotionProductId());

    }
    @VOTransactional //删除全部product  已经再售的不删
    public void deleteAllProduct(int promotionId) {
        //先删除sku 再删除product
        daoExtCmsBtJmPromotionSku.deleteAllSku(promotionId);
        daoExt.deleteAllProduct(promotionId);

    }
}

