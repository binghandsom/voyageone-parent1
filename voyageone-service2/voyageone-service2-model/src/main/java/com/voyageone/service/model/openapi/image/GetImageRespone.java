package com.voyageone.service.model.openapi.image;
import com.voyageone.service.model.openapi.OpenApiRespone;
public class GetImageRespone extends OpenApiRespone {
    GetImageResultData resultData;
    public GetImageRespone() {
        this.setResultData(new GetImageResultData());
    }
    public GetImageResultData getResultData() {
        return resultData;
    }
    public void setResultData(GetImageResultData resultData) {
        this.resultData = resultData;
    }
}
