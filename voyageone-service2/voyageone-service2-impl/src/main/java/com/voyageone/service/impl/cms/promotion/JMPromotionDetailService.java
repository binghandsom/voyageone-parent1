package com.voyageone.service.impl.cms.promotion;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.service.bean.cms.CmsBtPromotionSkuBean;
import com.voyageone.service.bean.cms.PromotionDetailAddBean;
import com.voyageone.service.bean.cms.jumei.BatchUpdateSkuPriceParameterBean;
import com.voyageone.service.bean.cms.jumei.ProductSaveInfo;
import com.voyageone.service.bean.cms.jumei.SkuImportBean;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionSkuDao;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionProductDaoExt;
import com.voyageone.service.daoext.cms.CmsBtJmPromotionSkuDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionSku3Service;
import com.voyageone.service.impl.cms.jumei2.CmsBtJmPromotionTagProductService;
import com.voyageone.service.model.cms.CmsBtJmPromotionModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductGroupModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2016/11/7.
 */
@Service
public class JMPromotionDetailService extends BaseService {

    @Autowired
    CmsBtJmPromotionProductDao dao;

    @Autowired
    CmsBtJmPromotionProductDaoExt daoext;

    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmPromotionSkuDaoExt daoExtCmsBtJmPromotionSku;

    @Autowired
    CmsBtJmPromotionSku3Service cmsBtJmPromotionSku3Service;

    @Autowired
    CmsBtJmPromotionTagProductService cmsBtJmPromotionTagProductService;

    @VOTransactional
    public void addPromotionDetail(PromotionDetailAddBean bean, CmsBtJmPromotionModel jmPromotionModel, String modifier, CmsBtProductModel productInfo) {

        //1.初始化 JmPromotionProduct
        CmsBtJmPromotionProductModel jmProductModel = loadJmPromotionProduct(bean, jmPromotionModel, modifier, productInfo);

        //2.初始化 JmPromotionSku
        List<CmsBtJmPromotionSkuModel> listPromotionSku = loadJmPromotionSkus(bean, jmProductModel, productInfo, modifier);

        //3.计算活动价格
        BatchUpdateSkuPriceParameterBean parameter = new BatchUpdateSkuPriceParameterBean();
        parameter.setJmPromotionId(jmPromotionModel.getId());
        parameter.setOptType(bean.getAddProductSaveParameter().getOptType());
        parameter.setPriceTypeId(bean.getAddProductSaveParameter().getPriceTypeId());
        parameter.setPriceValue(bean.getAddProductSaveParameter().getPriceValue());
        parameter.setRoundType(bean.getAddProductSaveParameter().getRoundType());
        parameter.setSkuUpdType(bean.getAddProductSaveParameter().getSkuUpdType());
        cmsBtJmPromotionSku3Service.UpdateSkuDealPrice(parameter, listPromotionSku, modifier);

        // 保存 JmPromotionProduct
        if (jmProductModel.getId() != null && jmProductModel.getId() > 0) {
            dao.update(jmProductModel);
        } else {
            dao.insert(jmProductModel);
        }

        // 保存 JmPromotionSku
        listPromotionSku.forEach(f->{
            if(f.getId()!=null&&f.getId()>0)
            {
                daoCmsBtJmPromotionSku.update(f);
            }
            else
            {
                daoCmsBtJmPromotionSku.insert(f);
            }
        });

        //更新 tag
        cmsBtJmPromotionTagProductService.updateJmPromotionTagProduct(bean.getTagList(), jmPromotionModel.getChannelId(), jmProductModel.getId(), modifier);

    }

    CmsBtJmPromotionProductModel loadJmPromotionProduct(PromotionDetailAddBean bean, CmsBtJmPromotionModel jmPromotionModel, String userName, CmsBtProductModel productInfo)
    {
        CmsBtJmPromotionProductModel jmProductModel = daoext.selectByProductCode(bean.getProductCode(), jmPromotionModel.getChannelId(), jmPromotionModel.getId());
        if (jmProductModel == null) {
            jmProductModel = new CmsBtJmPromotionProductModel();
            jmProductModel.setId(0);
            jmProductModel.setCreater(userName);
            jmProductModel.setCreated(new Date());
            jmProductModel.setJmHashId("");
//            if (!com.voyageone.common.util.StringUtils.isEmpty(product.getErrorMsg())) {
//                jmProductModel.setErrorMsg(product.getErrorMsg());
//            } else {
//                jmProductModel.setErrorMsg("");
//            }
            jmProductModel.setPriceStatus(0);
            jmProductModel.setDiscount(new BigDecimal(0));
            jmProductModel.setSkuCount(0);
            jmProductModel.setQuantity(0);
            jmProductModel.setDealEndTimeStatus(0);
            jmProductModel.setActivityStart(jmPromotionModel.getActivityStart());
            jmProductModel.setActivityEnd(jmPromotionModel.getActivityEnd());
            jmProductModel.setProductCode(bean.getProductCode());
            jmProductModel.setCmsBtJmPromotionId(jmPromotionModel.getId());
            jmProductModel.setChannelId(jmPromotionModel.getChannelId());
            jmProductModel.setSynchStatus(0);
            jmProductModel.setLimit(0);
            jmProductModel.setUpdateStatus(0);
            jmProductModel.setPromotionTag("");
            jmProductModel.setProductNameEn(productInfo.getCommon().getFields().getProductNameEn());
            if (productInfo.getCommon().getFields().getImages1() != null && productInfo.getCommon().getFields().getImages1().size() > 0) {
                if (productInfo.getCommon().getFields().getImages1().get(0).get("image1") != null) {
                    jmProductModel.setImage1(productInfo.getCommon().getFields().getImages1().get(0).get("image1").toString());
                }
            }
        }
        jmProductModel.setAppId(0L);
        jmProductModel.setPcId(0L);
//        if (jmProductModel.getSynchStatus() == 2) {
//            if (product.getLimit() !=jmProductModel.getLimit()) {
//               jmProductModel.setUpdateStatus(1);//已经变更
//            }
//        }
        //jmProductModel.setLimit(product.getLimit());
        //jmProductModel.setPromotionTag(product.getPromotionTag());
        jmProductModel.setModifier(userName);
        jmProductModel.setModified(new Date());
        StringBuilder sbPromotionTag = new StringBuilder();
        bean.getTagList().forEach(f -> sbPromotionTag.append("|").append(f.getName()));
        if (sbPromotionTag.length() > 0) {
            jmProductModel.setPromotionTag(sbPromotionTag.substring(1));
        }
        return  jmProductModel;
    }

    List<CmsBtJmPromotionSkuModel>   loadJmPromotionSkus(PromotionDetailAddBean bean, CmsBtJmPromotionProductModel jmProductModel,CmsBtProductModel productInfo, String modifier) {
        List<CmsBtProductModel_Sku> skusList = productInfo.getCommonNotNull().getSkus();
        List<BaseMongoMap<String, Object>> listSkuMongo = productInfo.getPlatform(bean.getCartId()).getSkus();
        List<CmsBtJmPromotionSkuModel> listPromotionSku = new ArrayList<>();
        skusList.forEach(sku -> {
            Double priceMsrp = 0d;
            Double priceRetail = 0d;
            Double priceSale = 0d;
            Double msrpUsd=9d;
            BaseMongoMap<String, Object> mapSkuPlatform = getJMPlatformSkuMongo(listSkuMongo, sku.getSkuCode());
            if (mapSkuPlatform != null) {
                priceMsrp = mapSkuPlatform.getDoubleAttribute("priceMsrp");
                priceRetail = mapSkuPlatform.getDoubleAttribute("priceRetail");
                priceSale = mapSkuPlatform.getDoubleAttribute("priceSale");
            }
            CmsBtProductModel_Sku cmsBtProductModel_sku = productInfo.getCommon().getSku(sku.getSkuCode());
            if (cmsBtProductModel_sku != null) {
                msrpUsd = cmsBtProductModel_sku.getClientMsrpPrice();
            }
            CmsBtJmPromotionSkuModel skuModel = null;
            if (jmProductModel.getId() != null && jmProductModel.getId() > 0) {
                skuModel = daoExtCmsBtJmPromotionSku.selectBySkuCode(sku.getSkuCode(), jmProductModel.getId(), jmProductModel.getCmsBtJmPromotionId());
            }
            if (skuModel == null) {
                skuModel = new CmsBtJmPromotionSkuModel();
                skuModel.setSynchStatus(0);
                skuModel.setUpdateState(0);
                skuModel.setCmsBtJmPromotionId(jmProductModel.getCmsBtJmPromotionId());
                skuModel.setChannelId(jmProductModel.getChannelId());
                skuModel.setSkuCode(sku.getSkuCode());
                skuModel.setCreated(new Date());
                skuModel.setCreater(modifier);
                skuModel.setProductCode(jmProductModel.getProductCode());
                skuModel.setErrorMsg("");
                if (jmProductModel.getSynchStatus() == 2) {
                    skuModel.setUpdateState(1);//已变更
                    jmProductModel.setUpdateStatus(1);//已变更     新增了一个sku
                }
            }
//            if (jmProductModel.getSynchStatus() == 2) {
//                if (skuModel.getDealPrice().doubleValue() != skuImportBean.getDealPrice()) {
//                    skuModel.setUpdateState(1);//已变更
//                    saveInfo.jmProductModel.setUpdateStatus(1);//已变更
//                }
//                if (skuModel.getMarketPrice().doubleValue() != skuImportBean.getMarketPrice()) {
//                    skuModel.setUpdateState(1);//已变更
//                    saveInfo.jmProductModel.setUpdateStatus(1);//已变更
//                }
//            }

            skuModel.setMsrpRmb(new BigDecimal(priceMsrp));
            skuModel.setRetailPrice(new BigDecimal(priceRetail));
            skuModel.setSalePrice(new BigDecimal(priceSale));
            skuModel.setMsrpUsd(new BigDecimal(msrpUsd));

            skuModel.setDealPrice(new BigDecimal(0));
            skuModel.setMarketPrice(new BigDecimal(priceRetail));
            skuModel.setDiscount(BigDecimalUtil.divide(skuModel.getDealPrice(), skuModel.getMarketPrice(), 2));//折扣
            skuModel.setModified(new Date());
            skuModel.setModifier(modifier);
            listPromotionSku.add(skuModel);
        });
        return listPromotionSku;
    }
    private BaseMongoMap<String, Object>  getJMPlatformSkuMongo(List<BaseMongoMap<String, Object>> list,String skuCode)
    {
        for(BaseMongoMap<String, Object> map:list)
        {
            if(skuCode.equalsIgnoreCase(map.getStringAttribute("skuCode")))
            {
                return  map;
            }
        }
        return null;
    }
}
