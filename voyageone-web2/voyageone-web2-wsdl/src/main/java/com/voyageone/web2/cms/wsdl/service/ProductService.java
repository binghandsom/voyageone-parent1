package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.CmsConstants;
import com.voyageone.cms.service.CmsProductLogService;
import com.voyageone.cms.service.dao.CmsBtSxWorkloadDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtFeedInfoDao;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.*;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtPriceLogDao;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtPriceLogModel;
import com.voyageone.web2.sdk.api.domain.ProductPriceModel;
import com.voyageone.web2.sdk.api.domain.ProductSkuPriceModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.Collator;
import java.util.*;

/**
 * product Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class ProductService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    protected CmsProductLogService cmsProductLogService;

    @Autowired
    private CmsBtPriceLogDao cmsBtPriceLogDao;

    @Autowired
    private ProductSkuService productSkuService;

    @Autowired
    private CmsBtSxWorkloadDao cmsBtSxWorkloadDao;

    @Autowired
    private CmsBtFeedInfoDao cmsBtFeedInfoDao;

    /**
     * selectOne
     * @param request ProductGetRequest
     * @return ProductGetResponse
     */
    public ProductGetResponse selectOne(ProductGetRequest request) {
        ProductGetResponse result = new ProductGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(request, queryObject);
        //sorts
        buildSort(request, queryObject);

        //getProductById
        Long pid = request.getProductId();
        //getProductByCode
        String productCode = request.getProductCode();
        //getQueryString
        String queryString = request.getQueryString();
        //getProductByCondition
        String props = request.getProps();

        if (pid != null) {
            queryObject.setQuery(String.format("{\"prodId\" : %s}", pid));
        } else if (!StringUtils.isEmpty(productCode)) {
            queryObject.setQuery(String.format("{\"fields.code\" : \"%s\" }", productCode));
        } else if (!StringUtils.isEmpty(queryString)) {
            queryObject.setQuery(queryString);
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
        }

        if (queryObject.getQuery() != null) {
            CmsBtProductModel product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            result.setProduct(product);
        }

        return result;
    }

    /**
     * selectList
     * @param request ProductsGetRequest
     * @return ProductsGetResponse
     */
    public ProductsGetResponse selectList(ProductsGetRequest request) {
        ProductsGetResponse result = new ProductsGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(request, queryObject);
        //sorts
        buildSort(request, queryObject);
        //limit
        buildLimit(request, queryObject);

        //getProductById
        Set<Long> pids = request.getProductIds();
        //getProductByCode
        Set<String> productCodes = request.getProductCodes();
        //getQueryString
        String queryString = request.getQueryString();
        //getProductByCondition
        String props = request.getProps();

        boolean isExecute = false;
        if (pids != null && pids.size() > 0) {
            String pidsArrStr = Joiner.on(", ").skipNulls().join(pids);
            queryObject.setQuery(String.format("{ \"prodId\" : { $in : [ %s ] } }", pidsArrStr));
            isExecute = true;
        } else if (productCodes != null && productCodes.size() > 0) {
            String productCodesStr = "\"" + Joiner.on("\", \"").skipNulls().join(productCodes) + "\"";
            queryObject.setQuery(String.format("{ \"fields.code\" : { $in : [ %s ] } }", productCodesStr));
            isExecute = true;
        } else if (!StringUtils.isEmpty(queryString)) {
            queryObject.setQuery(queryString);
            isExecute = true;
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            isExecute = true;
        }

        List<CmsBtProductModel> products = null;
        long totalCount = 0L;
        if (isExecute) {
            products = cmsBtProductDao.select(queryObject, channelId);
            if (request.getPageNo() == 1 && products != null && products.size() < request.getPageSize()) {
                totalCount = products.size();
            } else {
                totalCount = cmsBtProductDao.countByQuery(queryObject.getQuery(), channelId);
            }
        }

        result.setProducts(products);
        result.setTotalCount(totalCount);

        return result;
    }

    /**
     * selectCount
     * @param request Request
     * @return ProductsCountResponse
     */
    public ProductsCountResponse selectCount(ProductsCountRequest request) {
        ProductsCountResponse result = new ProductsCountResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        JomgoQuery queryObject = new JomgoQuery();

        //getProductById
        Set<Long> pids = request.getProductIds();
        //getProductByCode
        Set<String> productCodes = request.getProductCodes();
        //getQueryString
        String queryString = request.getQueryString();
        //getProductByCondition
        String props = request.getProps();

        boolean isExecute = false;
        if (pids != null && pids.size() > 0) {
            String pidsArrStr = Joiner.on(", ").skipNulls().join(pids);
            queryObject.setQuery(String.format("{ \"prodId\" : { $in : [ %s ] } }", pidsArrStr));
            isExecute = true;
        } else if (productCodes != null && productCodes.size() > 0) {
            String productCodesStr = "\"" + Joiner.on("\", \"").skipNulls().join(productCodes) + "\"";
            queryObject.setQuery(String.format("{ \"fields.code\" : { $in : [ %s ] } }", productCodesStr));
            isExecute = true;
        } else if (!StringUtils.isEmpty(queryString)) {
            queryObject.setQuery(queryString);
            isExecute = true;
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            isExecute = true;
        }

        long totalCount = 0L;
        if (isExecute) {
            totalCount = cmsBtProductDao.countByQuery(queryObject.getQuery(), channelId);
        }

        result.setTotalCount(totalCount);

        return result;
    }


    /**
     * cms Product ArrayType Paths
     */
    private static String[] cmsProductArrayTypePaths = {
            "fields.images1.",
            "fields.images2.",
            "fields.images3.",
            "fields.images4.",
            "groups.platforms.",
            "skus."};

    /**
     * build product query
     * @param props prop string
     * @return query string
     */
    private String buildProductQuery(String props) {
        StringBuilder resultSb = new StringBuilder();
        String propsTmp = props.replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] propsTmpArr = propsTmp.split(" ; ");
        Map<String, List<String>> propListMap = new TreeMap<>();
        int index = 0;
        for (String propTmp : propsTmpArr) {
            propTmp = propTmp.trim();
            String arrayTypePath = getProductArrayTypePath(props);
            if (arrayTypePath != null) {
                if (!propListMap.containsKey(arrayTypePath)) {
                    propListMap.put(arrayTypePath, new ArrayList<>());
                }
                List<String> arrayTypePathList = propListMap.get(arrayTypePath);
                arrayTypePathList.add(propTmp);
            } else {
                if (index > 0) {
                    resultSb.append(" , ");
                }
                resultSb.append(propTmp);
            }
            index++;
        }

        for (Map.Entry<String, List<String>> entry : propListMap.entrySet()) {
            List<String> propList = entry.getValue();
            if (propList.size() > 1) {
                Collections.sort(propList, Collator.getInstance());
                if (resultSb.length() > 0) {
                    resultSb.append(" , ");
                }
                String key = entry.getKey();
                String parentPath = key.substring(0, key.length() - 1);
                String paretnKey = String.format("%s : {$elemMatch : {", parentPath);
                resultSb.append(paretnKey);

                index = 0;
                for (String propTmp : propList) {
                    if (index>0) {
                        resultSb.append(" , ");
                    }
                    resultSb.append(propTmp.replace(key, ""));
                    index++;
                }
                resultSb.append(" } } ");

                //"fields":{$elemMatch: {"code": "100001", "isMain":1}
            } else if (propList.size()>0) {
                if (resultSb.length()>0) {
                    resultSb.append(" , ");
                }
                resultSb.append(propList.get(0));
            }
        }

        return "{ " + resultSb.toString() + " }";
    }

    /**
     * get Product ArrayType Path String
     * @param propStr porp string
     * @return Product ArrayType Path
     */
    private String getProductArrayTypePath(String propStr) {
        for (String arrayTypePath : cmsProductArrayTypePaths) {
            if (propStr.startsWith(arrayTypePath) || propStr.startsWith("\"" + arrayTypePath)) {
                return arrayTypePath;
            }
        }
        return null;
    }

    /**
     * get prices log list
     * @param request ProductPriceLogGetRequest
     * @return ProductPriceLogGetResponse
     */
    public ProductPriceLogGetResponse getPriceLog(ProductPriceLogGetRequest request) {
        ProductPriceLogGetResponse result = new ProductPriceLogGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        String productCode = request.getProductCode();
        String productSkuCode = request.getProductSkuCode();
        int offset = request.getOffset();
        int rows = request.getRows();

        Map<String, Object> params = new HashMap<>();
        params.put("channelId", channelId);
        params.put("offset", offset);
        params.put("rows", rows);

        boolean isExecute = false;
        List<CmsBtPriceLogModel> priceList;
        int count;

        if (productCode != null) {
            params.put("code", productCode);
            params.put("sku", "0");
            isExecute = true;
        } else if (productSkuCode != null) {
            params.put("sku", productSkuCode);
            isExecute = true;
        }

        if (isExecute) {
            priceList = cmsBtPriceLogDao.selectPriceLogByCode(params);
            count = cmsBtPriceLogDao.selectPriceLogByCodeCnt(params);
            result.setPriceList(priceList);
            result.setTotalCount((long)count);
        }

        return result;
    }

    /**
     * add products
     *
     * @return ProductsAddResponse
     */
    public ProductsAddResponse addProducts(ProductsAddRequest request) {
        ProductsAddResponse response = new ProductsAddResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;

        List<CmsBtProductModel> products = request.getProducts();

        List<Long> pids = new ArrayList<>();
        List<String> productCodes = new ArrayList<>();
        for (CmsBtProductModel product : products) {
            pids.add(product.getProdId());
            productCodes.add(product.getFields().getCode());
            product.setChannelId(channelId);
            product.setCreater(request.getModifier());
            product.setCreated(DateTimeUtil.getNow());
            product.setModifier(request.getModifier());
            product.setModified(DateTimeUtil.getNowTimeStamp());
        }

        /**
         * check row exist
         */
        String pidsArrStr = Joiner.on(", ").skipNulls().join(pids);
        String query = String.format("{ \"prodId\" : { $in : [ %s ] } }", pidsArrStr);
        long count = cmsBtProductDao.countByQuery(query, channelId);
        if (count > 0) {
            throw new ApiException(codeEnum.getErrorCode(), "prodId has existed, not add!");
        }
        String productCodesStr = "\"" + Joiner.on("\", \"").skipNulls().join(productCodes) + "\"";
        query = String.format("{ \"fields.code\" : { $in : [ %s ] } }", productCodesStr);
        count = cmsBtProductDao.countByQuery(query, channelId);
        if (count > 0) {
            throw new ApiException(codeEnum.getErrorCode(), "fields.code has existed, not add!");
        }

        /**
         * execute insert
         */
        cmsBtProductDao.insertWithList(products);
        response.setInsertedCount(products.size());

        return response;
    }

    /**
     * update product
     *
     * @return ProductUpdateResponse
     */
    public ProductUpdateResponse updateProduct(ProductUpdateRequest request) {
        ProductUpdateResponse response = new ProductUpdateResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        CmsBtProductModel productModel = request.getProductModel();
        Long prodId = productModel.getProdId();
        String productCode = null;
        if (productModel.getFields() != null) {
            productCode = productModel.getFields().getCode();
        }

        String queryStr = null;
        HashMap<String, Object> queryMap = new HashMap<>();
        if (prodId != null) {
            queryStr = String.format("{\"prodId\" : %s}", prodId);
            queryMap.put("prodId", prodId);
        } else if (!StringUtils.isEmpty(productCode)) {
            queryStr = String.format("{\"fields.code\" : \"%s\" }", productCode);
            queryMap.put("fields.code", productCode);
        }

        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        if (!StringUtils.isEmpty(queryStr)) {
            JomgoQuery queryObject = new JomgoQuery();
            queryObject.setQuery(queryStr);
            queryObject.setProjection("prodId", "modified", "fields.status");

            CmsBtProductModel findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
            if (findModel == null) {
                throw new ApiException(codeEnum.getErrorCode(), "product not found!");
            }

            if(request.getIsCheckModifed()) {
                if (findModel.getModified() != null && !findModel.getModified().equals(productModel.getModified())) {
                    throw new ApiException(codeEnum.getErrorCode(), "product has been update, not update!");
                }
            }

            List<BulkUpdateModel> bulkList = new ArrayList<>();

            HashMap<String, Object> updateMap = new HashMap<>();

            /**
             * common attribute
             */
            String catId = productModel.getCatId();
            if (catId != null) {
                updateMap.put("catId", catId);
            }
            String catPath = productModel.getCatPath();
            if (catPath != null) {
                updateMap.put("catPath", catPath);
            }

            String modified = DateTimeUtil.getNowTimeStamp();
            if (request.getModified() != null) {
                modified = request.getModified();
            }
            updateMap.put("modified", modified);
            updateMap.put("modifier", request.getModifier());

            /**
             * Fields
             */
            CmsBtProductModel_Field fields = productModel.getFields();
            if (fields != null && fields.size() > 0) {
                BasicDBObject fieldObj = fields.toUpdateBasicDBObject("fields.");
                updateMap.putAll(fieldObj);
            }

            /**
             * sku
             */
            List<CmsBtProductModel_Sku> skus = productModel.getSkus();
            if (skus != null && skus.size() > 0) {

                // 如果sku价格发生变化更新product/model的price
                ProductUpdatePriceRequest productPriceRequest = new ProductUpdatePriceRequest(channelId);
                ProductPriceModel model = new ProductPriceModel();

                model.setProductId(findModel.getProdId());

                ProductSkuPriceModel skuPriceModel;
                // 设置sku的价格.
                for (CmsBtProductModel_Sku sku : skus) {
                    skuPriceModel = new ProductSkuPriceModel();
                    skuPriceModel.setSkuCode(sku.getSkuCode());
                    skuPriceModel.setPriceMsrp(sku.getPriceMsrp());
                    skuPriceModel.setPriceRetail(sku.getPriceRetail());
                    skuPriceModel.setPriceSale(sku.getPriceSale());
                    model.addSkuPrice(skuPriceModel);
                }

                productPriceRequest.setModifier(request.getModifier());
                productPriceRequest.addProductPrices(model);
                productSkuService.updatePrices(productPriceRequest);

                // 更新sku信息
                ProductSkusPutRequest skusPutRequest = new ProductSkusPutRequest(channelId);
                skusPutRequest.setProductId(findModel.getProdId());
                skusPutRequest.setSkus(skus);
                productSkuService.saveSkus(skusPutRequest);
            }

            /**
             * Feed
             */
            CmsBtProductModel_Feed feed = productModel.getFeed();
            if (feed != null) {
                if (feed.getOrgAtts() != null && feed.getOrgAtts().size() > 0) {
                    BasicDBObject orgAttsObj = feed.getOrgAtts().toUpdateBasicDBObject("feed.orgAtts.");
                    updateMap.putAll(orgAttsObj);
                }
                if (feed.getCnAtts() != null && feed.getCnAtts().size() > 0) {
                    BasicDBObject cnAttsObj = feed.getCnAtts().toUpdateBasicDBObject("feed.cnAtts.");
                    updateMap.putAll(cnAttsObj);
                }
                if (feed.getCustomIds() != null && feed.getCustomIds().size() > 0) {
                    updateMap.put("feed.customIds", feed.getCustomIds());
                }
            }

            /**
             * set update model
             */
            if (updateMap.size() > 0) {
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
            }

            /**
             * execute update
             */
            if (bulkList.size() > 0) {

                if (findModel.getFields() != null && findModel.getFields().getStatus() != null
                    && productModel.getFields() != null && productModel.getFields().getStatus() != null) {
                    //insert　ProductHistory
                    CmsConstants.ProductStatus befStatus = CmsConstants.ProductStatus.valueOf(findModel.getFields().getStatus());
                    CmsConstants.ProductStatus aftStatus = CmsConstants.ProductStatus.valueOf(productModel.getFields().getStatus());
                    insertProductHistory(befStatus, aftStatus, channelId, findModel.getProdId());

                    //insert　SxWorkLoad
                    String modifier = "0";
                    if (!StringUtils.isEmpty(request.getModifier())) {
                        modifier = request.getModifier();
                    }
                    insertSxWorkLoad(befStatus, aftStatus, channelId, findModel.getProdId(), modifier);
                }

                /**
                 * 更新产品数据
                 */
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
                setResultCount(response, bulkWriteResult);
            }
        }

        return response;
    }


    /**
     * delete product
     *
     * @return ProductsDeleteResponse
     */
    public ProductsDeleteResponse deleteProducts(ProductsDeleteRequest request) {
        ProductsDeleteResponse response = new ProductsDeleteResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        //getProductById
        Set<Long> pids = request.getProductIds();
        //getProductByCode
        Set<String> productCodes = request.getProductCodes();

        boolean isExecute = false;
        List<Map<String, Object>> bulkList = new ArrayList<>();
        if (pids != null && pids.size() > 0) {
            for (Long pid : pids) {
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("prodId", pid);
                bulkList.add(queryMap);
            }
            isExecute = true;
        } else if (productCodes != null && productCodes.size() > 0) {
            for (String productCode : productCodes) {
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("fields.code", productCode);
                bulkList.add(queryMap);
            }
            isExecute = true;
        }

        if (isExecute) {
            if (bulkList.size() > 0) {
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkRemoveWithMap(channelId, bulkList);
                setResultCount(response, bulkWriteResult);
            }
        }

        return response;
    }

    /**
     * updateStatusProduct
     * @param request ProductStatusPutRequest
     * @return ProductGroupsPutResponse
     */
    public ProductGroupsPutResponse updateStatusProduct(@RequestBody ProductStatusPutRequest  request) {
        ProductGroupsPutResponse response = new ProductGroupsPutResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        CmsConstants.ProductStatus aftStatus = request.getStatus();

        HashMap<String, Object> queryMap = new HashMap<>();
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("prodId", "fields.status");
        if (request.getProductId() != null) {
            queryObject.setQuery("{\"prodId\":" + request.getProductId() + "}");
        } else {
            queryObject.setQuery("{\"fields.code\":\"" + request.getProductCode() + "\"}");
        }

        CmsBtProductModel findModel = null;
        if (!StringUtils.isEmpty(queryObject.getQuery())) {
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        HashMap<String, Object> updateMap = new HashMap<>();
        if (findModel != null) {
            String findStatus = null;
            if (findModel.getFields() != null && findModel.getFields().getStatus() != null) {
                findStatus = findModel.getFields().getStatus();
            }
            if (aftStatus != null && !aftStatus.toString().equals(findStatus)) {
                updateMap.put("fields.status", aftStatus.toString());

                if (!StringUtils.isEmpty(request.getModifier())) {
                    updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
                    updateMap.put("modifier", request.getModifier());
                }
            }
        }

        if (updateMap.size() > 0) {
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);

            List<BulkUpdateModel> bulkList = new ArrayList<>();
            bulkList.add(model);

            if (findModel !=null && findModel.getFields() != null) {
                //insert　Product　History
                CmsConstants.ProductStatus befStatus = CmsConstants.ProductStatus.valueOf(findModel.getFields().getStatus());
                insertProductHistory(befStatus, aftStatus, channelId, findModel.getProdId());

                //insert　SxWorkLoad
                String modifier = "0";
                if (!StringUtils.isEmpty(request.getModifier())) {
                    modifier = request.getModifier();
                }
                insertSxWorkLoad(befStatus, aftStatus, channelId, findModel.getProdId(), modifier);
            }

            // 更新产品状态
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$set");
            setResultCount(response, bulkWriteResult);
        }

        return response;
    }


    private void insertProductHistory(CmsConstants.ProductStatus befStatus,
                                      CmsConstants.ProductStatus aftStatus,
                                      String channelId, Long productId) {
        if (befStatus != null && aftStatus != null && !befStatus.equals(aftStatus)) {
            if (productId != null) {
                CmsBtProductModel productModel = cmsBtProductDao.selectProductById(channelId, productId);
                cmsProductLogService.insertProductHistory(productModel);
            }
        }
    }

    private void insertSxWorkLoad(CmsConstants.ProductStatus befStatus,
                                  CmsConstants.ProductStatus aftStatus,
                                  String channelId, Long groupId, String modifier) {
        if (befStatus != null && aftStatus != null) {
            boolean isNeed = false;
            // 从其他状态转为Pending
            if (befStatus != CmsConstants.ProductStatus.Pending && aftStatus == CmsConstants.ProductStatus.Pending) {
                isNeed = true;
            // 从Pending转为其他状态
            // 在Pending下变更了
            } else if (befStatus == CmsConstants.ProductStatus.Pending) {
                isNeed = true;
            }

            if (isNeed) {
                CmsBtSxWorkloadModel model = new CmsBtSxWorkloadModel();
                model.setChannelId(channelId);
                model.setGroupId(groupId.intValue());
                model.setPublishStatus(0);
                model.setCreater(modifier);
                model.setModifier(modifier);
                cmsBtSxWorkloadDao.insertSxWorkloadModel(model);
            }
        }
    }

    /**
     * confirm change category
     * @param request
     * @return
     */
    public VoApiUpdateResponse changeProductCategory(ProductCategoryUpdateRequest request){

        request.check();

        String modelCode = cmsBtProductDao.getModelCode(request.getChannelId(),request.getProductId());

        //update product's category id and category path
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("catId", request.getCategoryId());
        updateMap.put("catPath", request.getCategoryPath());
        updateMap.put("batchField.switchCategory",1);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("feed.orgAtts.modelCode", modelCode);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(request.getChannelId(), bulkList, request.getModifier(), "$set");

        int updateFeedInfoCount = cmsBtFeedInfoDao.updateFeedInfoUpdFlg(request.getChannelId(),modelCode);


        ProductCategoryUpdateResponse response = new ProductCategoryUpdateResponse();

        response.setUpdFeedInfoCount(updateFeedInfoCount);

        response.setUpdProductCount(result.getModifiedCount());

        response.setModifiedCount(result.getModifiedCount()+updateFeedInfoCount);

        return response;
    }

}
