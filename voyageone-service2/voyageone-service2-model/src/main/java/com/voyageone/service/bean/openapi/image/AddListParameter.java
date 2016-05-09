package com.voyageone.service.bean.openapi.image;

import java.util.List;

/**
 * Created by dell on 2016/4/26.{data[{},{}]}
 */
public class AddListParameter {
    protected List<CreateImageParameter> data;

    public List<CreateImageParameter> getData() {
        return data;
    }

    public void setData(List<CreateImageParameter> data) {
        this.data = data;
    }
}
