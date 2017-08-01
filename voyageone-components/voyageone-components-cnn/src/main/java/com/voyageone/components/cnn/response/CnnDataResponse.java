package com.voyageone.components.cnn.response;

import com.voyageone.components.cnn.response.data.DataBean;

/**
 * Created by morse on 2017/7/31.
 */
public class CnnDataResponse<T extends DataBean> extends AbstractCnnResponse {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
