package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Feed;
import com.voyageone.cms.service.model.CmsBtProductModel_Field;
import com.voyageone.cms.service.model.CmsBtProductModel_Sku;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.cms.wsdl.dao.CmsBtPriceLogDao;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.domain.CmsBtPriceLogModel;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.*;
import com.voyageone.web2.sdk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private CmsBtPriceLogDao cmsBtPriceLogDao;

    @Autowired
    private ProductSkuService productSkuService;

    /**
     * selectOne
     * @param request ProductGetRequest
     * @return ProductGetResponse
     */
    public ProductGetResponse selectOne(ProductGetRequest request) {
        ProductGetResponse result = new ProductGetResponse();

        CmsBtProductModel product = null;
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);


        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(request, queryObject);
        //sorts
        buildSort(request, queryObject);

        //getProductById
        Long pid = request.getProductId();
        //getProductByCode
        String productCode = request.getProductCode();
        //getProductByCondition
        String props = request.getProps();

        if (pid != null) {
            queryObject.setQuery(String.format("{\"prodId\" : %s}", pid));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (!StringUtils.isEmpty(productCode)) {
            queryObject.setQuery(String.format("{\"fields.code\" : \"%s\" }", productCode));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        result.setProduct(product);
        return result;
    }

    /**
     * selectList
     * @param request ProductsGetRequest
     * @return ProductsGetResponse
     */
    public ProductsGetResponse selectList(ProductsGetRequest request) {
        ProductsGetResponse result = new ProductsGetResponse();

        List<CmsBtProductModel> products = null;
        long totalCount = 0L;

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);


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
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            isExecute = true;
        }

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

        long totalCount = 0L;

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);


        JomgoQuery queryObject = new JomgoQuery();

        //getProductById
        Set<Long> pids = request.getProductIds();
        //getProductByCode
        Set<String> productCodes = request.getProductCodes();
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
        } else if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            isExecute = true;
        }

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
     * @return ProductUpdateResponse
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
            product.setCreater(request.getCreater());
            product.setCreated(DateTimeUtil.getNow());
            product.setModifier(request.getCreater());
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
            queryObject.setProjection("prodId", "modified");

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
            updateMap.put("modified", DateTimeUtil.getNowTimeStamp());
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

}
