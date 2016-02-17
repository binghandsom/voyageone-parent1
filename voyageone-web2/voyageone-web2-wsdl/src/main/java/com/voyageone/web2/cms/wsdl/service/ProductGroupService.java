package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteResult;
import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsDeleteRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsPutRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsPutResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 *  Product Group Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class ProductGroupService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    /**
     * selectOne
     * @param request ProductGroupGetRequest
     * @return ProductGroupGetResponse
     */
    public ProductGroupGetResponse selectOne(ProductGroupGetRequest request) {
        ProductGroupGetResponse result = new ProductGroupGetResponse();

        CmsBtProductModel product = null;
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(request, queryObject);
        if (StringUtils.isEmpty(queryObject.getProjection())) {
            queryObject.setProjection("groups.platforms.$");
        }
        //sorts
        buildSort(request, queryObject);

        //get GroupId
        Long groupId = request.getGroupId();
        //get CartId
        Integer cartId = request.getCartId();
        //get NumIId
        String numIId = request.getNumIId();
        //getProductByCondition
//        String props = request.getProps();

        if (groupId != null) {
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"groupId\":%s, \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, groupId));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (cartId != null && !StringUtils.isEmpty(numIId)) {
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"cartId\":%s, \"numIId\":\"%s\", \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, cartId, numIId));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (product != null && product.getGroups() != null
                && product.getGroups().getPlatforms() != null
                && product.getGroups().getPlatforms().size() > 0) {
            result.setProductGroupPlatform(product.getGroups().getPlatforms().get(0));
        }
        return result;
    }

    /**
     * selectList
     * @param request ProductGroupsGetRequest
     * @return ProductGroupsGetResponse
     */
    public ProductGroupsGetResponse selectList(ProductGroupsGetRequest request) {
        ProductGroupsGetResponse result = new ProductGroupsGetResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(request, queryObject);
        if (StringUtils.isEmpty(queryObject.getProjection())) {
            queryObject.setProjection("groups.platforms.$");
        }
        //sorts
        buildSort(request, queryObject);
        //limit
        buildLimit(request, queryObject);

        //get GroupId
        Set<Long> groupIds = request.getGroupIds();
        //get CartId
        Integer cartId = request.getCartId();
        //get NumIId
        Set<String> numIIds = request.getNumIIds();
        //getProductByCondition
//        String props = request.getProps();

        boolean isExecute = false;
        if (groupIds != null && groupIds.size() > 0) {
            String groupIdsArrStr = Joiner.on(", ").skipNulls().join(groupIds);
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"groupId\":{$in:[%s]}, \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, groupIdsArrStr));
            isExecute = true;
        } else if (cartId != null && numIIds != null && numIIds.size() > 0) {
            String productCodesStr = "\"" + Joiner.on("\", \"").skipNulls().join(numIIds) + "\"";
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"cartId\":%s, \"numIId\":{$in:[%s]}, \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, cartId, productCodesStr));
            isExecute = true;
        }
//        else if (!StringUtils.isEmpty(props)) {
//            //queryObject.setQuery(buildProductQuery(props));
//            isExecute = false;
//        }

        List<CmsBtProductModel> products;
        List<CmsBtProductModel_Group_Platform> productGroups = null;
        long totalCount = 0L;
        if (isExecute) {
            products = cmsBtProductDao.select(queryObject, channelId);
            if (request.getPageNo() == 1 && products != null && products.size() < request.getPageSize()) {
                totalCount = products.size();
            } else {
                totalCount = cmsBtProductDao.countByQuery(queryObject.getQuery(), channelId);
            }
            if (products != null) {
                productGroups = new ArrayList<>();
                for (CmsBtProductModel product : products) {
                    if (product != null && product.getGroups() != null && product.getGroups().getPlatforms() != null && product.getGroups().getPlatforms().size() > 0) {
                        productGroups.add(product.getGroups().getPlatforms().get(0));
                    }
                }
            }
        }

        result.setProductGroupPlatforms(productGroups);
        result.setTotalCount(totalCount);

        return result;
    }

    /**
     * save Groups
     * @param request ProductGroupsPutRequest
     * @return ProductGroupsPutResponse
     */
    public ProductGroupsPutResponse saveGroups(ProductGroupsPutRequest request) {
        ProductGroupsPutResponse result = new ProductGroupsPutResponse();
        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        Set<Long> productIds = request.getProductIds();
        Set<String> productCodes = request.getProductCodes();
        CmsBtProductModel_Group_Platform platform = request.getPlatform();

        List<BulkUpdateModel> bulkInsertList = new ArrayList<>();
        List<BulkUpdateModel> bulkUpdateList = new ArrayList<>();
        if (productIds != null && productIds.size() > 0) {
            for (Long productId : productIds) {
                saveAddGroupBlukUpdate(channelId, productId, null, platform, bulkInsertList, bulkUpdateList);
            }
        } else if (productCodes != null && productCodes.size() > 0) {
            for (String productCode : productCodes) {
                saveAddGroupBlukUpdate(channelId, null, productCode, platform, bulkInsertList, bulkUpdateList);
            }
        }

        if (bulkInsertList.size() > 0) {
            BasicDBObject queryObj = (BasicDBObject)bulkInsertList.get(0).getQueryMap();
            BasicDBList platformListObj = new BasicDBList();
            for (BulkUpdateModel bulkInsert : bulkInsertList) {
                platformListObj.add(bulkInsert.getUpdateMap());
            }
            BasicDBObject platformsObj = new BasicDBObject().append("groups.platforms", platformListObj);
            BasicDBObject pushObj = new BasicDBObject().append("$pushAll", platformsObj);
            WriteResult writeResult = cmsBtProductDao.getDBCollection(channelId).update(queryObj, pushObj);
            result.setInsertedCount(writeResult.getN());
        }

        if (bulkUpdateList.size() > 0) {
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkUpdateList, null, "$set");
            setResultCount(result, bulkWriteResult);
        }

        return result;
    }

    /**
     * saveAddGroupBlukUpdate
     */
    private void saveAddGroupBlukUpdate(String channelId,
                                        Long productId, String productCode,
                                        CmsBtProductModel_Group_Platform platform,
                                        List<BulkUpdateModel> bulkInsertList, List<BulkUpdateModel> bulkUpdateList) {
        VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70007;
        if (platform == null || platform.size() == 0 || platform.getGroupId() == null) {
            return;
        }

        if (productId == null && StringUtils.isEmpty(productCode)) {
            throw new ApiException(codeEnum.getErrorCode(), "productId or productCode not found!");
        }

        CmsBtProductModel findModel = null;
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("groups.platforms.groupId");
        if (productId != null) {
            queryObject.setQuery("{\"prodId\":" + productId + "}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (StringUtils.isEmpty(productCode)) {
            queryObject.setQuery("{\"fields.code\":\"" + productCode + "\"}");
            findModel = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (findModel != null) {
            Set<Long> findGroupIdSet = new HashSet<>();
            if (findModel.getGroups() != null && findModel.getGroups().getPlatforms() != null) {
                for (CmsBtProductModel_Group_Platform platformTemp : findModel.getGroups().getPlatforms()) {
                    if (platformTemp != null && platformTemp.getGroupId() != null) {
                        findGroupIdSet.add(platformTemp.getGroupId());
                    }
                }
            }

            BasicDBObject queryMap = new BasicDBObject();
            if (productId != null) {
                queryMap.append("prodId", productId);
            } else {
                queryMap.append("fields.code", productCode);
            }
            if (findGroupIdSet.contains(platform.getGroupId())) {
                queryMap.put("groups.platforms.groupId", platform.getGroupId());

                BasicDBObject dbObject = platform.toUpdateBasicDBObject("groups.platforms.$.");

                if (dbObject.size() > 0) {
                    BulkUpdateModel groupUpdateModel = new BulkUpdateModel();
                    groupUpdateModel.setUpdateMap(dbObject);
                    groupUpdateModel.setQueryMap(queryMap);

                    bulkUpdateList.add(groupUpdateModel);
                }
            } else {
                BasicDBObject dbObject = platform.toUpdateBasicDBObject("");
                if (dbObject.size() > 0) {
                    BulkUpdateModel groupUpdateModel = new BulkUpdateModel();
                    groupUpdateModel.setUpdateMap(dbObject);
                    groupUpdateModel.setQueryMap(queryMap);
                    bulkInsertList.add(groupUpdateModel);
                }
            }
        }
    }

    /**
     * deleteList
     * @param request ProductGroupsDeleteResponse
     * @return ProductGroupsDeleteResponse
     */
    public ProductGroupsDeleteResponse deleteList(@RequestBody ProductGroupsDeleteRequest request) {
        ProductGroupsDeleteResponse response = new ProductGroupsDeleteResponse();

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);

        request.check();

        //get GroupId
        Set<Long> groupIds = request.getGroupIds();
        //get CartId
        Integer cartId = request.getCartId();
        //get NumIId
        Set<String> numIIds = request.getNumIIds();

        boolean isExecute = false;
        JomgoQuery queryObject = new JomgoQuery();
        queryObject.setProjection("prodId", "groups.platforms");
        if (groupIds != null && groupIds.size() > 0) {
            String groupIdsArrStr = Joiner.on(", ").skipNulls().join(groupIds);
            String queryTmp = "{\"groups.platforms.groupId\":{$in:[%s]}}";
            queryObject.setQuery(String.format(queryTmp, groupIdsArrStr));
            isExecute = true;
        } else if (cartId != null && numIIds != null && numIIds.size() > 0) {
            String productCodesStr = "\"" + Joiner.on("\", \"").skipNulls().join(numIIds) + "\"";
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"cartId\":%s, \"numIId\":{$in:[%s]}}}}";
            queryObject.setQuery(String.format(queryTmp, cartId, productCodesStr));
            isExecute = true;
        }

        List<BulkUpdateModel> bulkList = new ArrayList<>();
        if (isExecute) {
            List<CmsBtProductModel> products = cmsBtProductDao.select(queryObject, channelId);
            addDeleteGroupBulk(products, groupIds, cartId, numIIds, bulkList);

            if (bulkList.size() > 0) {
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, null, "$pull");
                setResultCount(response, bulkWriteResult);
            }
        }

        return response;
    }

    /**
     * addDeleteGroupBulk
     */
    public void addDeleteGroupBulk(List<CmsBtProductModel> products,
                                   Set<Long> groupIds, Integer cartId, Set<String> numIIds,
                                   List<BulkUpdateModel> bulkList) {
        if (products != null) {
            for(CmsBtProductModel product: products) {
                if (product != null && product.getGroups() != null && product.getGroups().getPlatforms() != null) {
                    for (CmsBtProductModel_Group_Platform platform : product.getGroups().getPlatforms()) {
                        if (platform != null) {
                            boolean isExist = false;
                            if (groupIds != null && groupIds.size() > 0 && groupIds.contains(platform.getGroupId())) {
                                isExist = true;
                            } else if (cartId != null && numIIds != null && numIIds.size() > 0
                                    && cartId.equals(platform.getCartId()) && numIIds.contains(platform.getNumIId())) {
                                isExist = true;
                            }
                            if (isExist) {
                                HashMap<String, Object> groupQueryMap = new HashMap<>();
                                groupQueryMap.put("prodId", product.getProdId());

                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("groups.platforms", platform);

                                BulkUpdateModel groupUpdateModel = new BulkUpdateModel();
                                groupUpdateModel.setUpdateMap(updateMap);
                                groupUpdateModel.setQueryMap(groupQueryMap);
                                bulkList.add(groupUpdateModel);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * change main product.
     * @param groupId
     * @param channelId
     * @param productId
     * @return
     */
    public VoApiUpdateResponse updateMainProduct(Long groupId, String channelId, Long productId,String modifier){

        int updateCount = 0;

        updateCount = updateCount +resetMainProduct(groupId, channelId, modifier);

        updateCount = updateCount +setMainProduct(groupId, channelId, productId, modifier);

        VoApiUpdateResponse response = new VoApiUpdateResponse();

        response.setModifiedCount(updateCount);

        return response;
    }

    /**
     * 设置主产品.
     * @param groupId
     * @param channelId
     * @param productId
     * @param modifier
     */
    private int setMainProduct(Long groupId, String channelId, Long productId, String modifier) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("groups.platforms.$.isMain", 1);

        HashMap<String, Object> queryMap = new HashMap<>();

        queryMap.put("prodId", productId);

        queryMap.put("groups.platforms.groupId", groupId);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);

        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        return result.getModifiedCount();
    }

    /**
     * 将主商品设置为非主商品.
     * @param groupId
     * @param channelId
     * @param modifier
     */
    private int resetMainProduct(Long groupId, String channelId, String modifier) {
        List<BulkUpdateModel> bulkList = new ArrayList<>();

        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("groups.platforms.$.isMain", 0);

        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("groups.platforms.isMain", 1);
        queryMap.put("groups.platforms.groupId", groupId);

        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        BulkWriteResult result = cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, modifier, "$set");

        return result.getModifiedCount();
    }

}
