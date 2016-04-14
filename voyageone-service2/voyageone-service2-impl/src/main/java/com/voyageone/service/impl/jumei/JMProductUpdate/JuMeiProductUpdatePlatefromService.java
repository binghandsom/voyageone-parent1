package com.voyageone.service.impl.jumei.JMProductUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.Reponse.HtDealCopyDealResponse;
import com.voyageone.components.jumei.Reponse.HtSkuAddResponse;
import com.voyageone.components.jumei.Reponse.HtSpuAddResponse;
import com.voyageone.components.jumei.Request.HtDealCopyDealRequest;
import com.voyageone.components.jumei.Request.HtSkuAddRequest;
import com.voyageone.components.jumei.Request.HtSpuAddRequest;
import com.voyageone.components.jumei.bean.JmProductBean;
import com.voyageone.components.jumei.bean.JmProductBean_DealInfo;
import com.voyageone.components.jumei.bean.JmProductBean_Spus;
import com.voyageone.components.jumei.bean.JmProductBean_Spus_Sku;
import com.voyageone.components.jumei.enums.EnumJuMeiMtMasterInfo;
import com.voyageone.components.jumei.enums.EnumJuMeiProductImageType;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.dao.jumei.CmsBtJmProductDao;
import com.voyageone.service.dao.jumei.CmsBtJmPromotionProductDao;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.EnumJuMeiSynchState;
import com.voyageone.service.model.jumei.businessmodel.JMNewProductInfo;
import com.voyageone.service.model.jumei.businessmodel.JMUpdateProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductUpdatePlatefromService {
    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";
    private static final String DESCRIPTION_USAGE = "<div>%s %s <br /></div>";
    private static final String DESCRIPTION_IMAGES = "%s<br />";
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductUpdatePlatefromService.class);
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
    //活动上新
    public void addProductAndDealByPromotionId(int promotionId) throws Exception {
        ShopBean shopBean = service.getShopBean();
        LOG.info(promotionId + " 聚美上新开始");
        CmsBtJmPromotionModel modelCmsBtJmPromotion = service.getCmsBtJmPromotion(promotionId);
        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = service.getJuMeiNewListPromotionProduct(promotionId);
        int shippingSystemId = service.getShippingSystemId(modelCmsBtJmPromotion.getChannelId());
        try {
            for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
                try {
                    if (model.getState() == 0) {//上新
                        addProductAndDeal(modelCmsBtJmPromotion, shippingSystemId, model, shopBean);//上新
                    } else //更新 copyDeal
                    {
                        updateProductAddDeal(modelCmsBtJmPromotion, shippingSystemId, model, shopBean);////更新 copyDeal
                    }
                } catch (Exception ex) {
                    model.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
                    model.setSynchState(3);//同步更新失败
                    try {
                        daoCmsBtJmPromotionProduct.update(model);
                    } catch (Exception cex) {
                        LOG.error("addProductAndDealByPromotionId", cex);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("addProductAndDealByPromotionId上新失败", ex);
        }
        LOG.info(promotionId + " 聚美上新end");
    }

    //上新
    private void addProductAndDeal(CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId, CmsBtJmPromotionProductModel model, ShopBean shopBean) throws Exception {
        JMNewProductInfo updateInfo = service.getJMNewProductInfo(model);
        updateInfo.loadData();
        JmProductBean jmProductBean = selfBeanToJmBean(updateInfo, modelCmsBtJmPromotion, shippingSystemId);
        for (CmsBtJmProductImagesModel imgemodel : updateInfo.getListCmsBtJmProductImages()) {
            if (imgemodel.getSynFlg() == 0) { //上传图片
                serviceJuMeiUploadImageJob.uploadImage(imgemodel, shopBean);
                imgemodel.setSynFlg(1);
                service.saveCmsBtJmProductImages(imgemodel);
            }
        }
        for (CmsMtMasterInfoModel modelCmsMtMasterInfo : updateInfo.getListCmsMtMasterInfo()) {
            if (modelCmsMtMasterInfo.getSynFlg() == 0) { //上传图片
                modelCmsMtMasterInfo.setSynFlg(1);
                serviceJuMeiUploadImageJob.uploadImage(modelCmsMtMasterInfo, shopBean);
                service.saveCmsMtMasterInfo(modelCmsMtMasterInfo);
            }
        }
        setImages(updateInfo, jmProductBean);
        serviceJumeiProduct.productNewUpload(shopBean, jmProductBean);
        updateInfo.getModelCmsBtJmProduct().setState(1);//已经上新
        updateInfo.getModelCmsBtJmProduct().setJumeiProductId(jmProductBean.getJumei_product_id());
        updateInfo.getModelCmsBtJmProduct().setLastJmHashId(jmProductBean.getJumei_product_id());//保存最后一次活动JmHashId
        updateInfo.getModelCmsBtJmPromotionProduct().setJmHashId(jmProductBean.getJumei_product_id());
        updateInfo.getModelCmsBtJmPromotionProduct().setState(1);//已经上新
        updateInfo.getModelCmsBtJmPromotionProduct().setSynchState(2);//更新成功
        for (JmProductBean_Spus spu : jmProductBean.getSpus()) {
            String skuCode = spu.getSkuInfo().getPartner_sku_no();
            CmsBtJmSkuModel cmsBtJmSkuModel = updateInfo.getMapCodeCmsBtJmSkuModel().get(skuCode);
            cmsBtJmSkuModel.setJmSkuNo(spu.getSkuInfo().getJumei_sku_no());
            cmsBtJmSkuModel.setJmSpuNo(spu.getJumei_spu_no());
            cmsBtJmSkuModel.setState(1);
            CmsBtJmPromotionSkuModel promotionSkuModel = updateInfo.getMapSkuIdCmsBtJmPromotionSkuModel().get(cmsBtJmSkuModel.getId());
            promotionSkuModel.setState(1);//已经上新
            promotionSkuModel.setSynchState(2);//上新更新成功
        }
    }
    //更新 copyDeal
    private  void updateProductAddDeal(CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId, CmsBtJmPromotionProductModel modelPromotionProduct, ShopBean shopBean) throws Exception {
        JMUpdateProductInfo info = service.getJMUpdateProductInfo(modelPromotionProduct);
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        request.setJumei_hash_id(modelProduct.getLastJmHashId());
        request.setStart_time(getTime(modelCmsBtJmPromotion.getActivityStart()));
        request.setEnd_time(getTime(modelCmsBtJmPromotion.getActivityEnd()));
        HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(shopBean, request);
        if (response.is_Success()) {
            modelPromotionProduct.setJmHashId(response.getJumei_hash_id());
            modelProduct.setLastJmHashId(response.getJumei_hash_id());
            jmAddListSku(info, shopBean);//添加未上新的sku
            service.saveJMUpdateProductInfo(info);
        }
    }



    //添加未上新的sku
    private  void  jmAddListSku( JMUpdateProductInfo info,ShopBean shopBean) throws Exception {
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {
            try {
                jmAddSku(info, shopBean, modelPromotionSku);
            } catch (Exception ex) {
                modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
                modelPromotionSku.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            }
        }
    }
    private void jmAddSku(JMUpdateProductInfo info, ShopBean shopBean, CmsBtJmPromotionSkuModel modelPromotionSku) throws Exception {
        CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
        //spu
        HtSpuAddRequest requestSpu = new HtSpuAddRequest();
        requestSpu.setUpc_code(modelSku.getSkuCode());
        requestSpu.setUpc_code(modelSku.getUpc());//jmBtSkuImportModel.getUpcCode());
        requestSpu.setPropery("OTHER");
        requestSpu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
        requestSpu.setAttribute(info.getModelCmsBtJmProduct().getAttribute());//jmBtProductImport.getAttribute());
        //spt  spu.setAbroad_price(modelPromotionSku.);//jmBtSkuImportModel.getAbroadPrice());
        // todo 价格单位
        requestSpu.setArea_code(19);
        HtSpuAddResponse responseSpu = serviceJumeiHtSpu.add(shopBean, requestSpu);
        if (responseSpu.is_Success()) {
            modelSku.setJmSpuNo(responseSpu.getJumei_spu_no());
        } else {
            modelPromotionSku.setErrorMsg(responseSpu.getErrorMsg());
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
        }
        //sku
        HtSkuAddRequest requestSku = new HtSkuAddRequest();
        requestSku.setJumei_spu_no(modelSku.getJmSpuNo());//.setPartner_sku_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
        requestSku.setBusinessman_num(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
        requestSku.setStocks("1");
        requestSku.setDeal_price(modelPromotionSku.getDealPrice().toString());//jmBtSkuImportModel.getDealPrice().toString());
        requestSku.setMarket_price(modelPromotionSku.getMarketPrice().toString());//jmBtSkuImportModel.getMarketPrice().toString());
        if (modelPromotionSku.getDealPrice().doubleValue() < 1 || modelPromotionSku.getMarketPrice().doubleValue() < 1) {
            throw new BusinessException("价格为0");
        }
        HtSkuAddResponse responseSku = serviceJumeiHtSku.add(shopBean, requestSku);
        if (responseSku.is_Success()) {
            modelSku.setJmSkuNo(responseSku.getJumei_sku_no());
            modelSku.setState(1);
            modelPromotionSku.setState(1);
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.NewSuccess.getId());
        } else {
            modelPromotionSku.setErrorMsg(responseSku.getErrorMsg());
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
        }
    }

    private JmProductBean selfBeanToJmBean(JMNewProductInfo info, CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId) throws Exception {
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        String partner_sku_nos = "";
        JmProductBean jmProductBean = new JmProductBean();
        jmProductBean.setName(modelProduct.getProductNameCn());//jmBtProductImport.getProductName());
        jmProductBean.setProduct_spec_number(modelProduct.getProductCode());//jmBtProductImport.getProductCode());
        jmProductBean.setCategory_v3_4_id(modelProduct.getCategoryLv4Id());//jmBtProductImport.getCategoryLv4Id());
        jmProductBean.setBrand_id(modelProduct.getBrandId());//jmBtProductImport.getBrandId());
        jmProductBean.setForeign_language_name(modelProduct.getForeignLanguageName());//jmBtProductImport.getForeignLanguageName());
        // Todo
        // sku
        List<JmProductBean_Spus> spus = new ArrayList<>();
        jmProductBean.setSpus(spus);
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {//.getSkuImportModelList()) {
            CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
            spu.setUpc_code(modelSku.getUpc());//jmBtSkuImportModel.getUpcCode());
            spu.setPropery("OTHER");
            spu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
            spu.setAttribute(modelProduct.getAttribute());//jmBtProductImport.getAttribute());
            //spt  spu.setAbroad_price(modelPromotionSku.);//jmBtSkuImportModel.getAbroadPrice());
            // todo 价格单位
            spu.setArea_code("19");
            JmProductBean_Spus_Sku sku = new JmProductBean_Spus_Sku();
            sku.setPartner_sku_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
            sku.setSale_on_this_deal("1");
            sku.setBusinessman_num(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
            sku.setStocks("1");
            sku.setDeal_price(modelPromotionSku.getDealPrice().toString());//jmBtSkuImportModel.getDealPrice().toString());
            sku.setMarket_price(modelPromotionSku.getMarketPrice().toString());//jmBtSkuImportModel.getMarketPrice().toString());
            if (modelPromotionSku.getDealPrice().doubleValue() < 1 || modelPromotionSku.getMarketPrice().doubleValue() < 1) {
                throw new BusinessException("价格为0");
            }
            spu.setSkuInfo(sku);
            spus.add(spu);
            partner_sku_nos += modelSku.getSkuCode(); //jmBtSkuImportModel.getSku() + ",";
        }
        //dealinfo
        // JmBtDealImportModel jmBtDealImportModel = jmBtProductImport.getJmBtDealImportModel();
        JmProductBean_DealInfo jmProductBean_DealInfo = new JmProductBean_DealInfo();
        jmProductBean_DealInfo.setPartner_deal_id(modelProduct.getProductCode() + "-" + info.getModelCmsBtJmPromotionProduct().getCmsBtJmPromotionId());//jmBtDealImportModel.getProductCode() + "-" + jmBtDealImportModel.getDealId());
        jmProductBean_DealInfo.setStart_time(getTime(modelCmsBtJmPromotion.getActivityStart()));//getTime(jmBtDealImportModel.getStartTime()));
        jmProductBean_DealInfo.setEnd_time(getTime(modelCmsBtJmPromotion.getActivityEnd()));//jmBtDealImportModel.getEndTime()));
        jmProductBean_DealInfo.setUser_purchase_limit(modelPromotionProduct.getLimit());//jmBtDealImportModel.getUserPurchaseLimit());
        jmProductBean_DealInfo.setShipping_system_id(shippingSystemId);//jmBtDealImportModel.getShippingSystemId());
        jmProductBean_DealInfo.setProduct_long_name(modelProduct.getProductLongName());//jmBtDealImportModel.getProductLongName());
        jmProductBean_DealInfo.setProduct_medium_name(modelProduct.getProductMediumName());//jmBtDealImportModel.getProductMediumName());
        jmProductBean_DealInfo.setProduct_short_name(modelProduct.getProductShortName());//jmBtDealImportModel.getProductShortName());
        jmProductBean_DealInfo.setAddress_of_produce(modelProduct.getAddressOfProduce());//jmBtProductImport.getAddressOfProduce());
        jmProductBean_DealInfo.setBefore_date("无");
        jmProductBean_DealInfo.setSuit_people("时尚潮流人士");
        jmProductBean_DealInfo.setSearch_meta_text_custom(modelProduct.getSearchMetaTextCustom());//jmBtDealImportModel.getSearchMetaTextCustom());

        // 特殊说明
        jmProductBean_DealInfo.setSpecial_explain(modelProduct.getSpecialNote());//jmBtProductImport.getSpecialNote());

        jmProductBean_DealInfo.setPartner_sku_nos(partner_sku_nos.substring(0, partner_sku_nos.length() - 1));

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
        if (listNormal.size() != 0) {
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
        if (listVERTICAL.size() != 0) {
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
        if (listPRODUCT.size() > 0) {
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
        if (listPARAMETER.size() != 0) {
            for (CmsBtJmProductImagesModel jmPicBean : listPARAMETER) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getJmUrl()));
            }
        }


        // 品牌图
        stringBuffer = new StringBuffer();
        // pics = imagesMap.get(JumeiImageType.BRANDSTORY.getId());
        List<CmsMtMasterInfoModel> listBRANDSTORY = getListCmsMtMasterInfoModel(info.getListCmsMtMasterInfo(), EnumJuMeiMtMasterInfo.BRANDSTORY.getId());
        if (listBRANDSTORY.size() != 0) {
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
        if (listSIZE.size() != 0) {
            for (CmsMtMasterInfoModel jmPicBean : listSIZE) {
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue2()));
            }
        } else {
            //if(!jmBtProductImport.getSizeType().equalsIgnoreCase("One Size") && !jmBtProductImport.getSizeType().equalsIgnoreCase("OneSize")){
            String SizeType = info.getModelCmsBtJmProduct().getSizeType();
            if (!SizeType.equalsIgnoreCase("One Size") && !SizeType.equalsIgnoreCase("OneSize")) {
                throw new BusinessException("尺码图不存在");
            }
        }
        jmProductBean.getDealInfo().setDescription_usage(String.format(DESCRIPTION_USAGE, info.getModelCmsBtJmProduct().getProductDesCn(), stringBuffer.toString()));//.getProductDes()
        //物流图
        //pics = imagesMap.get(JumeiImageType.LOGISTICS.getId());
        List<CmsMtMasterInfoModel> lisLOGISTICS = getListCmsMtMasterInfoModel(info.getListCmsMtMasterInfo(), EnumJuMeiMtMasterInfo.LOGISTICS.getId());
        if (lisLOGISTICS.size() != 0) {
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
    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }

}
