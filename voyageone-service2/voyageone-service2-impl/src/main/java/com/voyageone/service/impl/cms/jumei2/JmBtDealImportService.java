package com.voyageone.service.impl.cms.jumei2;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.TransactionRunner;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.BigDecimalUtil;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.common.util.excel.ListHelp;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.daoext.cms.JmBtDealImportDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtJmProductModel;
import com.voyageone.service.model.cms.CmsBtJmSkuModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@Service
public class JmBtDealImportService extends BaseService {
    @Autowired
    JmBtDealImportDaoExt daoExtJmBtDealImport;
    @Autowired
    CmsBtJmProductDao daoCmsBtJmProduct;
    @Autowired
    CmsBtJmSkuDao daoCmsBtJmSku;
    @Autowired
    CmsBtProductDao daoCmsBtProductDao;
    @Autowired
    CmsBtProductGroupDao daoCmsBtProductGroup;
    @Autowired
    TransactionRunner transactionRunner;

    @VOTransactional
    public Object importJMOne(String channelId, String code) {
        StringBuilder sbResult = new StringBuilder();
        List<JMProductDealBean> listJMProductDealBean = daoExtJmBtDealImport.selectListProductDealByChannelId(channelId);
        List<JMProductDealBean> page = new ArrayList<>();
        for (JMProductDealBean deal : listJMProductDealBean) {
            if (deal.getProductCode().equals(code)) {
                page.add(deal);
                break;
            }
        }
        importPage(channelId, sbResult, page);
        return null;
    }

    @VOTransactional
    public Object importJM(String channelId) {
        StringBuilder sbResult = new StringBuilder();
        try {
            //取所有商品最后一次deal信息
            List<JMProductDealBean> listJMProductDealBean = daoExtJmBtDealImport.selectListProductDealByChannelId(channelId);
            List<List<JMProductDealBean>> listPage = ListHelp.getPageList(listJMProductDealBean, 1000);
            for (List<JMProductDealBean> page : listPage) {
                importPage(channelId, sbResult, page);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            $error("JmBtDealImportService.importJM error", ex);
            throw new BusinessException("JmBtDealImportService.importJM error", ex);
        }
        $info("JmBtDealImportService.importJM 导入成功");
        $info("JmBtDealImportService.importJM" + sbResult.toString());
        return true;
    }

    private void importPage(String channelId, StringBuilder sbResult, List<JMProductDealBean> listJMProductDealBean) {
        List<BulkUpdateModel> bulkProductList = new ArrayList<>();
        List<BulkUpdateModel> bulkGroupList = new ArrayList<>();
        for (JMProductDealBean productDeal : listJMProductDealBean) {
            importProduct(sbResult, productDeal, bulkProductList, bulkGroupList);
        }

        //CmsBtProduct        更新所有
        if (!bulkProductList.isEmpty()) {
            daoCmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, "system", "$set");
        }
        //CmsBtProductGroup   更新所有
        if (!bulkGroupList.isEmpty()) {
            daoCmsBtProductGroup.bulkUpdateWithMap(channelId, bulkGroupList, "system", "$set", false);
        }
    }

    private void importProduct(StringBuilder sbResult, JMProductDealBean productDeal, List<BulkUpdateModel> bulkProductList, List<BulkUpdateModel> bulkGroupList) {
        JmBtDealImportModel modelJmBtDealImport = daoExtJmBtDealImport.selectJmBtDealImportModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        JmBtProductModel modelJmBtProduct = daoExtJmBtDealImport.selectJmBtProductModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        List<JmBtSkuModel> listModelJmBtSku = daoExtJmBtDealImport.selectListJmBtSkuModel(productDeal.getChannelId(), productDeal.getProductCode());

        //不存在 sql可以查询出来  不用处理
        if (modelJmBtProduct == null) {
            sbResult.append("ChannelId:").append(productDeal.getChannelId()).append(" DealId:").append(productDeal.getDealId()).append("  code:").append(productDeal.getProductCode());
            return;
        }
        CmsBtProductModel modelCmsBtProduct = daoCmsBtProductDao.selectByCode(modelJmBtProduct.getProductCode(), modelJmBtProduct.getChannelId());
        if (modelCmsBtProduct == null) {
            sbResult.append("mongodb不存在 " + "ChannelId:").append(productDeal.getChannelId()).append(" DealId:").append(productDeal.getDealId()).append("  code:").append(productDeal.getProductCode());
            return;
        }
        insertCmsBtJmProduct(modelJmBtDealImport, modelJmBtProduct, modelCmsBtProduct);

        insertCmsBtJmSkuModel(listModelJmBtSku, modelCmsBtProduct);

        //初始化CmsBtProduct更新数据
        BulkUpdateModel modelProduct = getBulkUpdateProductModel(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku, modelCmsBtProduct);
        bulkProductList.add(modelProduct);

        //初始化CmsBtProductGroup 更新数据
        BulkUpdateModel modelGroup = getBulkUpdateProductGroupModel(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
        bulkGroupList.add(modelGroup);
    }

    private void insertCmsBtJmProduct(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, CmsBtProductModel modelCmsBtProduct) {
        List<CmsBtProductModel_Sku> listCmsBtProductModel_Sku = modelCmsBtProduct.getCommon().getSkus();
        CmsBtProductModel_Field commonField = modelCmsBtProduct.getCommon().getFields();

        CmsBtJmProductModel modelCmsBtJmProduct = new CmsBtJmProductModel();
        modelCmsBtJmProduct.setAddressOfProduce(modelJmBtProduct.getAddressOfProduce());
        modelCmsBtJmProduct.setApplicableCrowd("");

        modelCmsBtJmProduct.setAttribute(modelJmBtProduct.getAttribute());
        modelCmsBtJmProduct.setAvailablePeriod("");
        modelCmsBtJmProduct.setBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setOrgChannelId(modelJmBtProduct.getChannelId());
        modelCmsBtJmProduct.setChannelId(modelJmBtProduct.getChannelId());
        modelCmsBtJmProduct.setColorEn(modelJmBtProduct.getAttribute());
        modelCmsBtJmProduct.setForeignLanguageName(modelJmBtProduct.getForeignLanguageName());
        modelCmsBtJmProduct.setHsCode(modelJmBtProduct.getHsCode());
        modelCmsBtJmProduct.setHsName(modelJmBtProduct.getHsName());
        modelCmsBtJmProduct.setHsUnit(modelJmBtProduct.getHsUnit());
//        modelCmsBtJmProduct.setImage1("");modelJmBtProduct.
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        if (commonField != null && !StringUtils.isEmpty(commonField.getMaterialCn())) {
            modelCmsBtJmProduct.setMaterialCn(commonField.getMaterialCn());
        } else {
            modelCmsBtJmProduct.setMaterialCn("");
        }
        if (commonField != null && !StringUtils.isEmpty(commonField.getMaterialEn())) {
            modelCmsBtJmProduct.setMaterialEn(commonField.getMaterialEn());
        } else {
            modelCmsBtJmProduct.setMaterialEn("");
        }
        if (commonField != null && !StringUtils.isEmpty(commonField.getOrigin())) {
            modelCmsBtJmProduct.setOrigin(commonField.getOrigin());
        } else {
            modelCmsBtJmProduct.setOrigin("");
        }
        modelCmsBtJmProduct.setOriginJmHashId(modelJmBtDealImport.getJumeiHashId());
        modelCmsBtJmProduct.setProductCode(modelJmBtProduct.getProductCode());
        modelCmsBtJmProduct.setProductDesCn(modelJmBtProduct.getProductDes());
        modelCmsBtJmProduct.setProductDesEn("");
        modelCmsBtJmProduct.setJumeiProductId(modelJmBtProduct.getJumeiProductId());
        modelCmsBtJmProduct.setProductLongName(modelJmBtDealImport.getProductLongName());
        modelCmsBtJmProduct.setProductMediumName(modelJmBtDealImport.getProductMediumName());
        modelCmsBtJmProduct.setProductNameCn(modelJmBtProduct.getProductName());
        modelCmsBtJmProduct.setProductShortName(modelJmBtDealImport.getProductShortName());
        if (commonField != null && !StringUtils.isEmpty(commonField.getProductType())) {
            modelCmsBtJmProduct.setProductType(commonField.getProductType());
        } else {
            modelCmsBtJmProduct.setProductType("");
        }
        modelCmsBtJmProduct.setRetailPrice(new BigDecimal(0));
        modelCmsBtJmProduct.setSalePrice(new BigDecimal(0));
        modelCmsBtJmProduct.setMsrpRmb(new BigDecimal(0));
        modelCmsBtJmProduct.setMsrpUsd(new BigDecimal(0));
        if (listCmsBtProductModel_Sku != null && !listCmsBtProductModel_Sku.isEmpty()) {
            CmsBtProductModel_Sku sku = listCmsBtProductModel_Sku.get(0);
            modelCmsBtJmProduct.setMsrpRmb(BigDecimalUtil.getValue(sku.getPriceMsrp()));//priceMsrp
            modelCmsBtJmProduct.setMsrpUsd(BigDecimalUtil.getValue(sku.getClientMsrpPrice()));//clientMsrpPrice
            modelCmsBtJmProduct.setRetailPrice(BigDecimalUtil.getValue(sku.getPriceRetail()));//priceRetail
            modelCmsBtJmProduct.setSalePrice(BigDecimalUtil.getValue(sku.getPriceRetail()));//priceRetail
        }
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
        } catch (org.springframework.dao.DuplicateKeyException ex) {
            //重复 不处理
            $warn("该数据已经存在,不再重复插入:" + modelJmBtProduct.getChannelId() + "-" + modelCmsBtJmProduct.getProductCode());
        }
    }

    private void insertCmsBtJmSkuModel(List<JmBtSkuModel> listModelJmBtSku, CmsBtProductModel modelCmsBtProduct) {
        List<CmsBtProductModel_Sku> listCmsBtProductModel_Sku = modelCmsBtProduct.getCommon().getSkus();

        for (JmBtSkuModel modelJmBtSku : listModelJmBtSku) {
            CmsBtProductModel_Sku cmsBtProductModel_Sku = getCmsBtProductModel_Sku(modelJmBtSku.getSku(), listCmsBtProductModel_Sku);
            if (cmsBtProductModel_Sku == null) continue;
            CmsBtJmSkuModel modelCmsBtJmSku = new CmsBtJmSkuModel();
//            if (StringUtils.isEmpty(modelJmBtSku.getSku())) {
//                String aa = modelCmsBtJmSku.getSkuCode();
//            }
            modelCmsBtJmSku.setSkuCode(modelJmBtSku.getSku());
            modelCmsBtJmSku.setOrgChannelId(modelJmBtSku.getChannelId());
            modelCmsBtJmSku.setChannelId(modelJmBtSku.getChannelId());
            modelCmsBtJmSku.setCmsSize(modelJmBtSku.getSize());//modelJmBtSku.getSize();
            modelCmsBtJmSku.setFormat("");
            modelCmsBtJmSku.setJmSize(modelJmBtSku.getSize());
            modelCmsBtJmSku.setJmSkuNo(modelJmBtSku.getJumeiSkuNo());
            modelCmsBtJmSku.setJmSpuNo(modelJmBtSku.getJumeiSpuNo());
            modelCmsBtJmSku.setProductCode(modelJmBtSku.getProductCode());
//            * clientMsrpPrice     ->客户建议零售价
//                    * clientRetailPrice   ->客户指导价
//                    * clientNetPrice      ->客户成本价
//                    * priceMsrp           ->中国建议零售价
//                    * priceRetail         ->中国指导价

            modelCmsBtJmSku.setMsrpRmb(BigDecimalUtil.getValue(cmsBtProductModel_Sku.getPriceMsrp()));//priceMsrp
            modelCmsBtJmSku.setMsrpUsd(BigDecimalUtil.getValue(cmsBtProductModel_Sku.getClientMsrpPrice()));//clientMsrpPrice
            modelCmsBtJmSku.setRetailPrice(BigDecimalUtil.getValue(cmsBtProductModel_Sku.getPriceRetail()));//priceRetail
            modelCmsBtJmSku.setSalePrice(BigDecimalUtil.getValue(cmsBtProductModel_Sku.getPriceRetail()));//priceRetail
            modelCmsBtJmSku.setUpc(modelJmBtSku.getUpcCode());
            modelCmsBtJmSku.setModifier("system");
            modelCmsBtJmSku.setModified(new Date());
            modelCmsBtJmSku.setCreated(new Date());
            modelCmsBtJmSku.setCreater("system");
            try {
                daoCmsBtJmSku.insert(modelCmsBtJmSku);//CmsBtJmSku
            } catch (org.springframework.dao.DuplicateKeyException ex) {
                //重复 不处理
                $warn("该数据已经存在,不再重复插入:" + modelCmsBtJmSku.getChannelId() + "-" + modelCmsBtJmSku.getSkuCode());
            }
        }
    }

    private CmsBtProductModel_Sku getCmsBtProductModel_Sku(String skuCode, List<CmsBtProductModel_Sku> listCmsBtProductModel_Sku) {

        Stream<CmsBtProductModel_Sku> resultList = listCmsBtProductModel_Sku.stream().filter(o -> o.getSkuCode().equals(skuCode));
        return resultList.findFirst().orElse(null);
    }

    //    void importCmsBtProductModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku) {
//        List<BulkUpdateModel> bulkList = new ArrayList<>();
//        BulkUpdateModel model = getBulkUpdateModels(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
//        bulkList.add(model);
//        daoCmsBtProductDao.bulkUpdateWithMap(modelJmBtProduct.getChannelId(), bulkList, "system", "$set");
//    }
    private BulkUpdateModel getBulkUpdateProductGroupModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku) {
//    numIId
//            platformPid
//    publishTime
//            onSaleTime
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("mainProductCode", modelJmBtProduct.getProductCode());
        queryMap.put("cartId", 27);


        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("numIId", modelJmBtDealImport.getJumeiHashId());
        updateMap.put("platformPid", modelJmBtProduct.getJumeiProductId());
        updateMap.put("publishTime", DateTimeUtil.getDateTime(modelJmBtProduct.getCreated(), null));
        updateMap.put("onSaleTime", DateTimeUtil.getDateTime(modelJmBtProduct.getCreated(), null));
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
        updateMap.put("platformStatus", CmsConstants.PlatformStatus.InStock.name());
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        //bulkList.add(model);
        return model;
    }

    private BulkUpdateModel getBulkUpdateProductModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku, CmsBtProductModel modelCmsBtProduct) {

//        1.pCatId未设置 处理
//        2.pCatPath未设置     ? category_lv4_id fullPath  >  处理
//        3.pCatStatus未设置   1 处理
//        4.pIsMain被覆盖成空  ? 1 处理
//        5.pAttributeSetTime未设置      处理
//        6.pPriceMsrpSt等被覆盖成空        处理
//        7.fields.productShortName 未空？  处理
//        8.skus.priceMsrp等被覆盖成空     处理

        CmsBtProductModel_Platform_Cart platform = modelCmsBtProduct.getPlatform(CartEnums.Cart.JM.getValue());// new CmsBtProductModel_Platform_Cart();
        platform.setCartId(CartEnums.Cart.JM.getValue());
        platform.setpCatId(String.valueOf(modelJmBtProduct.getCategoryLv4Id()));
        if (modelJmBtProduct.getCategoryLv4Id() != 0) {
            String catPath = daoExtJmBtDealImport.selectCategoryFullPath(modelJmBtProduct.getCategoryLv4Id());
            if (!StringUtils.isEmpty(catPath)) {
                platform.setpCatPath(catPath);
            } else {
                $info("JmBtDealImportService.getBulkUpdateProductModel selectCategoryFullPath categoryLv4Id 未找到");
            }
        }
        platform.setpCatStatus("1");
        platform.setpIsMain(1);
        platform.setpAttributeSetTime(DateTimeUtil.getDateTime(modelJmBtProduct.getCreated(), null));
        platform.setpBrandId(Integer.toString(modelJmBtProduct.getBrandId()));
        platform.setpBrandName(modelJmBtProduct.getBrandName());
        platform.setpNumIId(modelJmBtDealImport.getJumeiHashId());
        platform.setpProductId(modelJmBtProduct.getJumeiProductId());
        platform.setStatus(CmsConstants.ProductStatus.Approved.name());
        platform.setpPublishTime(DateTimeUtil.getDateTime(modelJmBtProduct.getCreated(), null));
        platform.setpAttributeStatus("1");
        platform.setpAttributeSetter(modelJmBtDealImport.getCreater());
        platform.setpStatus(CmsConstants.PlatformStatus.InStock);

        //fields
        BaseMongoMap<String, Object> fields = platform.getFields() == null ? new BaseMongoMap<>() : platform.getFields();
        // new BaseMongoMap<>();
        fields.setAttribute("productNameCn", modelJmBtDealImport.getProductLongName());
        fields.setAttribute("productNameEn", modelJmBtProduct.getForeignLanguageName());
        fields.setAttribute("productLongName", modelJmBtDealImport.getProductLongName());
        fields.setAttribute("productMediumName", modelJmBtDealImport.getProductMediumName());
        fields.setAttribute("productShortName", modelJmBtDealImport.getProductShortName());
        fields.setAttribute("originCn", modelJmBtProduct.getAddressOfProduce());
        fields.setAttribute("beforeDate", "无");
        fields.setAttribute("suitPeople", "时尚潮流人士");
        fields.setAttribute("userPurchaseLimit", "0");
        fields.setAttribute("specialExplain", modelJmBtProduct.getSpecialNote());//特殊说明
        fields.setAttribute("searchMetaTextCustom", modelJmBtDealImport.getSearchMetaTextCustom());
        fields.setAttribute("attribute", modelJmBtProduct.getAttribute());
        if (platform.getFields() == null)
            platform.setFields(fields);

        List<BaseMongoMap<String, Object>> skus = platform.getSkus(); //new ArrayList<>();
        BaseMongoMap<String, Object> skuMap;
        for (JmBtSkuModel jmBtSkuModel : listModelJmBtSku) {
            skuMap = getMongoSku(skus, jmBtSkuModel.getSku()); //new BaseMongoMap<String, Object>();
            if (skuMap == null) {
                $error("code:" + modelJmBtDealImport.getProductCode() + " skuCode:" + jmBtSkuModel.getSku() + "mongo不存在");
                continue;
            }
            skuMap.setAttribute("jmSpuNo", jmBtSkuModel.getJumeiSpuNo());
            skuMap.setAttribute("jmSkuNo", jmBtSkuModel.getJumeiSkuNo());
            skuMap.setAttribute("property", "OTHER");
            skuMap.setAttribute("size", jmBtSkuModel.getSize());
        }

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("platforms.P27", platform);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", modelJmBtProduct.getProductCode());
        queryMap.put("channelId", modelJmBtProduct.getChannelId());

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
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

    BaseMongoMap<String, Object> getMongoSku(List<BaseMongoMap<String, Object>> skus, String skuCode) {
        if (skus == null)
            return null;
        Stream<BaseMongoMap<String, Object>> resultList = skus.stream().filter(o -> o.getStringAttribute("skuCode").equals(skuCode));
        return resultList.findFirst().orElse(null);
    }
}
