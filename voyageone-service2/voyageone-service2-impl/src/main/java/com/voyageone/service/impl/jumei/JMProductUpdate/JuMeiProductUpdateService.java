package com.voyageone.service.impl.jumei.JMProductUpdate;

import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.service.dao.jumei.*;
import com.voyageone.service.daoext.jumei.CmsMtMasterInfoDaoExt;
import com.voyageone.service.model.jumei.*;
import com.voyageone.service.model.jumei.businessmodel.JMNewProductInfo;
import com.voyageone.service.model.jumei.businessmodel.JMUpdateProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2016/4/12.
 */
@Service
public class JuMeiProductUpdateService {
    @Autowired
    CmsBtJmPromotionProductDao daoCmsBtJmPromotionProduct;
    @Autowired
    CmsBtJmPromotionSkuDao daoCmsBtJmPromotionSku;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
    @Autowired
    CmsBtJmPromotionDao daoCmsBtJmPromotion;
    @Autowired
    CmsBtJmProductImagesDao daoCmsBtJmProductImages;
    @Autowired
    CmsMtMasterInfoDao daoCmsMtMasterInfo;

    @Autowired
    CmsMtMasterInfoDaoExt daoExtCmsMtMasterInfo;
    public ShopBean getShopBean() {
        ShopBean shopBean = new ShopBean();
        shopBean.setAppKey("72");
        shopBean.setAppSecret("62cc742a25d3ec18ecee9dd5bcc724ccfb2844ac");
        shopBean.setSessionKey("e5f9d143815a520726576040460bd67f");
        shopBean.setApp_url("http://182.138.102.82:8868/");
        return shopBean;
    }
    public CmsBtJmPromotionModel getCmsBtJmPromotion(int id) {
        return daoCmsBtJmPromotion.select(id);
    }
    public int getShippingSystemId(String ChannelId) {
        return 2121;
    }
    public List<CmsBtJmPromotionProductModel> getJuMeiNewListPromotionProduct(int promotionId) {
        Map<String, Object> parameterPromotionProduct = new HashMap<>();
        parameterPromotionProduct.put("promotionId",promotionId);
        parameterPromotionProduct.put("synchState",1);
        List<CmsBtJmPromotionProductModel> listPromotionProduct = daoCmsBtJmPromotionProduct.selectList(parameterPromotionProduct);
        return listPromotionProduct;
    }

    public JMUpdateProductInfo getJMUpdateProductInfo(CmsBtJmPromotionProductModel promotionProductModel) {
        JMUpdateProductInfo info = new JMUpdateProductInfo();
        info.setModelCmsBtJmPromotionProduct(promotionProductModel);

        //CmsBtJmProduct
        CmsBtJmProductModel productModel = daoCmsBtJmProduct.select(promotionProductModel.getCmsBtJmProductId());
        info.setModelCmsBtJmProduct(productModel);

        //CmsBtJmPromotionSku   list
        Map<String, Object> parameterPromotionSku = new HashMap<>();
        parameterPromotionSku.put("cmsBtJmPromotionId", promotionProductModel.getCmsBtJmPromotionId());
        parameterPromotionSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        parameterPromotionSku.put("state", "0");
        List<CmsBtJmPromotionSkuModel> listPromotionSku = daoCmsBtJmPromotionSku.selectList(parameterPromotionSku);
        info.setListCmsBtJmPromotionSku(listPromotionSku);

        //CmsBtJmSku   list
        Map<String, Object> parameterSku = new HashMap<>();
        parameterSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        parameterSku.put("state", "0");
        List<CmsBtJmSkuModel> listSkuModel = daoCmsBtJmSku.selectList(parameterSku);
        info.setListCmsBtJmSku(listSkuModel);
        return info;
    }
    public JMNewProductInfo getJMNewProductInfo(CmsBtJmPromotionProductModel promotionProductModel) {
        JMNewProductInfo info = new JMNewProductInfo();
        info.setModelCmsBtJmPromotionProduct(promotionProductModel);

        //CmsBtJmProduct
        CmsBtJmProductModel productModel = daoCmsBtJmProduct.select(promotionProductModel.getCmsBtJmProductId());
        info.setModelCmsBtJmProduct(productModel);

        //CmsBtJmPromotionSku   list
        Map<String, Object> parameterPromotionSku = new HashMap<>();
        parameterPromotionSku.put("cmsBtJmPromotionId", promotionProductModel.getCmsBtJmPromotionId());
        parameterPromotionSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        List<CmsBtJmPromotionSkuModel> listPromotionSku = daoCmsBtJmPromotionSku.selectList(parameterPromotionSku);
        info.setListCmsBtJmPromotionSku(listPromotionSku);


        //CmsBtJmSku   list
        Map<String, Object> parameterSku = new HashMap<>();
        parameterSku.put("cmsBtJmProductId", promotionProductModel.getCmsBtJmProductId());
        List<CmsBtJmSkuModel> listSkuModel = daoCmsBtJmSku.selectList(parameterSku);
        info.setListCmsBtJmSku(listSkuModel);

        //tCmsBtJmProductImages  list
        Map<String, Object> parameterJmProductImages = new HashMap<>();
        parameterJmProductImages.put("channelId", promotionProductModel.getChannelId());
        parameterJmProductImages.put("productCode", promotionProductModel.getProductCode());
        List<CmsBtJmProductImagesModel> listCmsBtJmProductImagesModel = daoCmsBtJmProductImages.selectList(parameterJmProductImages);
        info.setListCmsBtJmProductImages(listCmsBtJmProductImagesModel);


        //CmsMtMasterInfo  list
        Map<String, Object> parameterCmsMtMasterInfo = new HashMap<>();
        parameterCmsMtMasterInfo.put("platformId", 27);
        parameterCmsMtMasterInfo.put("channelId", promotionProductModel.getChannelId());
        parameterCmsMtMasterInfo.put("brandName", productModel.getBrandName());
        parameterCmsMtMasterInfo.put("productType", productModel.getProductType());
        List<Integer> dataTypeList=new ArrayList<>();
        dataTypeList.add(4);
        dataTypeList.add(5);
        dataTypeList.add(6);
        parameterCmsMtMasterInfo.put("dataTypeList",dataTypeList);
        List<CmsMtMasterInfoModel> listCmsMtMasterInfoModel = daoExtCmsMtMasterInfo.getListJMNewImageInfo(parameterCmsMtMasterInfo);//daoCmsMtMasterInfo.selectList(parameterCmsMtMasterInfo);
        info.setListCmsMtMasterInfo(listCmsMtMasterInfoModel);

        return info;
    }
    @VOTransactional
    public  void  saveJMNewProductUpdateInfo(JMNewProductInfo info) {
        daoCmsBtJmProduct.update(info.getModelCmsBtJmProduct());
        daoCmsBtJmPromotionProduct.update(info.getModelCmsBtJmPromotionProduct());
        for (CmsBtJmSkuModel cmsBtJmSku : info.getListCmsBtJmSku()) {
            daoCmsBtJmSku.update(cmsBtJmSku);
        }
        for (CmsBtJmPromotionSkuModel promotionSku : info.getListCmsBtJmPromotionSku()) {
            daoCmsBtJmPromotionSku.update(promotionSku);
        }
    }
    @VOTransactional
    public  void  saveJMUpdateProductInfo(JMUpdateProductInfo info)
    {
        daoCmsBtJmProduct.update(info.getModelCmsBtJmProduct());
        daoCmsBtJmPromotionProduct.update(info.getModelCmsBtJmPromotionProduct());
        for (CmsBtJmSkuModel cmsBtJmSku : info.getListCmsBtJmSku()) {
            daoCmsBtJmSku.update(cmsBtJmSku);
        }
        for (CmsBtJmPromotionSkuModel promotionSku : info.getListCmsBtJmPromotionSku()) {
            daoCmsBtJmPromotionSku.update(promotionSku);
        }
    }
    public void saveCmsBtJmProductImages(CmsBtJmProductImagesModel model) {
        daoCmsBtJmProductImages.update(model);
    }
    public void saveCmsMtMasterInfo(CmsMtMasterInfoModel model) {
        daoCmsMtMasterInfo.update(model);
    }
}
