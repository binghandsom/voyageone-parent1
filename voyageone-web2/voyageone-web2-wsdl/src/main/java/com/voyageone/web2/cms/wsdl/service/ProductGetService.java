package com.voyageone.web2.cms.wsdl.service;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.cms.wsdl.BaseService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.response.ProductGetResponse;
import com.voyageone.web2.sdk.api.response.ProductsGetResponse;
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
public class ProductGetService extends BaseService {

    @Autowired
    private CmsBtProductDao cmsBtProductDao;


    public ProductGetResponse selectOne(ProductGetRequest request) {
        ProductGetResponse result = new ProductGetResponse();

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


    public ProductsGetResponse selectList(ProductsGetRequest request) {
        ProductsGetResponse result = new ProductsGetResponse();

        List<CmsBtProductModel> products = null;
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
                    propListMap.put(arrayTypePath, new ArrayList<String>());
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

}
