package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;


/**
 * Respose Entity
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 */
public class VoResponseEntity<T> extends ResponseEntity<T> {

    /**
     * Create a new {@code ResponseEntity} with the given status code, and no body nor headers.
     * @param statusCode the status code
     */
    public VoResponseEntity(HttpStatus statusCode) {
        super(statusCode);
    }

    /**
     * Create a new {@code ResponseEntity} with the given body and status code, and no headers.
     * @param body the entity body
     * @param statusCode the status code
     */
    public VoResponseEntity(T body, HttpStatus statusCode) {
        super(body, statusCode);
    }

    /**
     * Create a new {@code HttpEntity} with the given headers and status code, and no body.
     * @param headers the entity headers
     * @param statusCode the status code
     */
    public VoResponseEntity(MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(headers, statusCode);
    }

    /**
     * Create a new {@code HttpEntity} with the given body, headers, and status code.
     * @param body the entity body
     * @param headers the entity headers
     * @param statusCode the status code
     */
    public VoResponseEntity(T body, MultiValueMap<String, String> headers, HttpStatus statusCode) {
        super(body, headers, statusCode);
    }


    /**
     * Error Object
     */
    private ApiException error;

    public ApiException getError() {
        return error;
    }

    public void setError(ApiException error) {
        this.error = error;
    }


    @Override
    public String toString() {
        String result = super.toString();
        if (result != null && result.length()>1) {
            result = result.substring(0, result.length()-1);
        }
        result = result + ", " + error.toString() + '>';
        return result;
    }

}
