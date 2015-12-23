package com.voyageone.web2.cms.rest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.dao.mongodb.CmsBtProductDao;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
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
public class PostProductSelectOneService extends BaseAppService{

    @Autowired
    private CmsBtProductDao cmsBtProductDao;


    private static String[] arrayTypePaths = {
            "fields.images1.",
            "fields.images2.",
            "fields.images3.",
            "fields.images4.",
            "groups.platforms.",
            "skus."};

    public CmsBtProductModel selectOne(PostProductSelectOneRequest params) {
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
            queryObject.setQuery(convertProps(props));
            return cmsBtProductDao.selectOneWithQuery(queryObject, channelId);
        }

        return null;
    }

    private String convertProps (String props) {
        StringBuilder resultSb = new StringBuilder();
        String propsTmp = props.replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] propsTmpArr = propsTmp.split(" ; ");
        Map<String, List<String>> propListMap = new TreeMap<>();
        int index = 0;
        for (String propTmp : propsTmpArr) {
            propTmp = propTmp.trim();
            String arrayTypePath = getArrayTypePath(props);
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

    private String getArrayTypePath(String propStr) {
        for (String arrayTypePath : arrayTypePaths) {
            if (propStr.startsWith(arrayTypePath) || propStr.startsWith("\"" + arrayTypePath)) {
                return arrayTypePath;
            }
        }
        return null;
    }

    private void buildProjection(PostProductSelectOneRequest params, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(params.getFields())) {
            return;
        }
        String fieldsTmp = params.getFields().replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] fieldsTmpArr = fieldsTmp.split(" ; ");
        queryObject.setProjection(fieldsTmpArr);
    }

    private void buildSort(PostProductSelectOneRequest params, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(params.getSorts())) {
            return;
        }
        String sortsTmp = params.getSorts().replaceAll("[\\s]*;[\\s]*", ", ");
        queryObject.setSort("{ " + sortsTmp + " }");
    }

}
