package com.voyageone.web2.cms.wsdl;

import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.cms.service.model.CmsBtProductModel;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.base.BaseAppComponent;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiException;
import com.voyageone.web2.sdk.api.request.ProductSkusGetRequest;

import java.util.List;

/**
 * Rest webservice Service 层提供基类
 * Created by chuanyu.liang on 15/6/26.
 * @author chuanyu.liang
 */
public abstract class BaseService extends BaseAppComponent {

    /**
     * Check Request
     * @param request Request
     */
    protected void checkCommRequest(VoApiRequest request) {
        List<CmsBtProductModel> products = null;
        long totalCount = 0L;

        if (request == null) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70001;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }
    }

    /**
     * check Request ChannelId
     * @param channelId channel ID
     */
    protected void checkRequestChannelId(String channelId) {
        if (StringUtils.isEmpty(channelId)) {
            VoApiConstants.VoApiErrorCodeEnum codeEnum = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70003;
            throw new ApiException(codeEnum.getErrorCode(), codeEnum.getErrorMsg());
        }
    }

    /**
     * buildProjection
     * @param request VoApiRequest
     * @param queryObject JomgoQuery
     */
    protected void buildProjection(VoApiRequest request, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(request.getFields())) {
            return;
        }
        String fieldsTmp = request.getFields().replaceAll("[\\s]*;[\\s]*", " ; ");
        String[] fieldsTmpArr = fieldsTmp.split(" ; ");
        queryObject.setProjection(fieldsTmpArr);
    }

    /**
     * buildSort
     * @param request VoApiRequest
     * @param queryObject queryObject
     */
    protected void buildSort(VoApiRequest request, JomgoQuery queryObject) {
        if (StringUtils.isEmpty(request.getSorts())) {
            return;
        }
        String sortsTmp = request.getSorts().replaceAll("[\\s]*;[\\s]*", ", ");
        queryObject.setSort("{ " + sortsTmp + " }");
    }

    protected void buildLimit(VoApiListRequest request, JomgoQuery queryObject) {
        int pageSize = request.getPageSize();
        if (pageSize < 1) {
            pageSize = 1;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        int pageNo = request.getPageNo();
        if (pageNo < 1) {
            pageNo = 1;
        }

        queryObject.setLimit(pageSize);
        queryObject.setSkip((pageNo-1) * pageSize);
        request.getPageSize();
    }


}
