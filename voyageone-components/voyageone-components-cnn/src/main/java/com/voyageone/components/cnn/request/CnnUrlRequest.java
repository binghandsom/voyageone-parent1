package com.voyageone.components.cnn.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voyageone.components.cnn.response.AbstractCnnResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by morse on 2017/7/31.
 */
public abstract class CnnUrlRequest<T extends AbstractCnnResponse> extends AbstractCnnRequest<T> {

    protected List<String> params;

    @JsonIgnore
    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void resetParams() {
        params = new ArrayList<>();
    }

    public void addParam(String param) {
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(param);
    }
}
