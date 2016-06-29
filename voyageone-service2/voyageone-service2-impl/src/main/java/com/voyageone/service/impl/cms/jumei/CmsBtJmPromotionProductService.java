package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.cms.CmsBtJmPromotionDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtTagDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.cms.jumei.platform.JMShopBeanService;
import com.voyageone.service.impl.cms.jumei.platform.JuMeiProductPlatformService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTime;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.model.util.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    JuMeiProductPlatformService serviceJuMeiProductPlatform;
    @Autowired
    CmsMtJmConfigService serviceCmsMtJmConfig;
    @Autowired
    JMShopBeanService serviceJMShopBean;
   @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtTagDao daoCmsBtTag;
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
        return daoExt.selectListByWhere(map);
    }

    public List<MapModel> getPageByWhere(Map<String, Object> map) {
        if(map.containsKey("code"))
        {
           String code= map.get("code").toString();
          String[] codeList=code.split("\r\n|\n|\\s+");//split("\r\n");
          map.put("codeList",codeList);
        }
        List<MapModel> list = daoExt.selectPageByWhere(map);
        for (MapModel model : list) {
            loadMap(model);
        }
        return list;
    }
      void  loadMap(MapModel map) {
          String promotionTag = map.get("promotionTag").toString();
          if (!StringUtils.isEmpty(promotionTag)) {
              String[] tagStrList =  promotionTag.split("\\|");
              List<String> tagNameList = new ArrayList<>();
              for (String tagName : tagStrList) {
                  if (!StringUtils.isEmpty(tagName)) {
                      tagNameList.add(tagName);
                  }
              }
              map.put("tagNameList",tagNameList);
          }
      }
    public int getCountByWhere(Map<String, Object> map) {
        return daoExt.selectCountByWhere(map);
    }

    public int delete(int id) {
      CmsBtJmPromotionProductModel modelProduct=dao.select(id);
        if(modelProduct.getSynchStatus()==2)
        {
            return 0;
        }
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

    //所有上新
    public CallResult updateDealEndTimeAll(ParameterUpdateDealEndTimeAll parameter) {
        CallResult result = new CallResult();
        CmsBtJmPromotionModel modelCmsBtJmPromotion = daoCmsBtJmPromotion.select(parameter.getPromotionId());
        //获取本活动商品在其他活动,处于在售状态的商品
        CmsBtJmPromotionProductModel modelJmPromotionProduct = daoExt.selectOnSaleByNoPromotionId(modelCmsBtJmPromotion.getChannelId(), parameter.getPromotionId());
        if (modelJmPromotionProduct != null) {

            result.setMsg("该专场商品已在其它聚美专场上传，且未过期("+modelJmPromotionProduct.getJmHashId()+")。专场延期失败");
            result.setResult(false);
            return  result;
        }
        modelCmsBtJmPromotion.setActivityEnd(parameter.getDealEndTime());
        daoCmsBtJmPromotion.update(modelCmsBtJmPromotion);
        daoExt.updateDealEndTimeAll(parameter);//商品改变延期状态
        return result;
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
        CmsBtJmPromotionProductModel model = dao.select(promotionProductId);
        int ShippingSystemId = serviceCmsMtJmConfig.getShippingSystemId(model.getChannelId());
        ShopBean shopBean = serviceJMShopBean.getShopBean(model.getChannelId());
        // if (model.getSynchState() == 0 || model.getSynchState() == 1 || model.getSynchState() == 4) {
        CallResult result = serviceJuMeiProductPlatform.updateJm(model, shopBean, ShippingSystemId);
        // }
        return result;
    }

}

