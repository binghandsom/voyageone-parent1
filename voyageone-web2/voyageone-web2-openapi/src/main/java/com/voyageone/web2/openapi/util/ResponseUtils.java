package com.voyageone.web2.openapi.util;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.base.exception.SystemException;
import com.voyageone.service.bean.vms.channeladvisor.ErrorModel;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiException;
import com.voyageone.web2.openapi.channeladvisor.exception.CAApiExceptions;
import com.voyageone.web2.sdk.api.VoApiConstants;
import com.voyageone.web2.sdk.api.VoApiResponse;
import com.voyageone.service.bean.vms.channeladvisor.enums.ErrorIDEnum;
import com.voyageone.service.bean.vms.channeladvisor.enums.ResponseStatusEnum;
import com.voyageone.service.bean.vms.channeladvisor.response.ActionResponse;
import com.voyageone.web2.sdk.api.exception.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Date;

/**
 * Response Utils
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public class ResponseUtils {

    /**
     * build API Error Info
     */
    public static VoApiResponse buildError(Exception exception) {
        String code;
        String message;

        if (exception instanceof ApiException) {
            ApiException apiException = (ApiException)exception;
            code = apiException.getErrCode();
            message = apiException.getErrMsg();
        } else if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException)exception;
            code = businessException.getCode();
            message = businessException.getMessage();
        } else if (exception instanceof SystemException) {
            SystemException systemException = (SystemException)exception;
            code = systemException.getCode();
            message = systemException.getMessage();
        } else {
            code = VoApiConstants.VoApiErrorCodeEnum.ERROR_CODE_70000.getErrorCode();
            message = exception.getMessage();
        }

        String messageNew = StringUtils.isEmpty(message) ? exception.getClass().getName() : message;

        return new VoApiResponse(code, messageNew);
    }


    /**
     * build Channel Advisor API Error Info
     */
    public static ActionResponse buildCAError(Exception exception) {
        String code;
        String message = null;

        if (exception instanceof CAApiException) {
            CAApiException caException = (CAApiException)exception;
            code = caException.getErrCode();
            message = caException.getErrMsg();
        } else if (exception instanceof HttpMessageNotReadableException) {
            code = String.valueOf(ErrorIDEnum.InvalidRequiredParameter.getCode());
            if (exception.getCause() instanceof InvalidFormatException) {
                if (Date.class.equals(((InvalidFormatException)exception.getCause()).getTargetType())) {
                    message = "DateFormat of ShippedDateUtc is invalid. The valid date format is something like 2016-12-29T16:28:01.0000000Z.";
                }
            }
            if (message == null) {
                message = exception.getMessage();
            }
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            code = String.valueOf(ErrorIDEnum.InvalidRequest.getCode());
            //message = ErrorIDEnum.InvalidRequest.getDefaultMessage();
            message="The endpoint URL does not exist. Please check the URL.";
        } else {
            code = String.valueOf(ErrorIDEnum.SystemUnavailable.getCode());
            message = ErrorIDEnum.SystemUnavailable.getDefaultMessage();
        }

        String messageNew = StringUtils.isEmpty(message) ? exception.getClass().getName() : message;

        ActionResponse response = new ActionResponse();
        response.setResponseBody(null);
        response.setStatus(ResponseStatusEnum.Failed);
        response.setPendingUri(null);
        response.setHasErrors(true);

        if(exception instanceof CAApiExceptions){
            if(CollectionUtils.isEmpty(((CAApiExceptions) exception).getCaApiExceptions())){
                ErrorIDEnum errorIDEnum = ErrorIDEnum.SystemUnavailable;
                response.addError(new ErrorModel(errorIDEnum));
            }else{
                for (CAApiException exceptionItem : ((CAApiExceptions) exception).getCaApiExceptions()) {
                    ErrorModel errorModel;
                    String exceptionItemMsg=exceptionItem.getErrMsg();
                    ErrorIDEnum errorIDEnum = ErrorIDEnum.getInstance(exceptionItem.getErrCode());
                    if(StringUtils.isEmpty(exceptionItemMsg)){
                        errorModel=new ErrorModel(errorIDEnum);
                    }else {
                        errorModel=new ErrorModel(errorIDEnum,exceptionItemMsg);
                    }
                    response.addError(errorModel);
                }
            }
        }else {
            ErrorIDEnum errorIDEnum = ErrorIDEnum.getInstance(code);
            response.addError(new ErrorModel(errorIDEnum, messageNew));
        }

        return response;
    }
}
