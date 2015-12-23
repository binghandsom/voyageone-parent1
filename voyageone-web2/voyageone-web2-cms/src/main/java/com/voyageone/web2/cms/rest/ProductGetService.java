package com.voyageone.web2.cms.rest;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductsGetRequest;
import com.voyageone.web2.sdk.api.request.ProductGetRequest;
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
public class ProductGetService extends BaseRestService{

    @Autowired
    private CmsBtProductDao cmsBtProductDao;


    public CmsBtProductModel selectOne(ProductGetRequest params) {
        if (params == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }

        //ChannelId
        String channelId = params.getChannelId();
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }


        JomgoQuery queryObject = new JomgoQuery();
        //fields
        buildProjection(params, queryObject);
        //sorts
        buildSort(params, queryObject);

        //getProductById
        Long pid = params.getProductId();
        if (pid != null) {
            queryObject.setQuery(String.format("{\"prodId\" : %s}", pid));
            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        //getProductByCode
        String productCode = params.getProductCode();
        if (!StringUtils.isEmpty(productCode)) {
            queryObject.setQuery(String.format("{\"fields.code\" : \"%s\" }", productCode));
            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        //getProductByCondition
        String props = params.getProps();
        if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        return null;
    }


    public List<CmsBtProductModel> selectList(ProductsGetRequest request) {
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
        Set<Long> pids = request.getProductIds();
        if (pids != null && pids.size() > 0) {
//            queryObject.setQuery(String.format("{\"prodId\" : %s}", pid));
//            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        //getProductByCode
        Set<String> productCodes = request.getProductCodes();
        if (productCodes != null && productCodes.size() > 0) {
//            queryObject.setQuery(String.format("{\"fields.code\" : \"%s\" }", productCode));
//            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        //getProductByCondition
        String props = request.getProps();
        if (!StringUtils.isEmpty(props)) {
            queryObject.setQuery(buildProductQuery(props));
            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        return null;
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
