package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.cms.service.model.CmsBtProductModel_Group_Platform;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductGroupGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGroupsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGroupGetResponse;
import com.voyageone.web2.sdk.api.response.ProductGroupsGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 *  Product Group Service
 *
 * @author chuanyu.liang 15/12/9
 * @version 2.0.1
 * @since. 2.0.0
 */
@Service
public class ProductGroupGetService extends BaseService {

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

}
