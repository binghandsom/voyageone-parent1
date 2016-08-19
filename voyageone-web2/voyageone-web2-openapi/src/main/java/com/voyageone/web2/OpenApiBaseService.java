package com.voyageone.web2;

import com.voyageone.common.logger.VOAbsLoggable;
import com.voyageone.common.util.StringUtils;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiRequest;
import com.voyageone.web2.sdk.api.exception.ApiException;


/**
 * Rest webservice Service 层提供基类
 * Created by chuanyu.liang on 15/6/26.
 * @author chuanyu.liang
 */
public abstract class OpenApiBaseService extends VOAbsLoggable {
    /**
     * Check Request
     * @param request Request
     */
    protected void checkCommRequest(VoApiRequest request) {
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

}
