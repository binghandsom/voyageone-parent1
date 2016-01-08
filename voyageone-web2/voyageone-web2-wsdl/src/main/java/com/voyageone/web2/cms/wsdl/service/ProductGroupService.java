package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsDeleteRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsDeleteResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
        String props = request.getProps();

        if (groupId != null) {
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"groupId\":%s, \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, groupId));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (cartId != null && !StringUtils.isEmpty(numIId)) {
            String queryTmp = "{\"groups.platforms\":{$elemMatch: {\"cartId\":%s, \"numIId\":\"%s\", \"isMain\":1}}}";
            queryObject.setQuery(String.format(queryTmp, cartId, numIId));
            product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        } else if (!StringUtils.isEmpty(props)) {
            //queryObject.setQuery(buildProductQuery(props));
            //product = cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        if (product != null && product.getGroups() != null && product.getGroups().getPlatforms() != null && product.getGroups().getPlatforms().size() > 0) {
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

        List<CmsBtProductModel> products = null;
        List<CmsBtProductModel_Group_Platform> productGroups = null;
        long totalCount = 0L;

        checkCommRequest(request);
        //ChannelId
        String channelId = request.getChannelId();
        checkRequestChannelId(channelId);


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
        String props = request.getProps();


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
        } else if (!StringUtils.isEmpty(props)) {
            //queryObject.setQuery(buildProductQuery(props));
            isExecute = false;
        }

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
                                HashMap<String, Object> skuQueryMap = new HashMap<>();
                                skuQueryMap.put("prodId", product.getProdId());

                                HashMap<String, Object> updateMap = new HashMap<>();
                                updateMap.put("groups.platforms", platform);

                                BulkUpdateModel skuUpdateModel = new BulkUpdateModel();
                                skuUpdateModel.setUpdateMap(updateMap);
                                skuUpdateModel.setQueryMap(skuQueryMap);
                                bulkList.add(skuUpdateModel);
                            }
                        }
                    }
                }
            }
        }
    }

}
