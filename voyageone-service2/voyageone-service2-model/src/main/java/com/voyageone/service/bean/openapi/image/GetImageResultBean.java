package com.voyageone.service.bean.openapi.image;

import com.voyageone.service.bean.openapi.OpenApiResultBean;

public class GetImageResultBean extends OpenApiResultBean {
    GetImageResultData resultData;

    public GetImageResultBean() {
        this.setResultData(new GetImageResultData());
    }

    public GetImageResultData getResultData() {
        return resultData;
    }

    public void setResultData(GetImageResultData resultData) {
        this.resultData = resultData;
    }
}
