package com.voyageone.web2.cms.openapi;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JomgoQuery;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.OpenApiBaseService;
import com.voyageone.web2.sdk.api.VoApiListRequest;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.VoApiUpdateResponse;


/**
 * Rest webservice Service 层提供基类
 * Created by chuanyu.liang on 15/6/26.
 * @author chuanyu.liang
 */
public abstract class OpenApiCmsBaseService extends OpenApiBaseService {

    /**
     * buildProjection
     * @param request VoApiRequest
     * @param queryObject JomgoQuery
     */
    protected void buildProjection(VoApiRequest request, JomgoQuery queryObject) {
        queryObject.setProjectionExt(getProjection(request));
    }

    /**
     * getProjection
     * @param request VoApiRequest
     * @return String
     */
    protected String[] getProjection(VoApiRequest request) {
        if (StringUtils.isEmpty(request.getFields())) {
            return null;
        }
        String fieldsTmp = request.getFields().replaceAll("[\\s]*;[\\s]*", " ; ");
        return fieldsTmp.split(" ; ");
    }

    /**
     * buildSort
     * @param request VoApiRequest
     * @param queryObject queryObject
     */
    protected void buildSort(VoApiRequest request, JomgoQuery queryObject) {
        queryObject.setSort(getSort(request));
    }

    /**
     * getSort
     * @param request VoApiRequest
     * @return String
     */
    protected String getSort(VoApiRequest request) {
        if (StringUtils.isEmpty(request.getSorts())) {
            return null;
        }
        String sortsTmp = request.getSorts().replaceAll("[\\s]*;[\\s]*", ", ").trim();

        String result;
        if (sortsTmp.startsWith("{") && sortsTmp.endsWith("}")) {
            result = sortsTmp;
        } else {
            result = "{ " + sortsTmp + " }";
        }
        return result;
    }

    /**
     * buildLimit
     * @param request VoApiListRequest
     * @param queryObject JomgoQuery
     */
    protected void buildLimit(VoApiListRequest request, JomgoQuery queryObject) {
        if (request.getIsPage()) {
            int pageSize = request.getPageSize();
            if (pageSize < 1) {
                pageSize = 1;
            }
            // 因为下载取数据的分页不能最大100,所以取消这个设置
//            if (pageSize > 100) {
//                pageSize = 100;
//            }

            int pageNo = request.getPageNo();
            if (pageNo < 1) {
                pageNo = 1;
            }

            queryObject.setLimit(pageSize);
            queryObject.setSkip((pageNo - 1) * pageSize);
        }
    }

    /**
     * setResultCount
     * @param response VoApiUpdateResponse
     * @param bulkWriteResult BulkWriteResult
     */
    protected void setResultCount(VoApiUpdateResponse response, BulkWriteResult bulkWriteResult) {
        response.setInsertedCount(response.getInsertedCount() + bulkWriteResult.getInsertedCount());
        response.setMatchedCount(response.getMatchedCount() + bulkWriteResult.getMatchedCount());
        response.setModifiedCount(response.getModifiedCount() + bulkWriteResult.getModifiedCount());
        response.setRemovedCount(response.getRemovedCount() + bulkWriteResult.getRemovedCount());
    }

}
