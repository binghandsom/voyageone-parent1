package com.voyageone.service.impl.cms.product;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.MongoUtils;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.dao.cms.mongo.CmsBtProductGroupDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 *  Product Group Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ProductSkuService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;
    @Autowired
    private CmsBtProductGroupDao cmsBtProductGroupDao;

    /**
     * save Skus
     */
    public void saveSkus(String channelId,  long productId, List<CmsBtProductModel_Sku> skus) {
        List<BulkUpdateModel> bulkInsertList = new ArrayList<>();
        List<BulkUpdateModel> bulkUpdateList = new ArrayList<>();

        saveSkusAddBlukUpdateModel(channelId, productId, null, skus, bulkInsertList, bulkUpdateList);

        if (!bulkInsertList.isEmpty()) {
            BasicDBObject queryObj = (BasicDBObject)bulkInsertList.get(0).getQueryMap();
            BasicDBList skusList = new BasicDBList();
            for (BulkUpdateModel bulkInsert : bulkInsertList) {
                skusList.add(bulkInsert.getUpdateMap());
            }
            BasicDBObject skusObj = new BasicDBObject().append("skus", skusList);
            BasicDBObject pushObj = new BasicDBObject().append("$pushAll", skusObj);
            cmsBtProductDao.getDBCollection(channelId).update(queryObj, pushObj);
        }

        if (!bulkUpdateList.isEmpty()) {
            cmsBtProductDao.bulkUpdateWithMap(channelId, bulkUpdateList, null, "$set");
        }
    }

    /**
     * saveSkusAddBlukUpdateModel
     * @param channelId channel ID
     * @param productId product Id
     * @param productCode product Code
     * @param models CmsBtProductModel_Sku
     * @param bulkUpdateList List
     * @param bulkInsertList List
     */
    private void saveSkusAddBlukUpdateModel(String channelId,
                                            Long productId, String productCode,
                                            List<CmsBtProductModel_Sku> models,
                                            List<BulkUpdateModel> bulkInsertList, List<BulkUpdateModel> bulkUpdateList) {
        if (models == null || models.isEmpty()) {
            return;
        }

        if (productId == null && StringUtils.isEmpty(productCode)) {
            throw new RuntimeException("productId or productCode not found!");
        }

        CmsBtProductModel findModel = null;
        JongoQuery queryObject = new JongoQuery();
        queryObject.setProjectionExt("skus.skuCode");
        if (productId != null) {
            queryObject.setQuery("{\"prodId\":" + productId + "}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (StringUtils.isEmpty(productCode)) {
            queryObject.setQuery("{\"common.fields.code\":\"" + productCode + "\"}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (findModel != null) {
            Set<String> findSkuSet = new HashSet<>();
            if (findModel.getCommon().getSkus() != null) {
                for (CmsBtProductModel_Sku findSku : findModel.getCommon().getSkus()) {
                    if (findSku != null && findSku.getSkuCode() != null) {
                        findSkuSet.add(findSku.getSkuCode());
                    }
                }
            }

            for (CmsBtProductModel_Sku model : models) {
                if (model == null) {
                    continue;
                }
                BasicDBObject queryMap = new BasicDBObject();
                if (productId != null) {
                    queryMap.append("prodId", productId);
                } else {
                    queryMap.append("common.fields.code", productCode);
                }
                if (StringUtils.isEmpty(model.getSkuCode())) {
                    throw new RuntimeException("SkuCode not found!");
                }
                if (findSkuSet.contains(model.getSkuCode())) {
                    queryMap.put("skus.skuCode", model.getSkuCode());

                    BasicDBObject dbObject = model.toUpdateBasicDBObject("skus.$.");

                    if (!dbObject.isEmpty()) {
                        BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                        skuUpdateModel.setUpdateMap(dbObject);
                        skuUpdateModel.setQueryMap(queryMap);

                        bulkUpdateList.add(skuUpdateModel);
                    }
                } else {
                    BasicDBObject dbObject = model.toUpdateBasicDBObject("");
                    if (!dbObject.isEmpty()) {
                        BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                        skuUpdateModel.setUpdateMap(dbObject);
                        skuUpdateModel.setQueryMap(queryMap);
                        bulkInsertList.add(skuUpdateModel);
                    }
                }
            }
        }
    }

    /**
     * update Prices
     */
    public int updatePrices(String channelId, List<ProductPriceBean> productPrices, String modifier) {

        if (productPrices == null || productPrices.isEmpty()) {
            throw new RuntimeException("ProductPrices not found!");
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();

        for (ProductPriceBean model : productPrices) {
            if (model == null) {
                throw new RuntimeException("ProductPrices not found!");
            }
            if (model.getProductId() == null && model.getProductCode() == null) {
                throw new RuntimeException("ProductPrices ProductId or ProductCode not found!");
            }
            updatePricesAddBlukUpdateModel(channelId, model, bulkList, modifier);
        }

        int result = 0;
        // 更新sku价格变更
        if (!bulkList.isEmpty()) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
            result = bulkWriteResult.getInsertedCount() + bulkWriteResult.getModifiedCount();

            updateGroupPrice(channelId, productPrices);
        }

        return result;
    }

    /**
     *  更新group里的价格
     */
    private void updateNewGroupPrice(String channelId, Long ProdId, String modifier) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        // 先根据产品id找到产品code
        CmsBtProductModel findModel = cmsBtProductDao.selectOneWithQuery("{\"prodId\":" + ProdId + "}", channelId);
        // 再根据产品code从group表中找出其所在group的信息
        List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select("{\"productCodes\":\"" + findModel.getCommon().getFields().getCode() + "\"},{\"productCodes\":1,\"groupId\":1}", channelId);
        grpList.forEach(grpObj -> {
            // 其所在group下的所有产品code
            List<String> codeList = grpObj.getProductCodes();
            if (codeList != null && !codeList.isEmpty()) {
                // 再找到所有产品fields信息
                String[] codeArr = new String[codeList.size()];
                codeArr = codeList.toArray(codeArr);
                List<CmsBtProductModel> prodList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "},{\"common.fields\":1,\"platform\":1}", channelId);
                bulkList.add(calculateNewPriceRange(prodList, grpObj, modifier));
            }
        });
        if (!bulkList.isEmpty()) {
            cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, null, "$set", false);
        }
    }

    /**
     *  更新group里的价格
     */
    private void updateGroupPrice(String channelId, List<ProductPriceBean> productPrices) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();
        for (ProductPriceBean model : productPrices) {
            // 先根据产品id找到产品code
            CmsBtProductModel findModel = cmsBtProductDao.selectOneWithQuery("{\"prodId\":" + model.getProductId() + "},{\"common.fields.code\":1}", channelId);
            // 再根据产品code从group表中找出其所在group的信息
            List<CmsBtProductGroupModel> grpList = cmsBtProductGroupDao.select("{\"productCodes\":\"" + findModel.getCommon().getFields().getCode() + "\"},{\"productCodes\":1,\"groupId\":1}", channelId);
            grpList.forEach(grpObj -> {
                // 其所在group下的所有产品code
                List<String> codeList = grpObj.getProductCodes();
                if (codeList != null && !codeList.isEmpty()) {
                    // 再找到所有产品fields信息
                    String[] codeArr = new String[codeList.size()];
                    codeArr = codeList.toArray(codeArr);
                    List<CmsBtProductModel> prodList = cmsBtProductDao.select("{" + MongoUtils.splicingValue("common.fields.code", codeArr, "$in") + "},{\"common.fields\":1}", channelId);
                    bulkList.add(calculatePriceRange(prodList, grpObj.getGroupId()));
                }
            });
        }
        if (!bulkList.isEmpty()) {
            cmsBtProductGroupDao.bulkUpdateWithMap(channelId, bulkList, null, "$set", false);
        }
    }

    // 计算价格区间
    private BulkUpdateModel calculatePriceRange(List<CmsBtProductModel> products, Long groupId) {
        Double priceSaleSt = null;
        Double priceSaleEd = null;
        Double priceRetailSt = null;
        Double priceRetailEd = null;
        Double priceMsrpSt = null;
        Double priceMsrpEd = null;

        for (CmsBtProductModel product : products) {
//            if (priceSaleSt == null || product.getCommon().getFields().getPriceSaleSt() < priceSaleSt) {
//                priceSaleSt = product.getCommon().getFields().getPriceSaleSt();
//            }
//            if (priceSaleEd == null || product.getFields().getPriceSaleEd() > priceSaleEd) {
//                priceSaleEd = product.getFields().getPriceSaleEd();
//            }

            if (priceRetailSt == null || product.getCommon().getFields().getPriceRetailSt() < priceRetailSt) {
                priceRetailSt = product.getCommon().getFields().getPriceRetailSt();
            }
            if (priceRetailEd == null || product.getCommon().getFields().getPriceRetailEd() > priceRetailEd) {
                priceRetailEd = product.getCommon().getFields().getPriceRetailEd();
            }

            if (priceMsrpSt == null || product.getCommon().getFields().getPriceMsrpSt() < priceMsrpSt) {
                priceMsrpSt = product.getCommon().getFields().getPriceMsrpSt();
            }
            if (priceMsrpEd == null || product.getCommon().getFields().getPriceMsrpEd() > priceMsrpEd) {
                priceMsrpEd = product.getCommon().getFields().getPriceMsrpEd();
            }
        }

        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("priceSaleSt", priceSaleSt);
        updateMap.put("priceSaleEd", priceSaleEd);
        updateMap.put("priceRetailSt", priceRetailSt);
        updateMap.put("priceRetailEd", priceRetailEd);
        updateMap.put("priceMsrpSt", priceMsrpSt);
        updateMap.put("priceMsrpEd", priceMsrpEd);

        BulkUpdateModel bulk = new BulkUpdateModel();
        bulk.setUpdateMap(updateMap);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", groupId);
        bulk.setQueryMap(queryMap);

        return bulk;
    }

    // 计算价格区间
    private BulkUpdateModel calculateNewPriceRange(List<CmsBtProductModel> products, CmsBtProductGroupModel group, String modifier) {
        Double priceSaleSt = null;
        Double priceSaleEd = null;
        Double priceRetailSt = null;
        Double priceRetailEd = null;
        Double priceMsrpSt = null;
        Double priceMsrpEd = null;

        for (CmsBtProductModel product : products) {
            // update desmond 2016/07/01 start
            // 删除group.getCartId() == 23的这种情况
            // TODO 0630前暂时的方案，之后会把group.getCartId() == 23的这种情况删除
//            if (group.getCartId() != 23) {
                for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
                    if (group.getCartId() == entry.getValue().getCartId()) {
                        if (priceSaleSt == null || entry.getValue().getpPriceSaleSt() < priceSaleSt) {
                            priceSaleSt = entry.getValue().getpPriceSaleSt();
                        }
                        if (priceSaleEd == null || entry.getValue().getpPriceSaleEd() > priceSaleEd) {
                            priceSaleEd = entry.getValue().getpPriceSaleEd();
                        }
                    }
                }
//            } else{
//                for (CmsBtProductModel_Sku sku : product.getCommon().getSkus()) {
//                    if (priceSaleSt == null || Double.parseDouble(String.valueOf(sku.get("priceSale"))) < priceSaleSt) {
//                        priceSaleSt = Double.parseDouble(String.valueOf(sku.get("priceSale")));
//                    }
//                    if (priceSaleEd == null || Double.parseDouble(String.valueOf(sku.get("priceSale"))) > priceSaleEd) {
//                        priceSaleEd = Double.parseDouble(String.valueOf(sku.get("priceSale")));
//                    }
//                }
//            }

            if (group.getCartId() == 0 || group.getCartId() == 1) {
                if (product.getCommon() != null && product.getCommon().getFields() != null && product.getCommon().getFields().getPriceRetailSt() != null) {
                    if (priceRetailSt == null || product.getCommon().getFields().getPriceRetailSt() < priceRetailSt) {
                        priceRetailSt = product.getCommon().getFields().getPriceRetailSt();
                    }
                }
                if (product.getCommon() != null && product.getCommon().getFields() != null && product.getCommon().getFields().getPriceRetailEd() != null) {
                    if (priceRetailEd == null || product.getCommon().getFields().getPriceRetailEd() > priceRetailEd) {
                        priceRetailEd = product.getCommon().getFields().getPriceRetailEd();
                    }
                }
            } else {
                // TODO 0630前暂时的方案，之后会把group.getCartId() == 23的这种情况删除
//                if (group.getCartId() != 23) {
                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
                        if (group.getCartId() == entry.getValue().getCartId()) {
                            if (priceRetailSt == null || entry.getValue().getpPriceRetailSt() < priceRetailSt) {
                                priceRetailSt = entry.getValue().getpPriceRetailSt();
                            }
                            if (priceRetailEd == null || entry.getValue().getpPriceRetailEd() > priceRetailEd) {
                                priceRetailEd = entry.getValue().getpPriceRetailEd();
                            }
                        }
                    }
//                } else{
//                    for (CmsBtProductModel_Sku sku : product.getCommon().getSkus()) {
//                        if (priceRetailSt == null || Double.parseDouble(String.valueOf(sku.get("priceRetail"))) < priceRetailSt) {
//                            priceRetailSt = Double.parseDouble(String.valueOf(sku.get("priceRetail")));
//                        }
//                        if (priceRetailEd == null || Double.parseDouble(String.valueOf(sku.get("priceRetail"))) > priceRetailEd) {
//                            priceRetailEd = Double.parseDouble(String.valueOf(sku.get("priceRetail")));
//                        }
//                    }
//                }
            }

            if (group.getCartId() == 0 || group.getCartId() == 1) {
                if (product.getCommon() != null && product.getCommon().getFields() != null && product.getCommon().getFields().getPriceMsrpSt() != null) {
                    if (priceMsrpSt == null || product.getCommon().getFields().getPriceMsrpSt() < priceMsrpSt) {
                        priceMsrpSt = product.getCommon().getFields().getPriceMsrpSt();
                    }
                }
                if (product.getCommon() != null && product.getCommon().getFields() != null && product.getCommon().getFields().getPriceMsrpEd() != null) {
                    if (priceMsrpEd == null || product.getCommon().getFields().getPriceMsrpEd() > priceMsrpEd) {
                        priceMsrpEd = product.getCommon().getFields().getPriceMsrpEd();
                    }
                }
            } else {
                // TODO 0630前暂时的方案，之后会把group.getCartId() == 23的这种情况删除
//                if (group.getCartId() != 23) {
                    for (Map.Entry<String, CmsBtProductModel_Platform_Cart> entry : product.getPlatforms().entrySet()) {
                        if (group.getCartId() == entry.getValue().getCartId()) {
                            if (priceMsrpSt == null || entry.getValue().getpPriceMsrpSt() < priceMsrpSt) {
                                priceMsrpSt = entry.getValue().getpPriceMsrpSt();
                            }
                            if (priceMsrpEd == null || entry.getValue().getpPriceMsrpEd() > priceMsrpEd) {
                                priceMsrpEd = entry.getValue().getpPriceMsrpEd();
                            }
                        }
                    }
//                } else{
//                    for (CmsBtProductModel_Sku sku : product.getCommon().getSkus()) {
//                        if (priceMsrpSt == null || Double.parseDouble(String.valueOf(sku.get("priceMsrp"))) < priceMsrpSt) {
//                            priceMsrpSt = Double.parseDouble(String.valueOf(sku.get("priceMsrp")));
//                        }
//                        if (priceMsrpEd == null || Double.parseDouble(String.valueOf(sku.get("priceMsrp"))) > priceMsrpEd) {
//                            priceMsrpEd = Double.parseDouble(String.valueOf(sku.get("priceMsrp")));
//                        }
//                    }
//                }
                // update desmond 2016/07/01 end
            }
        }

        Map<String, Object> updateMap = new HashMap<>();
        if (group.getCartId() == 0 || group.getCartId() == 1) {
            updateMap.put("priceSaleSt", new Double("0.0"));
            updateMap.put("priceSaleEd", new Double("0.0"));
        } else {
            updateMap.put("priceSaleSt", priceSaleSt);
            updateMap.put("priceSaleEd", priceSaleEd);
        }
        updateMap.put("priceRetailSt", priceRetailSt);
        updateMap.put("priceRetailEd", priceRetailEd);
        updateMap.put("priceMsrpSt", priceMsrpSt);
        updateMap.put("priceMsrpEd", priceMsrpEd);

        updateMap.put("modifier", modifier);
        updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
        BulkUpdateModel bulk = new BulkUpdateModel();
        bulk.setUpdateMap(updateMap);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groupId", group.getGroupId());
        bulk.setQueryMap(queryMap);

        return bulk;
    }

    /**
     * 批量更新价格信息 根据CodeList
     */
    private void updatePricesAddBlukUpdateModel(String channelId, ProductPriceBean model,
                                               // List<BulkUpdateModel> bulkList, List<CmsBtPriceLogModel> logList,
                                                List<BulkUpdateModel> bulkList,
                                               String modifier) {

        // 取得更新前的sku数据
        HashMap<String, Object> productQueryMap = new HashMap<>();
        CmsBtProductModel findModel;
        JongoQuery queryObject = new JongoQuery();
        queryObject.setProjectionExt("prodId", "fields", "skus");
        if (model.getProductId() != null) {
            productQueryMap.put("prodId", model.getProductId());
            queryObject.setQuery("{\"prodId\":" + model.getProductId() + "}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else {
            productQueryMap.put("common.fields.code", model.getProductCode());
            queryObject.setQuery("{\"common.fields.code\":\"" + model.getProductCode() + "\"}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (findModel == null || findModel.getCommon().getSkus() == null) {
            return;
        }
        List<CmsBtProductModel_Sku> findSkuList = findModel.getCommon().getSkus();

        //Sku price set
        List<Double> msrpPriceList = new ArrayList<>();
        List<Double> retailPriceList = new ArrayList<>();
        List<Double> salePriceList = new ArrayList<>();

        if (model.getSkuPrices() != null && !model.getSkuPrices().isEmpty()) {

            // 循环原始sku列表
            for (CmsBtProductModel_Sku skuModelBefore : findSkuList) {
                boolean isFindSku = false;

                // 循环变更sku列表
                for (ProductSkuPriceBean skuModel : model.getSkuPrices()) {
                    if (skuModel.getSkuCode().equals(skuModelBefore.getSkuCode())) {
                        // 判断价格是否发生变化
                        if (isPriceChanged(skuModelBefore, skuModel)) {

                            HashMap<String, Object> skuQueryMap = new HashMap<>();
                            if (model.getProductId() != null) {
                                skuQueryMap.put("prodId", model.getProductId());
                            } else {
                                skuQueryMap.put("fields.code", model.getProductCode());
                            }
                            if (StringUtils.isEmpty(skuModel.getSkuCode())) {
                                throw new RuntimeException("SkuPrices.SkuCode not found!");
                            }
                            skuQueryMap.put("skus.skuCode", skuModel.getSkuCode());

                            HashMap<String, Object> updateMap = new HashMap<>();
                            if (skuModel.getPriceMsrp() != null) {
                                updateMap.put("skus.$.priceMsrp", skuModel.getPriceMsrp());
                                msrpPriceList.add(skuModel.getPriceMsrp());
                            }
                            if (skuModel.getPriceRetail() != null) {
                                updateMap.put("skus.$.priceRetail", skuModel.getPriceRetail());
                                retailPriceList.add(skuModel.getPriceRetail());
                            }
                            if (skuModel.getPriceSale() != null) {
                                updateMap.put("skus.$.priceSale", skuModel.getPriceSale());
                                salePriceList.add(skuModel.getPriceSale());
                            }

                            //vendor price update
                            if (skuModel.getClientNetPrice() != null) {
                                updateMap.put("skus.$.client_net_price", skuModel.getClientNetPrice());
                            }
                            if (skuModel.getClientMsrpPrice() != null) {
                                updateMap.put("skus.$.client_msrp_price", skuModel.getClientMsrpPrice());
                            }
                            if (skuModel.getClientRetailPrice() != null) {
                                updateMap.put("skus.$.client_retail_price", skuModel.getClientRetailPrice());
                            }

                            // 涨价降价标志
                            if (skuModel.getPriceChgFlg() != null) {
                                updateMap.put("skus.$.priceChgFlg", skuModel.getPriceChgFlg());
                            }

                            if (!updateMap.isEmpty()) {
                                BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                                skuUpdateModel.setUpdateMap(updateMap);
                                skuUpdateModel.setQueryMap(skuQueryMap);

                                bulkList.add(skuUpdateModel);
                            }
                        }
                        // 如果价格未发生变化,则设置isFindSku标示为true
                        else {
                            isFindSku = true;
                        }
                        break;
                    }
                }

                // 如果价格没有没有发生变化则插入当前未变化sku的原始价格
                if (isFindSku) {
                    if (skuModelBefore.getPriceMsrp() != null) {
                        msrpPriceList.add(skuModelBefore.getPriceMsrp());
                    }
                    if (skuModelBefore.getPriceRetail() != null) {
                        retailPriceList.add(skuModelBefore.getPriceRetail());
                    }
//                    if (skuModelBefore.getPriceSale() != null) {
//                        salePriceList.add(skuModelBefore.getPriceSale());
//                    }
                }
            }
        }

        //get price price
        Map<String, Object> resultField = getNewPriceWithFields(findModel.getCommon().getFields(), msrpPriceList, retailPriceList, salePriceList);

        HashMap<String, Object> productUpdateMap = new HashMap<>();
        if (model.getPriceChange() != null) {
            productUpdateMap.put("fields.priceChange", model.getPriceChange());
        }

        //update product price
        if ((boolean)resultField.get("isChanged")) {
            if (resultField.get("priceMsrpSt") != null) {
                productUpdateMap.put("fields.priceMsrpSt", resultField.get("priceMsrpSt"));
            }
            if (resultField.get("priceMsrpEd") != null) {
                productUpdateMap.put("fields.priceMsrpEd", resultField.get("priceMsrpEd"));
            }
            if (resultField.get("priceRetailSt") != null) {
                productUpdateMap.put("fields.priceRetailSt", resultField.get("priceRetailSt"));
            }
            if (resultField.get("priceRetailEd") != null) {
                productUpdateMap.put("fields.priceRetailEd", resultField.get("priceRetailEd"));
            }
            if (resultField.get("priceSaleSt") != null) {
                productUpdateMap.put("fields.priceSaleSt", resultField.get("priceSaleSt"));
            }
            if (resultField.get("priceSaleEd") != null) {
                productUpdateMap.put("fields.priceSaleEd", resultField.get("priceSaleEd"));
            }
        }

        if (!productUpdateMap.isEmpty()) {
            BulkUpdateModel productUpdateModel = new BulkUpdateModel();
            productUpdateModel.setUpdateMap(productUpdateMap);
            productUpdateModel.setQueryMap(productQueryMap);

            bulkList.add(productUpdateModel);
        }
    }

    /**
     * 判断价格是否变更
     * @param skuBefore 变更前SKU信息
     * @param skuAfter 变更后SKU信息
     * @return true:有变更；false：没有变更
     */
    private boolean isPriceChanged(CmsBtProductModel_Sku skuBefore, ProductSkuPriceBean skuAfter) {
        BigDecimal clientPriceMsrpBefore = null;
        if (skuBefore.getClientMsrpPrice() != null) {
            clientPriceMsrpBefore = new BigDecimal(skuBefore.getClientMsrpPrice());
        }
        BigDecimal clientPriceRetailBefore = null;
        if (skuBefore.getClientRetailPrice() != null) {
            clientPriceRetailBefore = new BigDecimal(skuBefore.getClientRetailPrice());
        }
        BigDecimal clientPriceNetBefore = null;
        if (skuBefore.getClientNetPrice() != null) {
            clientPriceNetBefore = new BigDecimal(skuBefore.getClientNetPrice());
        }

        BigDecimal clientPriceMsrpAfter = null;
        if (skuAfter.getClientMsrpPrice() != null) {
            clientPriceMsrpAfter = new BigDecimal(skuAfter.getClientMsrpPrice());
        }
        BigDecimal clientPriceRetailAfter = null;
        if (skuAfter.getClientRetailPrice() != null) {
            clientPriceRetailAfter = new BigDecimal(skuAfter.getClientRetailPrice());
        }
        BigDecimal clientPriceNetAfter = null;
        if (skuAfter.getClientNetPrice() != null) {
            clientPriceNetAfter = new BigDecimal(skuAfter.getClientNetPrice());
        }

        BigDecimal priceMsrpBefore = null;
        if (skuBefore.getPriceMsrp() != null) {
            priceMsrpBefore = new BigDecimal(skuBefore.getPriceMsrp());
        }
        BigDecimal priceRetailBefore = null;
        if (skuBefore.getPriceRetail() != null) {
            priceRetailBefore = new BigDecimal(skuBefore.getPriceRetail());
        }
//        BigDecimal priceSaleBefore = null;
//        if (skuBefore.getPriceSale() != null) {
//            priceSaleBefore = new BigDecimal(skuBefore.getPriceSale());
//        }

        BigDecimal priceMsrpAfter = null;
        if (skuAfter.getPriceMsrp() != null) {
            priceMsrpAfter = new BigDecimal(skuAfter.getPriceMsrp());
        }
        BigDecimal priceRetailAfter = null;
        if (skuAfter.getPriceRetail() != null) {
            priceRetailAfter = new BigDecimal(skuAfter.getPriceRetail());
        }
//        BigDecimal priceSaleAfter = null;
//        if (skuAfter.getPriceSale() != null) {
//            priceSaleAfter = new BigDecimal(skuAfter.getPriceSale());
//        }

        // true:变更前=变更后（没有变更）；false：变更前<>变更后（变更了）
        boolean clientPriceMsrpFlg = true;
        boolean clientPriceRetailFlg = true;
        boolean clientPriceNetFlg = true;
        boolean priceMsrpFlg = true;
        boolean priceRetailFlg = true;
//        boolean priceSaleFlg = true;

        // 变更后和变更前都存在的情况下进行比较，
        // 变更后值存在,变更前值不存在的情况下，认为变更
        if (clientPriceMsrpBefore != null && clientPriceMsrpAfter != null) {
            if (clientPriceMsrpBefore.compareTo(clientPriceMsrpAfter) != 0) {
                clientPriceMsrpFlg = false;
            }
        } else if (clientPriceMsrpAfter != null) {
            clientPriceMsrpFlg = false;
        }

        if (clientPriceRetailBefore != null && clientPriceRetailAfter != null) {
            if (clientPriceRetailBefore.compareTo(clientPriceRetailAfter) != 0) {
                clientPriceRetailFlg = false;
            }
        } else if (clientPriceRetailAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            clientPriceRetailFlg = false;
        }

        if (clientPriceNetBefore != null && clientPriceNetAfter != null) {
            if (clientPriceNetBefore.compareTo(clientPriceNetAfter) != 0) {
                clientPriceNetFlg = false;
            }
        } else if (clientPriceNetAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            clientPriceNetFlg = false;
        }

        if (priceMsrpBefore != null && priceMsrpAfter != null) {
            if (priceMsrpBefore.compareTo(priceMsrpAfter) != 0) {
                priceMsrpFlg = false;
            }
        } else if (priceMsrpAfter != null) {
            priceMsrpFlg = false;
        }

        if (priceRetailBefore != null && priceRetailAfter != null) {
            if (priceRetailBefore.compareTo(priceRetailAfter) != 0) {
                priceRetailFlg = false;
            }
        } else if (priceRetailAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            priceRetailFlg = false;
        }

//        if (priceSaleBefore != null && priceSaleAfter != null) {
//            if (priceSaleBefore.compareTo(priceSaleAfter) != 0) {
//                priceSaleFlg = false;
//            }
//        } else if (priceSaleAfter != null) {
//            // 变更后值存在,变更前值不存在的情况下，认为变更
//            priceSaleFlg = false;
//        }

        return !(clientPriceMsrpFlg && clientPriceRetailFlg && clientPriceNetFlg && priceMsrpFlg && priceRetailFlg);
    }

    /**
     * 更新field价格
     */
    private Map<String, Object> getNewPriceWithFields(
            CmsBtProductModel_Field field,
            List<Double> msrpPriceList,
            List<Double> retailPriceList,
            List<Double> salePriceList) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> msrpMap = getNewPriceScope(msrpPriceList, field.getPriceMsrpSt(), field.getPriceMsrpEd());
        Map<String, Object> retailMap = getNewPriceScope(retailPriceList, field.getPriceRetailSt(), field.getPriceRetailEd());
//        Map<String, Object> saleMap = getNewPriceScope(salePriceList, field.getPriceSaleSt(), field.getPriceSaleEd());
        if ((boolean) msrpMap.get("isChanged") || (boolean) retailMap.get("isChanged")) {
            result.put("priceMsrpSt", msrpMap.get("start"));
            result.put("priceMsrpEd", msrpMap.get("end"));
            result.put("priceRetailSt", retailMap.get("start"));
            result.put("priceRetailEd", retailMap.get("end"));
//            result.put("priceSaleSt", saleMap.get("start"));
//            result.put("priceSaleEd", saleMap.get("end"));
            result.put("isChanged", true);
        } else {
            result.put("isChanged", false);
        }
        result.put("field", field);

        return result;
    }

    /**
     * 获取价格范围
     */
    private Map<String, Object> getNewPriceScope(List<Double> priceList, Double priceStart, Double priceEnd) {
        Map<String, Object> result = new HashMap<>();
        Double start = priceStart;
        Double end = priceEnd;
        Boolean isChanged = false;
        if (!priceList.isEmpty()) {
            priceList.sort((o1, o2) -> o2.compareTo(o1));
            start = priceList.get(priceList.size() - 1);
            end = priceList.get(0);
            if (priceStart == 0 || priceEnd == 0
                || start.compareTo(priceStart) != 0
                || end.compareTo(priceEnd) != 0) {
                isChanged = true;
            }
        }
        result.put("start", start);
        result.put("end", end);
        result.put("isChanged", isChanged);
        return result;
    }

    /**
     * 取得Sku区间
     */
    private Map<String, Double> getPlatformPriceScope(List<BaseMongoMap<String, Object>> platformSkus) {

        Double priceSaleSt = null;
        Double priceSaleEd = null;
        Double priceRetailSt = null;
        Double priceRetailEd = null;
        Double priceMsrpSt = null;
        Double priceMsrpEd = null;

        Map<String, Double> result = new HashMap<>();
        for (BaseMongoMap<String, Object> platformSku : platformSkus) {
            if (priceSaleSt == null) {
                priceSaleSt = Double.parseDouble(String.valueOf(platformSku.get("priceSale")));
            } else if (priceSaleSt > Double.parseDouble(String.valueOf(platformSku.get("priceSale")))) {
                priceSaleSt = Double.parseDouble(String.valueOf(platformSku.get("priceSale")));
            }
            if (priceSaleEd == null) {
                priceSaleEd = Double.parseDouble(String.valueOf(platformSku.get("priceSale")));
            } else if (priceSaleEd < Double.parseDouble(String.valueOf(platformSku.get("priceSale")))) {
                priceSaleEd = Double.parseDouble(String.valueOf(platformSku.get("priceSale")));
            }
            if (priceRetailSt == null) {
                priceRetailSt = Double.parseDouble(String.valueOf(platformSku.get("priceRetail")));
            } else if (priceRetailSt > Double.parseDouble(String.valueOf(platformSku.get("priceRetail")))) {
                priceRetailSt = Double.parseDouble(String.valueOf(platformSku.get("priceRetail")));
            }
            if (priceRetailEd == null) {
                priceRetailEd = Double.parseDouble(String.valueOf(platformSku.get("priceRetail")));
            } else if (priceRetailEd < Double.parseDouble(String.valueOf(platformSku.get("priceRetail")))) {
                priceRetailEd = Double.parseDouble(String.valueOf(platformSku.get("priceRetail")));
            }

            if (priceMsrpSt == null) {
                priceMsrpSt = Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")));
            } else if (priceMsrpSt > Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")))) {
                priceMsrpSt = Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")));
            }
            if (priceMsrpEd == null) {
                priceMsrpEd = Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")));
            } else if (priceMsrpEd < Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")))) {
                priceMsrpEd = Double.parseDouble(String.valueOf(platformSku.get("priceMsrp")));
            }
        }

        result.put("pPriceSaleSt", priceSaleSt);
        result.put("pPriceSaleEd", priceSaleEd);
        result.put("pPriceRetailSt", priceRetailSt);
        result.put("pPriceRetailEd", priceRetailEd);
        result.put("pPriceMsrpSt", priceMsrpSt);
        result.put("pPriceMsrpEd", priceMsrpEd);

        return result;
    }

    /**
     * 取得Sku区间
     */
    private Map<String, Double> getPriceScope(List<CmsBtProductModel_Sku> commonSkus) {

        Double priceMsrpSt = null;
        Double priceMsrpEd = null;
        Double priceRetailSt = null;
        Double priceRetailEd = null;

        Map<String, Double> result = new HashMap<>();
        for (CmsBtProductModel_Sku commonSku : commonSkus) {
            if (priceMsrpSt == null) {
                priceMsrpSt = commonSku.getPriceMsrp();
            } else if (priceMsrpSt > commonSku.getPriceMsrp()) {
                priceMsrpSt = commonSku.getPriceMsrp();
            }
            if (priceMsrpEd == null) {
                priceMsrpEd = commonSku.getPriceMsrp();
            } else if (priceMsrpEd < commonSku.getPriceMsrp()) {
                priceMsrpEd = commonSku.getPriceMsrp();
            }
            if (priceRetailSt == null) {
                priceRetailSt = commonSku.getPriceRetail();
            } else if (priceRetailSt > commonSku.getPriceRetail()) {
                priceRetailSt = commonSku.getPriceRetail();
            }
            if (priceRetailEd == null) {
                priceRetailEd = commonSku.getPriceRetail();
            } else if (priceRetailEd < commonSku.getPriceRetail()) {
                priceRetailEd = commonSku.getPriceRetail();
            }
        }

        result.put("priceMsrpSt", priceMsrpSt);
        result.put("priceMsrpEd", priceMsrpEd);
        result.put("priceRetailSt", priceRetailSt);
        result.put("priceRetailEd", priceRetailEd);

        return result;
    }
}
