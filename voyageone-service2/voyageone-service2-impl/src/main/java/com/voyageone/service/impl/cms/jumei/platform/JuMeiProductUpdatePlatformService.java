package com.voyageone.service.impl.cms.jumei.platform;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtProductService;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.bean.HtDealUpdate_DealInfo;
import com.voyageone.components.jumei.bean.HtProductUpdate_ProductInfo;
import com.voyageone.components.jumei.reponse.*;
import com.voyageone.components.jumei.request.*;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmPromotionProductDao;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionProductModel;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.bean.cms.businessmodel.EnumJuMeiSynchState;
import com.voyageone.service.bean.cms.businessmodel.JMUpdateProductInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductUpdatePlatformService {
    private static final String IMG_HTML = "<img src=\"%s\" alt=\"\" />";
    private static final String DESCRIPTION_USAGE = "<div>%s %s <br /></div>";
    private static final String DESCRIPTION_IMAGES = "%s<br />";
    private static final Logger LOG = LoggerFactory.getLogger(JuMeiProductUpdatePlatformService.class);
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
    JumeiHtProductService serviceJumeiHtProduct;
    @Autowired
    JuMeiDealService serviceJuMeiDeal;

    public void updateProductAddDeal(int shippingSystemId, CmsBtJmPromotionProductModel modelPromotionProduct, ShopBean shopBean) throws Exception {
        JMUpdateProductInfo info = service.getJMUpdateProductInfo(modelPromotionProduct);
        CmsBtJmProductModel modelCmsBtJmProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        long activityStart = modelCmsBtJmPromotionProduct.getActivityStart().getTime();
        long activityEnd = modelCmsBtJmPromotionProduct.getActivityEnd().getTime();
        long lastJmDealBegin = modelCmsBtJmProduct.getLastJmDealBegin().getTime();
        long LastJmDealEnd = modelCmsBtJmProduct.getLastJmDealEnd().getTime();

        if (LastJmDealEnd >= activityEnd)//1.	时间包含（包括开始时间，结束时间）
        {
            /*1)取上次的jumei_hash_id      取上次(最新)deal_end时间 */
            modelPromotionProduct.setJmHashId(modelCmsBtJmProduct.getLastJmHashId());
            modelPromotionProduct.setActivityEnd(modelCmsBtJmProduct.getLastJmDealEnd());
        } else if (LastJmDealEnd < activityEnd && LastJmDealEnd >= activityStart)//2.	时间部分重叠 (包括开始时间)    开始时间肯定比上次大不存在只包含结束时间的情况
        {
            /*1)	取上次的jumei_hash_id  	Deal延期API */
            modelPromotionProduct.setJmHashId(modelCmsBtJmProduct.getLastJmHashId());
            serviceJuMeiDeal.updateDealEndTime(shopBean, modelPromotionProduct);
            modelCmsBtJmProduct.setLastJmDealBegin(modelPromotionProduct.getActivityStart());//保存最后一次 deal时间
            modelCmsBtJmProduct.setLastJmDealEnd(modelPromotionProduct.getActivityEnd());//
        } else //不重叠
        {
           /* 1)调用 复制Deal(特卖)信息 */
            jmHtDealCopy(info, shopBean);
        }
        /* 2)修改deal  sku  spu   商品属性  四个修改接口
            3) 新增spu  sku  两个接口*/
        jmHtDealupdate(info, shopBean, shippingSystemId);//deal
        jmHtProductUpdate(info, shopBean);//product
        jmHtSpuSkuUpdateList(info, shopBean);//spu sku
        jmAddListSku(info, shopBean);//添加未上新的sku
        service.saveJMUpdateProductInfo(info);
    }

    public void updateJMProductInfo(int shippingSystemId, CmsBtJmPromotionProductModel modelPromotionProduct, ShopBean shopBean) throws Exception {
        JMUpdateProductInfo info = service.getJMUpdateProductInfo(modelPromotionProduct);
        info.loadData();
        updateJMProductInfo(shippingSystemId, shopBean, info);
    }
    private void updateJMProductInfo(int shippingSystemId, ShopBean shopBean, JMUpdateProductInfo info) throws Exception {
       // jmHtSpuSkuUpdateList(info, shopBean);//spu sku  未提供
       // jmHtProductUpdate(info, shopBean);//product     未提供
        //jmAddListSku(info, shopBean);//添加未上新的sku  未提供
        jmHtDealupdate(info, shopBean, shippingSystemId);//deal
    }
    private void jmHtDealCopy(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
        HtDealCopyDealRequest request = new HtDealCopyDealRequest();
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelCmsBtJmPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        request.setJumei_hash_id(modelProduct.getLastJmHashId());
        request.setStart_time(getTime(info.getModelCmsBtJmPromotionProduct().getActivityStart()));
        request.setEnd_time(getTime(info.getModelCmsBtJmPromotionProduct().getActivityEnd()));
        HtDealCopyDealResponse response = serviceJumeiHtDeal.copyDeal(shopBean, request);
        if (response.is_Success()) {
            info.getModelCmsBtJmPromotionProduct().setJmHashId(response.getJumei_hash_id());
            modelProduct.setLastJmHashId(response.getJumei_hash_id());
            modelProduct.setLastJmDealBegin(modelCmsBtJmPromotionProduct.getActivityStart());
            modelProduct.setLastJmDealEnd(modelCmsBtJmPromotionProduct.getActivityEnd());
        } else {
            throw new BusinessException("productId:" + modelProduct.getId() + "jmHtDealCopyErrorMsg:" + response.getErrorMsg());
        }
    }
    //商品属性更新
    public void jmHtProductUpdate(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();

        HtProductUpdateRequest request = new HtProductUpdateRequest();
        request.setJumei_product_id(info.getModelCmsBtJmProduct().getJumeiProductId());
        HtProductUpdate_ProductInfo productInfo = new HtProductUpdate_ProductInfo();
        request.setJumei_product_name(info.getModelCmsBtJmProduct().getProductNameCn());

        productInfo.setName(modelProduct.getProductNameCn());//jmBtProductImport.getProductName());
        productInfo.setCategory_v3_4_id(String.valueOf(modelProduct.getCategoryLv4Id()));//jmBtProductImport.getCategoryLv4Id());
        productInfo.setBrand_id(String.valueOf(modelProduct.getBrandId()));//jmBtProductImport.getBrandId());
        productInfo.setForeign_language_name(modelProduct.getForeignLanguageName());//jmBtProductImport.getForeignLanguageName());
        request.setUpdate_data(productInfo);
        HtProductUpdateResponse response = serviceJumeiHtProduct.update(shopBean, request);
        if (response.getIs_Success()) {

        } else {
            throw new BusinessException("productId:" + modelProduct.getId() + " jmHtProductUpdateErrorMsg:" + response.getErrorMsg());
        }
    }
    //修改deal
    public void jmHtDealupdate(JMUpdateProductInfo info, ShopBean shopBean, int shippingSystemId) throws Exception {
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        dealInfo.setUser_purchase_limit(String.valueOf(modelPromotionProduct.getLimit()));//jmBtDealImportModel.getUserPurchaseLimit());
        dealInfo.setShipping_system_id(String.valueOf(shippingSystemId));//jmBtDealImportModel.getShippingSystemId());
        dealInfo.setProduct_long_name(modelProduct.getProductLongName());//jmBtDealImportModel.getProductLongName());
        dealInfo.setProduct_medium_name(modelProduct.getProductMediumName());//jmBtDealImportModel.getProductMediumName());
        dealInfo.setProduct_short_name(modelProduct.getProductShortName());//jmBtDealImportModel.getProductShortName());
        dealInfo.setBefore_date("无");
        dealInfo.setSuit_people("时尚潮流人士");
        dealInfo.setSpecial_explain(modelProduct.getSpecialNote());//jmBtProductImport.getSpecialNote());
        dealInfo.setSearch_meta_text_custom(modelProduct.getSearchMetaTextCustom());//jmBtDealImportModel.getSearchMetaTextCustom());
        String partner_sku_nos = "";
        for (CmsBtJmSkuModel modelSku : info.getListCmsBtJmSku()) {
            if(modelSku.getState()==1) {
                partner_sku_nos += modelSku.getSkuCode() + ",";
            }
        }
        // 特殊说明
        if (partner_sku_nos.length() > 0) {
            dealInfo.setJumei_sku_no(partner_sku_nos.substring(0, partner_sku_nos.length() - 1));
        }
        request.setJumei_hash_id(info.getModelCmsBtJmPromotionProduct().getJmHashId());
        request.setUpdate_data(dealInfo);
        HtDealUpdateResponse response = serviceJumeiHtDeal.update(shopBean, request);
        if (response.getIs_Success()) {

        } else {
            throw new BusinessException("productId:" + modelProduct.getId() + " jmHtDealupdateErrorMsg:" + response.getErrorMsg());
        }
    }
    //修改sku spu
    private void jmHtSpuSkuUpdateList(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {
            CmsBtJmPromotionProductModel modelPromotionProduct = info.getModelCmsBtJmPromotionProduct();
                if (modelPromotionProduct.getState() == 1) {
                    jmHtSpuSkuUpdate(info, shopBean, modelPromotionSku);
                }
        }
    }

    private void jmHtSpuSkuUpdate(JMUpdateProductInfo info, ShopBean shopBean, CmsBtJmPromotionSkuModel modelPromotionSku) throws Exception {
        CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
        //spu
        HtSpuUpdateRequest requestSpu = new HtSpuUpdateRequest();
        requestSpu.setJumei_spu_no(modelSku.getJmSpuNo());//);
        requestSpu.setUpc_code(modelSku.getUpc());
        requestSpu.setPropery("OTHER");
        requestSpu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
        requestSpu.setAttribute(info.getModelCmsBtJmProduct().getAttribute());//jmBtProductImport.getAttribute());
        requestSpu.setAbroad_price(info.getModelCmsBtJmProduct().getMsrp().doubleValue());//jmBtSkuImportModel.getAbroadPrice());
        // todo 价格单位
        requestSpu.setArea_code(19);
        HtSpuUpdateResponse responseSpu = serviceJumeiHtSpu.update(shopBean, requestSpu);
        if (responseSpu.is_Success()) {

        } else {
            throw new BusinessException("skuId:" + modelPromotionSku.getCmsBtJmSkuId() + " updateSpuErrorMsg:" + responseSpu.getErrorMsg());
        }
        //sku
        HtSkuUpdateRequest requestSku = new HtSkuUpdateRequest();
        requestSku.setJumei_sku_no(modelSku.getJmSkuNo());//.setPartner_sku_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
        requestSku.setBusinessman_num(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
        // requestSku.setCustoms_product_number();
        HtSkuUpdateResponse responseSku = serviceJumeiHtSku.update(shopBean, requestSku);
        if (responseSku.is_Success()) {
        } else {
            throw new BusinessException("skuId:" + modelPromotionSku.getCmsBtJmSkuId() + " updateSkuErrorMsg:" + responseSku.getErrorMsg());
        }
    }
    //添加未上新的sku
    private void jmAddListSku(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {
                if (modelPromotionSku.getState() == 0) {
                    jmAddSku(info, shopBean, modelPromotionSku);
                }
        }
    }
    private void jmAddSku(JMUpdateProductInfo info, ShopBean shopBean, CmsBtJmPromotionSkuModel modelPromotionSku) throws Exception {
        CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
        //spu
        HtSpuAddRequest requestSpu = new HtSpuAddRequest();
        requestSpu.setUpc_code(modelSku.getSkuCode());
        requestSpu.setUpc_code(modelSku.getUpc());//jmBtSkuImportModel.getUpcCode());
        requestSpu.setProperty("OTHER");
        requestSpu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
        requestSpu.setAttribute(info.getModelCmsBtJmProduct().getAttribute());//jmBtProductImport.getAttribute());
        //spt  spu.setAbroad_price(modelPromotionSku.);//jmBtSkuImportModel.getAbroadPrice());
        // todo 价格单位
        requestSpu.setArea_code("19");
        HtSpuAddResponse responseSpu = serviceJumeiHtSpu.add(shopBean, requestSpu);
        if (responseSpu.is_Success()) {
            modelSku.setJmSpuNo(responseSpu.getJumei_spu_no());
        } else {
            throw new BusinessException("skuId:" + modelPromotionSku.getCmsBtJmSkuId() + " jmAddSkuErrorMsg:" + responseSpu.getErrorMsg());
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
            throw new BusinessException("skuId:" + modelPromotionSku.getCmsBtJmSkuId() + " jmAddSkuErrorMsg:" + responseSpu.getErrorMsg());
        }
    }
    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }
}
