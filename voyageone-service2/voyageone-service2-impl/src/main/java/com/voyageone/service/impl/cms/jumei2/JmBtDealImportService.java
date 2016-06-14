package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.daoext.cms.JmBtDealImportDaoExt;
import com.voyageone.service.impl.cms.MongoSequenceService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class JmBtDealImportService {
    @Autowired
    JmBtDealImportDaoExt daoExtJmBtDealImport;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
    @Autowired
    private MongoSequenceService commSequenceMongoService; // DAO: Sequence
    @Autowired
    CmsBtProductDao daoCmsBtProductDao;

    public void importJM(String channelId) {
        List<JMProductDealBean> listJMProductDealBean = daoExtJmBtDealImport.selectListProductDealByChannelId(channelId);
        for (JMProductDealBean productDeal : listJMProductDealBean) {
            importProduct(productDeal);
        }
    }

    private void importProduct(JMProductDealBean productDeal) {
        JmBtDealImportModel modelJmBtDealImport = daoExtJmBtDealImport.selectJmBtDealImportModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        JmBtProductModel modelJmBtProduct = daoExtJmBtDealImport.selectJmBtProductModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        List<JmBtSkuModel> listModelJmBtSku = daoExtJmBtDealImport.selectListJmBtSkuModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        if (modelJmBtProduct == null)//不存在 sql可以查询出来  不用处理
        {
            System.out.println("ChannelId:" + productDeal.getChannelId() + " DealId:" + productDeal.getDealId() + "  code:" + productDeal.getProductCode());
            return;
        }
        insertProduct(modelJmBtDealImport, modelJmBtProduct);
        insertSku(listModelJmBtSku);
        importCmsBtProductModel(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
    }

    private void insertProduct(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct) {
        CmsBtJmProductModel modelCmsBtJmProduct = new CmsBtJmProductModel();
        modelCmsBtJmProduct.setAddressOfProduce(modelJmBtProduct.getAddressOfProduce());
        modelCmsBtJmProduct.setApplicableCrowd("");
        modelCmsBtJmProduct.setAttribute(modelJmBtProduct.getAttribute());
        modelCmsBtJmProduct.setAvailablePeriod("");
        modelCmsBtJmProduct.setBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setChannelId(modelJmBtProduct.getChannelId());
        modelCmsBtJmProduct.setColorEn("");
        modelCmsBtJmProduct.setForeignLanguageName(modelJmBtProduct.getForeignLanguageName());
        modelCmsBtJmProduct.setHsCode(modelJmBtProduct.getHsCode());
        modelCmsBtJmProduct.setHsName(modelJmBtProduct.getHsName());
        modelCmsBtJmProduct.setHsUnit(modelJmBtProduct.getHsUnit());
        modelCmsBtJmProduct.setImage1("");
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        modelCmsBtJmProduct.setMaterialCn("");
        modelCmsBtJmProduct.setMaterialEn("");
        modelCmsBtJmProduct.setMsrpRmb(new BigDecimal(0));
        modelCmsBtJmProduct.setMsrpUsd(new BigDecimal(0));
        modelCmsBtJmProduct.setOrigin("");
        modelCmsBtJmProduct.setOriginJmHashId(modelJmBtDealImport.getJumeiHashId());
        modelCmsBtJmProduct.setProductCode(modelJmBtProduct.getProductCode());
        modelCmsBtJmProduct.setProductDesCn(modelJmBtProduct.getProductDes());
        modelCmsBtJmProduct.setProductDesEn("");
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        modelCmsBtJmProduct.setProductLongName(modelJmBtDealImport.getProductLongName());
        modelCmsBtJmProduct.setProductMediumName(modelJmBtDealImport.getProductMediumName());
        modelCmsBtJmProduct.setProductNameCn(modelJmBtProduct.getProductName());
        modelCmsBtJmProduct.setProductShortName(modelJmBtDealImport.getProductShortName());
        modelCmsBtJmProduct.setProductType("");
        modelCmsBtJmProduct.setRetailPrice(new BigDecimal(0));
        modelCmsBtJmProduct.setSalePrice(new BigDecimal(0));
        modelCmsBtJmProduct.setSizeType(modelJmBtProduct.getSizeType());
        modelCmsBtJmProduct.setSearchMetaTextCustom(modelJmBtDealImport.getSearchMetaTextCustom());
        modelCmsBtJmProduct.setSpecialnote(modelJmBtProduct.getSpecialNote());
        modelCmsBtJmProduct.setVoBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setCreater("system");
        modelCmsBtJmProduct.setCreated(new Date());
        modelCmsBtJmProduct.setModifier("system");
        modelCmsBtJmProduct.setModified(new Date());
        try {
            daoCmsBtJmProduct.insert(modelCmsBtJmProduct);//CmsBtJmProduct
        } catch (org.springframework.dao.DuplicateKeyException ex)//重复 不处理
        {

        }
    }

    private void insertSku(List<JmBtSkuModel> listModelJmBtSku) {
        for (JmBtSkuModel modelJmBtSku : listModelJmBtSku) {
            CmsBtJmSkuModel modelCmsBtJmSku = new CmsBtJmSkuModel();
            modelCmsBtJmSku.setChannelId(modelJmBtSku.getChannelId());
            modelCmsBtJmSku.setCmsSize("");
            modelCmsBtJmSku.setFormat("");
            modelCmsBtJmSku.setJmSize(modelJmBtSku.getSize());
            modelCmsBtJmSku.setJmSkuNo(modelJmBtSku.getJumeiSkuNo());
            modelCmsBtJmSku.setJmSpuNo(modelJmBtSku.getJumeiSpuNo());
            modelCmsBtJmSku.setProductCode(modelJmBtSku.getProductCode());
            modelCmsBtJmSku.setSkuCode(modelJmBtSku.getSku());
            modelCmsBtJmSku.setMsrpRmb(new BigDecimal(0));
            modelCmsBtJmSku.setMsrpUsd(new BigDecimal(0));
            modelCmsBtJmSku.setRetailPrice(new BigDecimal(0));
            modelCmsBtJmSku.setSalePrice(new BigDecimal(0));
            modelCmsBtJmSku.setUpc(modelJmBtSku.getUpcCode());
            modelCmsBtJmSku.setModifier("system");
            modelCmsBtJmSku.setModified(new Date());
            modelCmsBtJmSku.setCreated(new Date());
            modelCmsBtJmSku.setCreater("system");
            try {
                daoCmsBtJmSku.insert(modelCmsBtJmSku);//CmsBtJmSku
            } catch (org.springframework.dao.DuplicateKeyException ex)//重复 不处理
            {

            }
        }
    }

    void importCmsBtProductModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku) {
        CmsBtProductModel productModel = new CmsBtProductModel();
        productModel.setCatId(CartEnums.Cart.JM.getId());
        productModel.setOrgChannelId(modelJmBtDealImport.getChannelId());


        CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
        productModel.setPlatform(CartEnums.Cart.JM, platform);

        platform.setCartId(CartEnums.Cart.JM.getValue());
        platform.setpBrandId(Integer.toString(modelJmBtProduct.getBrandId()));
        platform.setpBrandName(modelJmBtProduct.getBrandName());
        platform.setpNumIId(modelJmBtDealImport.getJumeiHashId());
        platform.setpProductId(modelJmBtProduct.getJumeiProductId());
        platform.setStatus(CmsConstants.ProductStatus.Approved.name());
        platform.setpPublishTime(modelJmBtDealImport.getCreated().toLocalDateTime().toString());
        platform.setpAttributeStatus("1");
        platform.setpAttributeSetter(modelJmBtDealImport.getCreater());

        //fields
        BaseMongoMap<String, Object> fields = new BaseMongoMap<>();
        fields.setAttribute("productNameCn", modelJmBtProduct.getProductName());
        fields.setAttribute("productNameEn", modelJmBtProduct.getForeignLanguageName());
        fields.setAttribute("productLongName", modelJmBtDealImport.getProductLongName());
        fields.setAttribute("productMediumName", modelJmBtDealImport.getProductMediumName());
        fields.setAttribute("originCn", "");
        fields.setAttribute("beforeDate", "");
        fields.setAttribute("suitPeople", "");
        fields.setAttribute("specialExplain", modelJmBtProduct.getSpecialNote());//特殊说明
        fields.setAttribute("searchMetaTextCustom", modelJmBtDealImport.getSearchMetaTextCustom());
        platform.setFields(fields);

        List<BaseMongoMap<String, Object>> skus = new ArrayList<>();
        BaseMongoMap<String, Object> skuMap = null;
        for (JmBtSkuModel jmBtSkuModel : listModelJmBtSku) {
            skuMap = new BaseMongoMap<String, Object>();
            skus.add(skuMap);
            skuMap.setAttribute("priceRetail", "");  //各平台的销售指导价
            skuMap.setAttribute("priceSale", "");    //中国最终售价
            skuMap.setAttribute("priceChgFlg", "");  //价格变更状态（U/D/XU/XD）
            skuMap.setAttribute("jmSpuNo", jmBtSkuModel.getJumeiSpuNo());
            skuMap.setAttribute("jmSkuNo", jmBtSkuModel.getJumeiSkuNo());
            skuMap.setAttribute("property", "");
            skuMap.setAttribute("attribute", "");
            skuMap.setAttribute("size", jmBtSkuModel.getSize());
            skuMap.setAttribute("skuCode", jmBtSkuModel.getSku());
        }
        platform.setSkus(skus);
        //commSequenceMongoService.getNextSequence(MongoSequenceService.CommSequenceName.CMS_BT_IMAGE_TEMPLATE_ID)
        daoCmsBtProductDao.insert(productModel);
//        * P27  ->聚美
//            * cartId
//            * pCatId
//            * pCatPath
//            * pCatStatus
//            * status  CmsConstants.ProductStatus.Approved
//            * pIsMain
//            * pProductId 1
//            * pNumIId 2
//            * pStatus
//            * pPublishError
//            * pBrandId      3
//            * pBrandName   4
//            * pPublishTime   createDate
//            * pAttributeStatus   :1
//            * pAttributeSetter   creator
//            * pAttributeSetTime createDate
//            * pPriceRetailSt
//            * pPriceRetailEd
//            * pPriceSaleSt
//            * pPriceSaleEd
//       * fields
//            * productNameCn
//            * productNameEn
//            * productLongName
//            * productMediumName
//            * productShortName
//            * originCn
//            * beforeDate
//            * suitPeople
//            * specialExplain
//            * searchMetaTextCustom
//            * skus[]
//        * skuCode
//            * priceRetail ->各平台的销售指导价
//            * priceSale //中国最终售价
//            * priceChgFlg ->价格变更状态（U/D/XU/XD）
//            * jmSpuNo
//            * jmSkuNo
//            * property
//            * attribute
//            * size
//        * sellerCats[]
//            * cId
//            * cIds
//            * cName
//            * cNames
    }
}
