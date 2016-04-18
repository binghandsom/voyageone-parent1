package com.voyageone.service.impl.jumei.JMProductUpdate;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.common.util.ExceptionUtil;
import com.voyageone.components.jumei.JumeiHtDealService;
import com.voyageone.components.jumei.JumeiHtSkuService;
import com.voyageone.components.jumei.JumeiHtSpuService;
import com.voyageone.components.jumei.Reponse.*;
import com.voyageone.components.jumei.Request.*;
import com.voyageone.components.jumei.bean.*;
import com.voyageone.components.jumei.enums.EnumJuMeiMtMasterInfo;
import com.voyageone.components.jumei.enums.EnumJuMeiProductImageType;
import com.voyageone.components.jumei.service.JumeiProductService;
import com.voyageone.service.bean.cms.CallResult;
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
import org.springframework.util.StringUtils;

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
    @Autowired
    JMShopBeanService serviceJMShopBean;





    //更新 copyDeal
    private void updateProductAddDeal(CmsBtJmPromotionModel modelCmsBtJmPromotion, int shippingSystemId, CmsBtJmPromotionProductModel modelPromotionProduct, ShopBean shopBean) throws Exception {
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

    public  HtDealUpdateResponse  jmHtDealupdate(JMUpdateProductInfo info, ShopBean shopBean,int shippingSystemId) throws Exception {
        HtDealUpdateRequest request = new HtDealUpdateRequest();
        HtDealUpdate_DealInfo dealInfo = new HtDealUpdate_DealInfo();
        CmsBtJmProductModel modelProduct = info.getModelCmsBtJmProduct();
        CmsBtJmPromotionProductModel modelPromotionProduct = info.getModelCmsBtJmPromotionProduct();
        dealInfo.setUser_purchase_limit(modelPromotionProduct.getLimit());//jmBtDealImportModel.getUserPurchaseLimit());
        dealInfo.setShipping_system_id(shippingSystemId);//jmBtDealImportModel.getShippingSystemId());
        dealInfo.setProduct_long_name(modelProduct.getProductLongName());//jmBtDealImportModel.getProductLongName());
        dealInfo.setProduct_medium_name(modelProduct.getProductMediumName());//jmBtDealImportModel.getProductMediumName());
        dealInfo.setProduct_short_name(modelProduct.getProductShortName());//jmBtDealImportModel.getProductShortName());
        dealInfo.setBefore_date("无");
        dealInfo.setSuit_people("时尚潮流人士");
        dealInfo.setSpecial_explain(modelProduct.getSpecialNote());//jmBtProductImport.getSpecialNote());
        dealInfo.setSearch_meta_text_custom(modelProduct.getSearchMetaTextCustom());//jmBtDealImportModel.getSearchMetaTextCustom());
        String partner_sku_nos = "";
        for (CmsBtJmSkuModel modelSku : info.getListCmsBtJmSku()) {
            partner_sku_nos += modelSku.getSkuCode() + ",";
        }
        // 特殊说明
        if (partner_sku_nos.length() > 0) {
            dealInfo.setJumei_sku_no(partner_sku_nos.substring(0, partner_sku_nos.length() - 1));
        }
        request.setJumei_hash_id(info.getModelCmsBtJmPromotionProduct().getJmHashId());
        request.setUpdate_data(dealInfo);
        return serviceJumeiHtDeal.update(shopBean, request);
    }
    private void jmHtSkuUpdateList(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
        for (CmsBtJmPromotionSkuModel modelPromotionSku : info.getListCmsBtJmPromotionSku()) {
            try {
                jmHtSkuUpdate(info, shopBean, modelPromotionSku);
            } catch (Exception ex) {
                modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
                modelPromotionSku.setErrorMsg(ExceptionUtil.getErrorMsg(ex));
            }
        }
    }

    private void jmHtSkuUpdate(JMUpdateProductInfo info, ShopBean shopBean, CmsBtJmPromotionSkuModel modelPromotionSku) throws Exception {
        CmsBtJmSkuModel modelSku = info.getMapCmsBtJmSkuModel().get(modelPromotionSku.getCmsBtJmSkuId());
        //spu
        HtSpuUpdateRequest requestSpu = new HtSpuUpdateRequest();
        //requestSpu.setUpc_code(modelSku.getSkuCode());
        requestSpu.setJumei_spu_id(modelSku.getJmSpuNo());//);
        requestSpu.setUpc_code(modelSku.getUpc());
        requestSpu.setPropery("OTHER");
        requestSpu.setSize(modelSku.getJmSize());//jmBtSkuImportModel.getSize());
        requestSpu.setAttribute(info.getModelCmsBtJmProduct().getAttribute());//jmBtProductImport.getAttribute());
        requestSpu.setAbroad_price(info.getModelCmsBtJmProduct().getMsrp().doubleValue());//jmBtSkuImportModel.getAbroadPrice());

        // todo 价格单位
        requestSpu.setArea_code(19);
        // todo 价格单位
        requestSpu.setArea_code(19);
        HtSpuUpdateResponse responseSpu = serviceJumeiHtSpu.update(shopBean, requestSpu);
        if (responseSpu.is_Success()) {
        } else {
            modelPromotionSku.setErrorMsg(responseSpu.getErrorMsg());
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
        }
        //sku
        HtSkuUpdateRequest requestSku = new HtSkuUpdateRequest();
      //  requestSku.setJumei_sku_id(modelSku.getJmSkuNo());//.setPartner_sku_no(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
       // requestSku.setBusinessman_num(modelSku.getSkuCode());//jmBtSkuImportModel.getSku());
       // requestSku.setStocks("1");
       // requestSku.setDeal_price(modelPromotionSku.getDealPrice().toString());//jmBtSkuImportModel.getDealPrice().toString());
       // requestSku.setMarket_price(modelPromotionSku.getMarketPrice().toString());//jmBtSkuImportModel.getMarketPrice().toString());
        if (modelPromotionSku.getDealPrice().doubleValue() < 1 || modelPromotionSku.getMarketPrice().doubleValue() < 1) {
            throw new BusinessException("价格为0");
        }
        HtSkuUpdateResponse responseSku = serviceJumeiHtSku.update(shopBean, requestSku);
        if (responseSku.is_Success()) {
          //  modelSku.setJmSkuNo(responseSku.getJumei_sku_no());
            modelSku.setState(1);
            modelPromotionSku.setState(1);
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.NewSuccess.getId());
        } else {
            modelPromotionSku.setErrorMsg(responseSku.getErrorMsg());
            modelPromotionSku.setSynchState(EnumJuMeiSynchState.Error.getId());
        }
    }
    //添加未上新的sku
    private void jmAddListSku(JMUpdateProductInfo info, ShopBean shopBean) throws Exception {
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


    public static Long getTime(Date d) throws Exception {
        long l = d.getTime() / 1000 - 8 * 3600;
        return l;
    }

}
