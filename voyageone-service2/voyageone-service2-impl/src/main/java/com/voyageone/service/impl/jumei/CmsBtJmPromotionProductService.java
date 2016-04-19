package com.voyageone.service.impl.jumei;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.jumei.CmsBtJmPromotionProductDao;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.jumei.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.jumei.JMProductUpdate.JMShopBeanService;
import com.voyageone.service.impl.jumei.JMProductUpdate.JuMeiProductPlatefromService;
import com.voyageone.service.model.jumei.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.jumei.businessmodel.ProductIdListInfo;
import com.voyageone.service.model.jumei.businessmodel.PromotionProduct.ParameterUpdateDealEndTime;
import com.voyageone.service.model.jumei.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/3/18.
 */
@Service
public class CmsBtJmPromotionProductService {
    @Autowired
    CmsBtJmPromotionProductDao dao;
    @Autowired
    CmsBtJmPromotionProductDaoExt daoExt;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    @Autowired
    JuMeiProductPlatefromService serviceJuMeiProductPlatefrom;
    @Autowired
    CmsMtJmConfigService serviceCmsMtJmConfig;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    public CmsBtJmPromotionProductModel select(int id) {
        return dao.select(id);
    }

    public int update(CmsBtJmPromotionProductModel entity) {
        return dao.update(entity);
    }

    public int insert(CmsBtJmPromotionProductModel entity) {
        return dao.insert(entity);
    }

    public List<MapModel> getListByWhere(Map<String, Object> map) {
        return daoExt.getListByWhere(map);
    }

    public List<MapModel> getPromotionProductInfoListByWhere(Map<String, Object> map) {
        return daoExt.getPromotionProductInfoListByWhere(map);
    }
    public int getPromotionProductInfoCountByWhere(Map<String, Object> map)
    {
       return  daoExt.getPromotionProductInfoCountByWhere(map);
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

    //所有未上心商品上新
    public int jmNewUpdateAll(int promotionId) {
        return daoExt.jmNewUpdateAll(promotionId);
    }

    //部分商品上新
    public int jmNewByProductIdListInfo(ProductIdListInfo parameter) {
        return daoExt.jmNewByProductIdListInfo(parameter);
    }
    //所有未上心商品上新
    public int updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter) {
        return daoExt.updateDealEndTimeAll(parameter);
    }
    //部分商品上新
    public int updateDealEndTime(ParameterUpdateDealEndTime parameter) {
        return daoExt.updateDealEndTime(parameter);
    }
    // 根据条件检索出promoiton的product数据
    public CmsBtJmPromotionProductModel selectOne(Map<String, Object> param) {
        return dao.selectOne(param);
    }
    public CallResult updateJM(@RequestBody int promotionProductId) throws Exception {
        CallResult result = new CallResult();
        CmsBtJmPromotionProductModel model = dao.select(promotionProductId);
        int ShippingSystemId = serviceCmsMtJmConfig.getShippingSystemId(model.getChannelId());
        ShopBean shopBean = serviceJMShopBean.getShopBean(model.getChannelId());
       // if (model.getSynchState() == 0 || model.getSynchState() == 1 || model.getSynchState() == 4) {
            serviceJuMeiProductPlatefrom.updateJm(model, shopBean, ShippingSystemId);
       // }
        return result;
    }
}

