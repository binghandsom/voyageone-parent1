package com.voyageone.service.impl.cms.jumei2;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.components.transaction.VOTransactional;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JMProductDealBean;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtDealImportModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtProductModel;
import com.voyageone.service.bean.cms.businessmodel.JMImportData.JmBtSkuModel;
import com.voyageone.service.dao.cms.CmsBtJmProductDao;
import com.voyageone.service.dao.cms.CmsBtJmSkuDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
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

    @Autowired
    CmsBtProductGroupDao daoCmsBtProductGroup;
//    numIId
//            platformPid
//    publishTime
//            onSaleTime
    @VOTransactional
    public void importJM(String channelId) {
        List<BulkUpdateModel> bulkProductList = new ArrayList<>();
        List<BulkUpdateModel> bulkGroupList = new ArrayList<>();
        //取所有商品最后一次deal信息
        List<JMProductDealBean> listJMProductDealBean = daoExtJmBtDealImport.selectListProductDealByChannelId(channelId);
        System.out.println("productCount:"+listJMProductDealBean.size());
        for (JMProductDealBean productDeal : listJMProductDealBean) {
            importProduct(productDeal, bulkProductList, bulkGroupList);
        }
        System.out.println("productCount end:"+new Date());
        //CmsBtProduct        更新所有
        if (bulkProductList.size() > 0) {
            daoCmsBtProductDao.bulkUpdateWithMap(channelId, bulkProductList, "system", "$set");
        }

        //CmsBtProductGroup   更新所有
        if (bulkGroupList.size() > 0) {
            daoCmsBtProductGroup.bulkUpdateWithMap(channelId, bulkProductList, "system", "$set", false);
        }
    }
    private void importProduct(JMProductDealBean productDeal,List<BulkUpdateModel> bulkProductList, List<BulkUpdateModel> bulkGroupList) {
        JmBtDealImportModel modelJmBtDealImport = daoExtJmBtDealImport.selectJmBtDealImportModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        JmBtProductModel modelJmBtProduct = daoExtJmBtDealImport.selectJmBtProductModel(productDeal.getChannelId(), productDeal.getDealId(), productDeal.getProductCode());
        List<JmBtSkuModel> listModelJmBtSku = daoExtJmBtDealImport.selectListJmBtSkuModel(productDeal.getChannelId(), productDeal.getProductCode());
        if (modelJmBtProduct == null)//不存在 sql可以查询出来  不用处理
        {
            System.out.println("ChannelId:" + productDeal.getChannelId() + " DealId:" + productDeal.getDealId() + "  code:" + productDeal.getProductCode());
            return;
        }

        insertCmsBtJmProduct(modelJmBtDealImport, modelJmBtProduct);

        insertCmsBtJmSkuModel(listModelJmBtSku);


        //初始化CmsBtProduct更新数据
        BulkUpdateModel modelProduct = getBulkUpdateProductModel(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
        bulkProductList.add(modelProduct);

        //初始化CmsBtProductGroup 更新数据
        BulkUpdateModel modelGroup = getBulkUpdateProductGroupModel(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
        bulkGroupList.add(modelGroup);
    }
    private void insertCmsBtJmProduct(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct) {
        CmsBtJmProductModel modelCmsBtJmProduct = new CmsBtJmProductModel();
        modelCmsBtJmProduct.setAddressOfProduce(modelJmBtProduct.getAddressOfProduce());
        modelCmsBtJmProduct.setApplicableCrowd("");
        modelCmsBtJmProduct.setAttribute(modelJmBtProduct.getAttribute());
        modelCmsBtJmProduct.setAvailablePeriod("");
        modelCmsBtJmProduct.setBrandName(modelJmBtProduct.getBrandName());
        modelCmsBtJmProduct.setChannelId(modelJmBtProduct.getChannelId());
        modelCmsBtJmProduct.setColorEn(modelJmBtProduct.getAttribute());
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
    private void insertCmsBtJmSkuModel(List<JmBtSkuModel> listModelJmBtSku) {
        for (JmBtSkuModel modelJmBtSku : listModelJmBtSku) {
            CmsBtJmSkuModel modelCmsBtJmSku = new CmsBtJmSkuModel();
            if(StringUtils.isEmpty(modelJmBtSku.getSku()))
            {
                String aa=modelCmsBtJmSku.getSkuCode();
            }
            modelCmsBtJmSku.setSkuCode(modelJmBtSku.getSku());
            modelCmsBtJmSku.setChannelId(modelJmBtSku.getChannelId());
            modelCmsBtJmSku.setCmsSize(modelJmBtSku.getSize());//modelJmBtSku.getSize();
            modelCmsBtJmSku.setFormat("");
            modelCmsBtJmSku.setJmSize(modelJmBtSku.getSize());
            modelCmsBtJmSku.setJmSkuNo(modelJmBtSku.getJumeiSkuNo());
            modelCmsBtJmSku.setJmSpuNo(modelJmBtSku.getJumeiSpuNo());
            modelCmsBtJmSku.setProductCode(modelJmBtSku.getProductCode());
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

//    void importCmsBtProductModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku) {
//        List<BulkUpdateModel> bulkList = new ArrayList<>();
//        BulkUpdateModel model = getBulkUpdateModels(modelJmBtDealImport, modelJmBtProduct, listModelJmBtSku);
//        bulkList.add(model);
//        daoCmsBtProductDao.bulkUpdateWithMap(modelJmBtProduct.getChannelId(), bulkList, "system", "$set");
//    }
private  BulkUpdateModel getBulkUpdateProductGroupModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku)
{
//    numIId
//            platformPid
//    publishTime
//            onSaleTime
    HashMap<String, Object> updateMap = new HashMap<>();
    updateMap.put("mainProductCode",modelJmBtProduct.getProductCode());

    HashMap<String, Object> queryMap = new HashMap<>();
    queryMap.put("numIId",modelJmBtDealImport.getJumeiHashId());
    queryMap.put("platformPid", modelJmBtProduct.getJumeiProductId());
    queryMap.put("publishTime", modelJmBtProduct.getCreated());
    queryMap.put("onSaleTime", modelJmBtProduct.getCreated());
    BulkUpdateModel model = new BulkUpdateModel();
    model.setUpdateMap(updateMap);
    model.setQueryMap(queryMap);
    //bulkList.add(model);
    return model;
}
    private BulkUpdateModel getBulkUpdateProductModel(JmBtDealImportModel modelJmBtDealImport, JmBtProductModel modelJmBtProduct, List<JmBtSkuModel> listModelJmBtSku) {
        CmsBtProductModel_Platform_Cart platform = new CmsBtProductModel_Platform_Cart();
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
        fields.setAttribute("originCn",modelJmBtProduct.getAddressOfProduce());
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

       // List<BulkUpdateModel> bulkList = new ArrayList<>();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("platforms.P27",platform);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("fields.code",modelJmBtProduct.getProductCode());
        queryMap.put("channelId", modelJmBtProduct.getChannelId());

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        //bulkList.add(model);
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
}
