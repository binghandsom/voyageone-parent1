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


    public ProductGroupGetResponse selectOne(ProductGroupGetRequest request) {
        ProductGroupGetResponse result = new ProductGroupGetResponse();

        CmsBtProductModel product = null;
        if (request == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //ChannelId
        String channelId = request.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }


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


    public ProductGroupsGetResponse selectList(ProductGroupsGetRequest request) {
        ProductGroupsGetResponse result = new ProductGroupsGetResponse();

        List<CmsBtProductModel> products = null;
        List<CmsBtProductModel_Group_Platform> productGroups = null;
        long totalCount = 0L;

        if (request == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //ChannelId
        String channelId = request.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }


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


//    /**
//     * cms Product ArrayType Paths
//     */
//    private static String[] cmsProductArrayTypePaths = {
//            "fields.images1.",
//            "fields.images2.",
//            "fields.images3.",
//            "fields.images4.",
//            "groups.platforms.",
//            "skus."};
//
//    /**
//     * build product query
//     * @param props prop string
//     * @return query string
//     */
//    private String buildProductQuery(String props) {
//        StringBuilder resultSb = new StringBuilder();
//        String propsTmp = props.replaceAll("[\\s]*;[\\s]*", " ; ");
//        String[] propsTmpArr = propsTmp.split(" ; ");
//        Map<String, List<String>> propListMap = new TreeMap<>();
//        int index = 0;
//        for (String propTmp : propsTmpArr) {
//            propTmp = propTmp.trim();
//            String arrayTypePath = getProductArrayTypePath(props);
//            if (arrayTypePath != null) {
//                if (!propListMap.containsKey(arrayTypePath)) {
//                    propListMap.put(arrayTypePath, new ArrayList<String>());
//                }
//                List<String> arrayTypePathList = propListMap.get(arrayTypePath);
//                arrayTypePathList.add(propTmp);
//            } else {
//                if (index > 0) {
//                    resultSb.append(" , ");
//                }
//                resultSb.append(propTmp);
//            }
//            index++;
//        }
//
//        for (Map.Entry<String, List<String>> entry : propListMap.entrySet()) {
//            List<String> propList = entry.getValue();
//            if (propList.size() > 1) {
//                Collections.sort(propList, Collator.getInstance());
//                if (resultSb.length() > 0) {
//                    resultSb.append(" , ");
//                }
//                String key = entry.getKey();
//                String parentPath = key.substring(0, key.length() - 1);
//                String paretnKey = String.format("%s : {$elemMatch : {", parentPath);
//                resultSb.append(paretnKey);
//
//                index = 0;
//                for (String propTmp : propList) {
//                    if (index>0) {
//                        resultSb.append(" , ");
//                    }
//                    resultSb.append(propTmp.replace(key, ""));
//                    index++;
//                }
//                resultSb.append(" } } ");
//
//                //"fields":{$elemMatch: {"code": "100001", "isMain":1}
//            } else if (propList.size()>0) {
//                if (resultSb.length()>0) {
//                    resultSb.append(" , ");
//                }
//                resultSb.append(propList.get(0));
//            }
//        }
//
//        return "{ " + resultSb.toString() + " }";
//    }
//
//    /**
//     * get Product ArrayType Path String
//     * @param propStr porp string
//     * @return Product ArrayType Path
//     */
//    private String getProductArrayTypePath(String propStr) {
//        for (String arrayTypePath : cmsProductArrayTypePaths) {
//            if (propStr.startsWith(arrayTypePath) || propStr.startsWith("\"" + arrayTypePath)) {
//                return arrayTypePath;
//            }
//        }
//        return null;
//    }

}
