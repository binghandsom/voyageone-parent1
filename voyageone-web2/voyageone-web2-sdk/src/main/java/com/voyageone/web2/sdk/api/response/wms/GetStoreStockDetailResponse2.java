package com.voyageone.web2.sdk.api.response.wms;

import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/6/20.
 */
public class GetStoreStockDetailResponse2 extends VoApiResponse{

    private GetStoreStockDetailData2 data;

    public GetStoreStockDetailData2 getData() {
        return data;
    }

    public void setData(GetStoreStockDetailData2 data) {
        this.data = data;
    }
}
