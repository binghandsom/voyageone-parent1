package com.voyageone.task2.cms.service.jumei;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.components.jumei.bean.JmProductBean;
import com.voyageone.components.jumei.bean.JmProductBean_DealInfo;
import com.voyageone.components.jumei.bean.JmProductBean_Spus;
import com.voyageone.components.jumei.bean.JmProductBean_Spus_Sku;
import com.voyageone.components.jumei.enums.EnumJuMeiMtMasterInfo;
import com.voyageone.components.jumei.enums.EnumJuMeiProductImageType;
import com.voyageone.components.jumei.enums.JumeiImageType;
import com.voyageone.service.impl.jumei.JuMeiProductUpdateService;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.JMProductUpdateInfo;
import com.voyageone.task2.base.BaseMQTaskService;
import com.voyageone.task2.base.modelbean.TaskControlBean;
import com.voyageone.task2.cms.bean.JmPicBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductUpdateJobService extends BaseMQTaskService {
    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";

    private static final String DESCRIPTION_USAGE = "<div>%s %s <br /></div>";

    private static final String DESCRIPTION_IMAGES = "%s<br />";
    @Autowired
    JuMeiProductUpdateService service;
    @Override
    protected void onStartup(List<TaskControlBean> taskControlList, Map<String, Object> message) throws Exception {
        int id = (int) Double.parseDouble(message.get("id").toString());
        addProductAndDealByPromotionId(id);
    }
    @Override
    public SubSystem getSubSystem() {
        return SubSystem.CMS;
    }
    @Override
    public String getTaskName() {
        return "JuMeiProductUpdateJobService";
    }
    public void addProductAndDealByPromotionId(int promotionId) throws Exception {
        //logger.info(jmBtProductImport.getChannelId() + "|" + jmBtProductImport.getProductCode() + " 聚美上新开始");
        CmsBtJmPromotionModel modelCmsBtJmPromotion = service.getCmsBtJmPromotion(promotionId);
        List<CmsBtJmPromotionProductModel> listCmsBtJmPromotionProductModel = service.getListPromotionProduct();
        int shippingSystemId = service.getShippingSystemId(modelCmsBtJmPromotion.getChannelId());
        for (CmsBtJmPromotionProductModel model : listCmsBtJmPromotionProductModel) {
            adddProductAndDeal(modelCmsBtJmPromotion, shippingSystemId, model);
        }
    }
    private void adddProductAndDeal(CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId, CmsBtJmPromotionProductModel model) throws Exception {
        JMProductUpdateInfo updateInfo = service.getJMProductUpdateInfo(model);
        JmProductBean jmProductBean = selfBeanToJmBean(updateInfo, modelCmsBtJmPromotion, shippingSystemId);
        setImages(updateInfo, jmProductBean);

    }
    private JmProductBean selfBeanToJmBean(JMProductUpdateInfo info, CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId) throws Exception {
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
            CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getId());
            JmProductBean_Spus spu = new JmProductBean_Spus();
            spu.setPartner_spu_no(modelSku.getJmSpuNo());//jmBtSkuImportModel.getSku());
            spu.setUpc_code(modelSku.getUpc());//jmBtSkuImportModel.getUpcCode());
            spu.setPropery("OTHER");
            spu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
            spu.setAttribute(modelProduct.getAttribute());//jmBtProductImport.getAttribute());
            //spt  spu.setAbroad_price(modelPromotionSku.);//jmBtSkuImportModel.getAbroadPrice());
            // todo 价格单位
            spu.setArea_code("19");
            JmProductBean_Spus_Sku sku = new JmProductBean_Spus_Sku();
            sku.setPartner_sku_no(modelSku.getJmSkuNo());//jmBtSkuImportModel.getSku());
            sku.setSale_on_this_deal("1");
            sku.setBusinessman_num(modelSku.getJmSkuNo());//jmBtSkuImportModel.getSku());
            sku.setStocks("1");
            sku.setDeal_price(modelPromotionSku.getDealPrice().toString());//jmBtSkuImportModel.getDealPrice().toString());
            sku.setMarket_price(modelPromotionSku.getMarketPrice().toString());//jmBtSkuImportModel.getMarketPrice().toString());
            if (modelPromotionSku.getDealPrice().doubleValue() < 1 || modelPromotionSku.getMarketPrice().doubleValue() < 1) {
                throw new BusinessException("价格为0");
            }
            spu.setSkuInfo(sku);
            spus.add(spu);
            partner_sku_nos += modelSku.getJmSkuNo(); //jmBtSkuImportModel.getSku() + ",";
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

    private void setImages(JMProductUpdateInfo info, JmProductBean jmProductBean) {
        Map<Integer, List<JmPicBean>> imagesMap = null;//jmUploadProductDao.selectImageByCode(jmBtProductImport.getChannelId(), jmBtProductImport.getProductCode(), jmBtProductImport.getBrandName(), jmBtProductImport.getSizeType());
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
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue1()));
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
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue1()));
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
                stringBuffer.append(String.format(IMG_HTML, jmPicBean.getValue1()));
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
