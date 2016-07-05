package com.voyageone.service.impl.cms.jumei.platform;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.bean.JmProductBean;
import com.voyageone.components.jumei.bean.JmProductBean_DealInfo;
import com.voyageone.components.jumei.bean.JmProductBean_Spus;
import com.voyageone.components.jumei.bean.JmProductBean_Spus_Sku;
import com.voyageone.components.jumei.enums.EnumJuMeiMtMasterInfo;
import com.voyageone.components.jumei.enums.EnumJuMeiProductImageType;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.bean.cms.CallResult;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.model.cms.*;
import com.voyageone.service.bean.cms.businessmodel.JMNewProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductAddPlatformService {
    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";
    private static final String DESCRIPTION_USAGE = "<div>%s %s <br /></div>";
    private static final String DESCRIPTION_IMAGES = "%s<br />";
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductAddPlatformService.class);
    @Autowired
    JuMeiProductUpdateService service;
    @Autowired
    JumeiProductService serviceJumeiProduct;

    @Autowired
    JumeiHtDealService serviceJumeiHtDeal;
    @Autowired
    JumeiHtSpuService serviceJumeiHtSpu;
    @Autowired
    JumeiHtSkuService serviceJumeiHtSku;
    @Autowired
    JuMeiUploadImageService serviceJuMeiUploadImageJob;
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProductDao;
    @Autowired
    JMShopBeanService serviceJMShopBean;
    @Autowired
    JuMeiProductUpdatePlatformService serviceJuMeiProductUpdatePlatform;
    //活动上新
//    public void addProductAndDealByPromotionId(int promotionId) throws Exception {
//        ShopBean shopBean = serviceJMShopBean.getShopBean();
//        LOG.info(promotionId + " 聚美上新开始");
//        CmsBtJmPromotionModel modelCmsBtJmPromotion = service.getCmsBtJmPromotion(promotionId);
//        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = service.getJuMeiNewListPromotionProduct(promotionId);
//        int shippingSystemId = service.getShippingSystemId(modelCmsBtJmPromotion.getChannelId());
//        try {
//            for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
//                try {
//                    if (model.getState() == 0) {//上新
//                        CallResult result = addProductAndDeal(modelCmsBtJmPromotion, shippingSystemId, model, shopBean);//上新
//                        if (!result.isResult()) {
//                            model.setErrorMsg(result.getMsg());
//                            model.setSynchState(EnumJuMeiSynchState.Error.getId());//同步更新失败
//                            daoCmsBtJmPromotionProduct.update(model);
//                        }
//                    } else //更新 copyDeal
//                    {
//                        serviceJuMeiProductUpdatePlatefrom.updateProductAddDeal(modelCmsBtJmPromotion, shippingSystemId, model, shopBean);////更新 copyDeal
//                    }
//                } catch (Exception ex) {
//                    model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
//                    model.setSynchState(EnumJuMeiSynchState.Error.getId());//同步更新失败
//                    LOG.error("addProductAndDealByPromotionId", ex);
//                    try {
//                        if(model.getErrorMsg().length()>200) {
//                            model.setErrorMsg(model.getErrorMsg().substring(0, 200));
//                        }
//                        daoCmsBtJmPromotionProduct.update(model);
//                    } catch (Exception cex) {
//                        LOG.error("addProductAndDealByPromotionId", cex);
//                        ex.printStackTrace();
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            LOG.error("addProductAndDealByPromotionId上新失败", ex);
//            ex.printStackTrace();
//        }
//        LOG.info(promotionId + " 聚美上新end");
//    }

    //上新
    public CallResult addProductAndDeal(int shippingSystemId, CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        JMNewProductInfo updateInfo = service.getJMNewProductInfo(model);
        updateInfo.loadData();
        CallResult result = checkJMNewProductInfo(updateInfo);//验证
        if (!result.isResult()) {
            return result;
        }
        JmProductBean jmProductBean = selfBeanToJmBean(updateInfo, shippingSystemId);
        for (CmsBtJmProductImagesModel imgemodel : updateInfo.getListCmsBtJmProductImages()) {
            if (imgemodel.getSynFlg() == 0) { //上传图片
                serviceJuMeiUploadImageJob.uploadImage(imgemodel, shopBean);
                imgemodel.setSynFlg(1);//已上传
                service.saveCmsBtJmProductImages(imgemodel);
            }
        }
        for (CmsMtMasterInfoModel modelCmsMtMasterInfo : updateInfo.getListCmsMtMasterInfo()) {
            if (modelCmsMtMasterInfo.getSynFlg() == 0 && modelCmsMtMasterInfo.getDataType() != 3) { //上传图片
                modelCmsMtMasterInfo.setSynFlg(1);//已上传
                serviceJuMeiUploadImageJob.uploadImage(modelCmsMtMasterInfo, shopBean);
                service.saveCmsMtMasterInfo(modelCmsMtMasterInfo);
            }
        }
        setImages(updateInfo, jmProductBean);
        serviceJumeiProduct.productNewUpload(shopBean, jmProductBean);//上新
       // updateInfo.getModelCmsBtJmProduct().setState(1);//已经上新
        updateInfo.getModelCmsBtJmProduct().setJumeiProductId(jmProductBean.getJumei_product_id());
        //updateInfo.getModelCmsBtJmProduct().setLastJmHashId(jmProductBean.getDealInfo().getJumei_hash_id());//保存最后一次活动JmHashId
        updateInfo.getModelCmsBtJmPromotionProduct().setJmHashId(jmProductBean.getDealInfo().getJumei_hash_id());
       // updateInfo.getModelCmsBtJmPromotionProduct().setState(1);//已经上新
      //  updateInfo.getModelCmsBtJmPromotionProduct().setSynchState(2);//更新成功
      //  updateInfo.getModelCmsBtJmPromotionProduct().setUpdateState(2);
        for (JmProductBean_Spus spu : jmProductBean.getSpus()) {
            String skuCode = spu.getSkuInfo().getPartner_sku_no();
            CmsBtJmSkuModel cmsBtJmSkuModel = updateInfo.getMapCodeCmsBtJmSkuModel().get(skuCode);
            cmsBtJmSkuModel.setJmSkuNo(spu.getSkuInfo().getJumei_sku_no());
            cmsBtJmSkuModel.setJmSpuNo(spu.getJumei_spu_no());
         //   cmsBtJmSkuModel.setState(1);
            updateInfo.getMapSkuIdCmsBtJmPromotionSkuModel().get(cmsBtJmSkuModel.getId());
           // CmsBtJmPromotionSkuModel promotionSkuModel = updateInfo.getMapSkuIdCmsBtJmPromotionSkuModel().get(cmsBtJmSkuModel.getId());
           // promotionSkuModel.setState(1);//已经上新
           // promotionSkuModel.setSynchState(2);//上新更新成功
        }
        service.saveJMNewProductUpdateInfo(updateInfo);
        return result;
    }

    public CallResult checkJMNewProductInfo(JMNewProductInfo updateInfo) {
        CallResult result = new CallResult();
        StringBuilder sb = new StringBuilder();
        //4：品牌故事图 ；5：尺码图； 6：物流介绍）
        boolean hasdataType4 = false;
        boolean hasdataType5 = false;
        boolean hasdataType6 = false;
        for (CmsMtMasterInfoModel modelCmsMtMasterInfo : updateInfo.getListCmsMtMasterInfo()) {
            if (modelCmsMtMasterInfo.getDataType() == 4) {
                if (!StringUtils.isEmpty(modelCmsMtMasterInfo.getValue1())) {
                    hasdataType4 = true;
                }
            } else if (modelCmsMtMasterInfo.getDataType() == 5) {
                if (!StringUtils.isEmpty(modelCmsMtMasterInfo.getValue1())) {
                    hasdataType5 = true;
                }

            } else if (modelCmsMtMasterInfo.getDataType() == 6) {
                if (!StringUtils.isEmpty(modelCmsMtMasterInfo.getValue1())) {
                    hasdataType6 = true;
                }
            }
        }
        if (!hasdataType4) {
            sb.append("品牌故事图不存在");
        }
        if (!hasdataType5) {
            sb.append("尺码图不存在");
        }
        if (!hasdataType6) {
            sb.append("物流介绍图不存在");
        }
        // CmsBtJmProductImagesModel modelCmsBtJmProductImages=updateInfo.getListCmsBtJmProductImages();
        //  modelCmsBtJmProductImages.
        if (sb.length() > 0) {
            result.setResult(false);
            result.setMsg(sb.toString());
        }
        return result;
    }
    private JmProductBean selfBeanToJmBean(JMNewProductInfo info,int shippingSystemId) throws Exception {
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        String partner_sku_nos = "";
        JmProductBean jmProductBean = new JmProductBean();
        jmProductBean.setName(modelProduct.getProductNameCn());//jmBtProductImport.getProductName());
        jmProductBean.setProduct_spec_number(modelProduct.getProductCode());//jmBtProductImport.getProductCode());
      //  jmProductBean.setCategory_v3_4_id(modelProduct.getCategoryLv4Id());//jmBtProductImport.getCategoryLv4Id());
       // jmProductBean.setBrand_id(modelProduct.getBrandId());//jmBtProductImport.getBrandId());
        jmProductBean.setForeign_language_name(modelProduct.getForeignLanguageName());//jmBtProductImport.getForeignLanguageName());
        // Todo
        // sku
        List<JmProductBean_Spus> spus = new ArrayList<>();
        jmProductBean.setSpus(spus);
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {//.getSkuImportModelList()) {
          //  CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
            JmProductBean_Spus spu = new JmProductBean_Spus();
           // spu.setPartner_spu_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
          //  spu.setUpc_code(modelSku.getUpc());//jmBtSkuImportModel.getUpcCode());
            spu.setPropery("OTHER");
         //   spu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
            spu.setAttribute(modelProduct.getAttribute());//jmBtProductImport.getAttribute());
        //    spu.setAbroad_price(modelProduct.getMsrp().doubleValue());//jmBtSkuImportModel.getAbroadPrice());
            // todo 价格单位
            spu.setArea_code("19");
            JmProductBean_Spus_Sku sku = new JmProductBean_Spus_Sku();
          //  sku.setPartner_sku_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
            sku.setSale_on_this_deal("1");
          //  sku.setBusinessman_num(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
            sku.setStocks("1");
            sku.setDeal_price(modelPromotionSku.getDealPrice().toString());//jmBtSkuImportModel.getDealPrice().toString());
            sku.setMarket_price(modelPromotionSku.getMarketPrice().toString());//jmBtSkuImportModel.getMarketPrice().toString());
            if (modelPromotionSku.getDealPrice().doubleValue() < 1 || modelPromotionSku.getMarketPrice().doubleValue() < 1) {
                throw new BusinessException("价格为0");
            }
            spu.setSkuInfo(sku);
            spus.add(spu);
           // partner_sku_nos += modelSku.getSkuCode() + ","; //jmBtSkuImportModel.getSku() + ",";
        }
        //dealinfo
        // JmBtDealImportModel jmBtDealImportModel = jmBtProductImport.getJmBtDealImportModel();
        JmProductBean_DealInfo jmProductBean_DealInfo = new JmProductBean_DealInfo();
        jmProductBean_DealInfo.setPartner_deal_id(modelProduct.getProductCode() + "-" + info.getModelCmsBtJmPromotionProduct().getCmsBtJmPromotionId());//jmBtDealImportModel.getProductCode() + "-" + jmBtDealImportModel.getDealId());
        jmProductBean_DealInfo.setStart_time(getTime(modelPromotionProduct.getActivityStart()));//getTime(jmBtDealImportModel.getStartTime()));
        jmProductBean_DealInfo.setEnd_time(getTime(modelPromotionProduct.getActivityEnd()));//jmBtDealImportModel.getEndTime()));
        jmProductBean_DealInfo.setUser_purchase_limit(modelPromotionProduct.getLimit());//jmBtDealImportModel.getUserPurchaseLimit());
        jmProductBean_DealInfo.setShipping_system_id(shippingSystemId);//jmBtDealImportModel.getShippingSystemId());
      //  jmProductBean_DealInfo.setProduct_long_name(modelProduct.getProductLongName());//jmBtDealImportModel.getProductLongName());
      //  jmProductBean_DealInfo.setProduct_medium_name(modelProduct.getProductMediumName());//jmBtDealImportModel.getProductMediumName());
     //   jmProductBean_DealInfo.setProduct_short_name(modelProduct.getProductShortName());//jmBtDealImportModel.getProductShortName());
        jmProductBean_DealInfo.setAddress_of_produce(modelProduct.getAddressOfProduce());//jmBtProductImport.getAddressOfProduce());
        jmProductBean_DealInfo.setBefore_date("无");
        jmProductBean_DealInfo.setSuit_people("时尚潮流人士");
      //  jmProductBean_DealInfo.setSearch_meta_text_custom(modelProduct.getSearchMetaTextCustom());//jmBtDealImportModel.getSearchMetaTextCustom());

        // 特殊说明
     //   jmProductBean_DealInfo.setSpecial_explain(modelProduct.getSpecialNote());//jmBtProductImport.getSpecialNote());
        if (partner_sku_nos.length() > 0) {
            jmProductBean_DealInfo.setPartner_sku_nos(partner_sku_nos.substring(0, partner_sku_nos.length() - 1));
        }

        jmProductBean.setDealInfo(jmProductBean_DealInfo);
        return jmProductBean;
    }

    private void setImages(JMNewProductInfo info, JmProductBean jmProductBean) {
        //   Map<Integer, List<JmPicBean>> imagesMap = null;//jmUploadProductDao.selectImageByCode(jmBtProductImport.getChannelId(), jmBtProductImport.getProductCode(), jmBtProductImport.getBrandName(), jmBtProductImport.getSizeType());
        // （1:宝贝图（白底方图）；2:；详情图（商品实拍图）；3：移动端宝贝图（竖图））
        StringBuffer stringBuffer = new StringBuffer();
        // List<JmPicBean> pics = imagesMap.get(JumeiImageType.NORMAL.getId());
        List<CmsBtJmProductImagesModel> listNormal = getListCmsBtJmProductImages(info.getListCmsBtJmProductImages(), EnumJuMeiProductImageType.NORMAL.getId());
        //白底方图
        if (!listNormal.isEmpty()) {
            for (CmsBtJmProductImagesModel jmPicBean : listNormal) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(jmPicBean.getJmUrl());
            }
            ;
        } else {
            throw new BusinessException("白底方图不存在");
        }
        jmProductBean.setNormalImage(stringBuffer.toString());

        // 竖图
        stringBuffer = new StringBuffer();
        //  pics = imagesMap.get(JumeiImageType.VERTICAL.getId());
        List<CmsBtJmProductImagesModel> listVERTICAL = getListCmsBtJmProductImages(info.getListCmsBtJmProductImages(), EnumJuMeiProductImageType.VERTICAL.getId());
        if (!listVERTICAL.isEmpty()) {
            for (CmsBtJmProductImagesModel jmPicBean : listVERTICAL) {
                if (stringBuffer.length() != 0) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(jmPicBean.getJmUrl());
            }
        }
        if (stringBuffer.length() > 0) {
            jmProductBean.setVerticalImage(stringBuffer.toString());
        }

        // 产品详细
        stringBuffer = new StringBuffer();
        //pics = imagesMap.get(JumeiImageType.PRODUCT.getId());
        List<CmsBtJmProductImagesModel> listPRODUCT = getListCmsBtJmProductImages(info.getListCmsBtJmProductImages(), EnumJuMeiProductImageType.PRODUCT.getId());
        if (!listPRODUCT.isEmpty()) {
            for (CmsBtJmProductImagesModel jmPicBean : listPRODUCT) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        } else {
            throw new BusinessException("产品图不存在");
        }

        //参数图
        stringBuffer = new StringBuffer();
        // pics = imagesMap.get(JumeiImageType.PARAMETER.getId());
        List<CmsBtJmProductImagesModel> listPARAMETER = getListCmsBtJmProductImages(info.getListCmsBtJmProductImages(), EnumJuMeiProductImageType.PARAMETER.getId());
        if (!listPARAMETER.isEmpty()) {
            for (CmsBtJmProductImagesModel jmPicBean : listPARAMETER) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        }


        // 品牌图
        stringBuffer = new StringBuffer();
        // pics = imagesMap.get(JumeiImageType.BRANDSTORY.getId());
        List<CmsMtMasterInfoModel> listBRANDSTORY = getListCmsMtMasterInfoModel(info.getListCmsMtMasterInfo(), EnumJuMeiMtMasterInfo.BRANDSTORY.getId());
        if (!listBRANDSTORY.isEmpty()) {
            for (CmsMtMasterInfoModel jmPicBean : listBRANDSTORY) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue2()));
            }
        } else {
            throw new BusinessException("品牌图不存在");
        }
        jmProductBean.getDealInfo().setDescription_properties(stringBuffer.toString());


        //尺码表
        // pics = imagesMap.get(JumeiImageType.SIZE.getId());
        List<CmsMtMasterInfoModel> listSIZE = getListCmsMtMasterInfoModel(info.getListCmsMtMasterInfo(), EnumJuMeiMtMasterInfo.SIZE.getId());
        if (!listSIZE.isEmpty()) {
            for (CmsMtMasterInfoModel jmPicBean : listSIZE) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue2()));
            }
        } else {
            //if(!jmBtProductImport.getSizeType().equalsIgnoreCase("One Size") && !jmBtProductImport.getSizeType().equalsIgnoreCase("OneSize")){
            String SizeType = info.getModelCmsBtJmProduct().getSizeType();
            if (!"One Size".equalsIgnoreCase(SizeType) && !"OneSize".equalsIgnoreCase(SizeType)) {
                throw new BusinessException("尺码图不存在");
            }
        }
        jmProductBean.getDealInfo().setDescription_usage(String.format(DESCRIPTION_USAGE, info.getModelCmsBtJmProduct().getProductDesCn(), stringBuffer.toString()));//.getProductDes()
        //物流图
        //pics = imagesMap.get(JumeiImageType.LOGISTICS.getId());
        List<CmsMtMasterInfoModel> lisLOGISTICS = getListCmsMtMasterInfoModel(info.getListCmsMtMasterInfo(), EnumJuMeiMtMasterInfo.LOGISTICS.getId());
        if (!lisLOGISTICS.isEmpty()) {
            for (CmsMtMasterInfoModel jmPicBean : lisLOGISTICS) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue2()));
            }
        } else {
            throw new BusinessException("物流图不存在");
        }
        jmProductBean.getDealInfo().setDescription_images(String.format(DESCRIPTION_IMAGES, stringBuffer.toString()));
    }

    public List<CmsBtJmProductImagesModel> getListCmsBtJmProductImages(List<CmsBtJmProductImagesModel> list, int image_type) {
        List<CmsBtJmProductImagesModel> result = new ArrayList<>();
        for (CmsBtJmProductImagesModel model : list) {
            if (model.getImageType() == image_type) {
                result.add(model);
            }
        }
        return result;
    }

    public List<CmsMtMasterInfoModel> getListCmsMtMasterInfoModel(List<CmsMtMasterInfoModel> list, int dataType) {
        List<CmsMtMasterInfoModel> result = new ArrayList<>();
        for (CmsMtMasterInfoModel model : list) {
            if (model.getDataType() == dataType) {
                result.add(model);
            }
        }
        return result;
    }

    private static Long getTime(Date d) throws Exception {
        return d.getTime() / 1000 - 8 * 3600;
    }

}
