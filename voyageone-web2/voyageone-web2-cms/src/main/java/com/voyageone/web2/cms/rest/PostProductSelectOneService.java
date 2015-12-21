package com.voyageone.web2.cms.rest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.voyageone.cms.service.CmsProductService;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.PostProductSelectOneRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private CmsProductService cmsProductService;


    private static String[] arrayTypePaths = {
            "fields.images1.",
            "fields.images2.",
            "fields.images3.",
            "fields.images4.",
            "groups.platforms.",
            "skus."};

    public CmsBtProductModel selectOne(PostProductSelectOneRequest params) {
        CmsBtProductModel model = null;

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

        //getProductById
        Long pid = params.getProductId();
        if (pid != null) {
            return cmsProductService.getProductById(channelId, pid);
        }

        //getProductByCode
        String productCode = params.getProductCode();
        if (!StringUtils.isEmpty(productCode)) {
            return cmsProductService.getProductByCode(channelId, productCode);
        }

        //getProductByCondition
        String props = params.getProps();
        if (!StringUtils.isEmpty(props)) {
            return cmsProductService.getProductWithQuery(channelId, props);
        }

        return model;
    }

    private String convertProps (String props) {
        StringBuilder resultSb = new StringBuilder();
        String propsTmp = props.replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] propsTmpArr = propsTmp.split(" ; ");
        List<String> propList = new ArrayList<>();
        int index = 0;
        for (String propTmp : propsTmpArr) {
            propTmp = propTmp.trim();
            if (isArrayType(propTmp)) {
                propList.add(propTmp);
            } else {
                if (index > 0) {
                    resultSb.append(" , ");
                }
                resultSb.append(propTmp);
            }
            index++;
        }

        Collections.sort(propList, Collator.getInstance());

        int preArrayTypePathIndex = 0;
        for (String propTmp : propList) {
            int arrayTypePathIndex = getArrayTypePathIndex(propTmp);
            if (arrayTypePathIndex == preArrayTypePathIndex) {

            } else {
                DBObject statusQuery = new BasicDBObject("event", "WonGame");
                statusQuery.put("playerId", "52307b8fe4b0fc612dea2c6f");
                DBObject fields = new BasicDBObject("$elemMatch", statusQuery);
                DBObject query = new BasicDBObject("playerHistories",fields);
                System.out.println(query.toString());
                System.out.println(query.toString());
            }
        }

        return resultSb.toString();
    }

    private boolean isArrayType(String props) {
        for (String arrayTypePath : arrayTypePaths) {
            if (props.startsWith(arrayTypePath)) {
                return true;
            }
        }
        return false;
    }

    private int getArrayTypePathIndex(String props) {
        for (int i=0; i<arrayTypePaths.length; i++) {
            if (props.startsWith(arrayTypePaths[i])) {
                return i+1;
            }
        }
        return 0;
    }
}
