package com.voyageone.service.impl.cms.jumei;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.bean.cms.businessmodel.ProductIdListInfo;
import com.voyageone.service.bean.cms.businessmodel.PromotionProduct.ParameterUpdateDealEndTimeAll;
import com.voyageone.service.dao.cms.*;
import com.voyageone.service.daoext.cms.*;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionTagProductModel;
import com.voyageone.service.model.cms.CmsBtTagModel;
import com.voyageone.service.model.util.MapModel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

/**
 * Created by dell on 2016/3/18.
 *
 * @version 2.8.0
 */
@Service
public class CmsBtJmPromotionProductService extends BaseService {
    private final CmsBtJmPromotionProductDao dao;
    private final CmsBtJmPromotionProductDaoExt daoExt;
    private final CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;
    private final CmsBtJmPromotionDao daoCmsBtJmPromotion;
    private final CmsBtJmPromotionTagProductDao cmsBtJmPromotionTagProductDao;
    private final CmsBtTagDao cmsBtTagDao;

    @Autowired
    public CmsBtJmPromotionProductService(CmsBtJmPromotionProductDao dao,
                                          CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku,
                                          CmsBtJmPromotionProductDaoExt daoExt,
                                          CmsBtJmPromotionDao daoCmsBtJmPromotion,
                                          CmsBtJmPromotionTagProductDao cmsBtJmPromotionTagProductDao,
                                          CmsBtTagDao cmsBtTagDao) {
        this.dao = dao;
        this.daoExtCmsBtJmPromotionSku = daoExtCmsBtJmPromotionSku;
        this.daoExt = daoExt;
        this.daoCmsBtJmPromotion = daoCmsBtJmPromotion;
        this.cmsBtJmPromotionTagProductDao = cmsBtJmPromotionTagProductDao;
        this.cmsBtTagDao = cmsBtTagDao;
    }

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
        loadWhere(map);
        // 根据sortOneName 和 sortOneType设置排序条件
        String sortOneName = (String) map.get("sortOneName");
        String sortOneType = (String) map.get("sortOneType");
        $info(String.format("聚美活动商品查询排序(%s)-(%s)", sortOneName, sortOneType));
        List<MapModel> list = daoExt.selectPageByWhere(map);
        list.forEach(this::setTagNames);
        return list;
    }

    public List<MapModel> getByWhere(Map<String, Object> map) {
        loadWhere(map);
        List<MapModel> list = daoExt.selectPageByWhere(map);
        return list;
    }

    /**
     * Jonas 修改
     * @since 2.8.0
     */
    private void setTagNames(MapModel map) {
        CmsBtJmPromotionTagProductModel parameter = new CmsBtJmPromotionTagProductModel();
        parameter.setCmsBtJmPromotionProductId(Integer.valueOf(map.get("id").toString()));
        List<CmsBtJmPromotionTagProductModel> cmsBtJmPromotionTagProductModelList = cmsBtJmPromotionTagProductDao.selectList(parameter);
        List<String> tagNameList = cmsBtJmPromotionTagProductModelList
                .stream()
                .map(CmsBtJmPromotionTagProductModel::getCmsBtTagId)
                .map(cmsBtTagDao::select)
                .filter(model -> model != null)
                .map(CmsBtTagModel::getTagName)
                .collect(toList());
        map.put("tagNameList",tagNameList);
    }

    public  void  loadWhere(Map<String, Object> map)
    {
        if (map.containsKey("code")) {
            String code = map.get("code").toString();
            String[] codeList = code.split("\r\n|\n");//split("\r\n");
            map.put("codeList", codeList);
        }
    }
    public int getCountByWhere(Map<String, Object> map) {
        loadWhere(map);
        return daoExt.selectCountByWhere(map);
    }

    public int delete(int id) {
        CmsBtJmPromotionProductModel modelProduct = dao.select(id);
        if (modelProduct.getSynchStatus() == 2) {
            return 0;
        }
        return dao.delete(id);
    }

//    @VOTransactional
//    public int updateDealPrice(BigDecimal dealPrice, int id, String userName) {
//        CmsBtJmPromotionProductModel model = dao.select(id);
//        model.setDealPrice(dealPrice);
//        model.setModifier(userName);
//        dao.update(model);
//        return daoExtCmsBtJmPromotionSku.updateDealPrice(dealPrice, model.getId());
//    }

    //    @VOTransactional
//    public void deleteByPromotionId(int jmPromotionId) {
//        daoExt.deleteByPromotionId(jmPromotionId);
//        daoExtCmsBtJmPromotionSku.deleteByPromotionId(jmPromotionId);
//        CmsBtPromotionModel modelCmsBtPromotion = getCmsBtPromotionModel(jmPromotionId);
//        if (modelCmsBtPromotion != null) {
//            daoExtCamelCmsBtPromotionCodes.deleteByPromotionId(modelCmsBtPromotion.getId());
//            daoExtCamelCmsBtPromotionGroups.deleteByPromotionId(modelCmsBtPromotion.getId());
//            daoExtCamelCmsBtPromotionSkus.deleteByPromotionId(modelCmsBtPromotion.getId());
//        }
//    }
    @VOTransactional
    public void deleteByProductIdList(ProductIdListInfo parameter) {
        daoExt.deleteByProductIdListInfo(parameter);
        daoExtCmsBtJmPromotionSku.deleteByProductIdListInfo(parameter);
    }

    //    public CmsBtPromotionModel getCmsBtPromotionModel(int jmPromotionId)
//    {
//        Map<String, Object> map = new HashMap<>();
//        map.put("promotionId",jmPromotionId);
//        map.put("cartId", CartEnums.Cart.JM.getValue());
//        CmsBtPromotionModel promotion = daoCmsBtPromotion.selectOne(map);
//        return  promotion;
//    }
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

        if (modelCmsBtJmPromotion.getIsPromotionFullMinus())//该专场为 满减专场的场合
        {
            result.setMsg("该专场为满减专场不允许延期");
            result.setResult(false);
            return result;
        }
//        if(cms_bt_jm_promotion.is_promotion_full_minus) 该专场为 满减专场的场合
//        {
//            errorMsg="满减专场不允许延期"；
//        }
        modelCmsBtJmPromotion.setActivityEnd(parameter.getDealEndTime());
        daoCmsBtJmPromotion.update(modelCmsBtJmPromotion);
        daoExt.updateDealEndTimeAll(parameter);//商品改变延期状态
        return result;
    }

//    //部分商品延期
//    public int updateDealEndTime(ParameterUpdateDealEndTime parameter) {
//        return daoExt.updateDealEndTime(parameter);
//    }

    // 根据条件检索出promoiton的product数据
    public CmsBtJmPromotionProductModel selectOne(Map<String, Object> param) {
        return dao.selectOne(param);
    }

//    public CallResult updateJM(@RequestBody int promotionProductId) throws Exception {
//        CmsBtJmPromotionProductModel model = dao.select(promotionProductId);
//        int ShippingSystemId = serviceCmsMtJmConfig.getShippingSystemId(model.getChannelId());
//        ShopBean shopBean = serviceJMShopBean.getShopBean(model.getChannelId());
//        // if (model.getSynchState() == 0 || model.getSynchState() == 1 || model.getSynchState() == 4) {
//        CallResult result = serviceJuMeiProductPlatform.updateJm(model, shopBean, ShippingSystemId);
//        // }
//        return result;
//    }

}

