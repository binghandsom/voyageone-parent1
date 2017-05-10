package com.voyageone.web2.sdk.api.response.wms;

import com.google.common.collect.ImmutableList;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/4/13.
 *
 */
public class GetStoreStockDetailResponse extends VoApiResponse {

    private GetStoreStockDetailData data;

    public GetStoreStockDetailData getData() {
        return data;
    }

    public void setData(GetStoreStockDetailData data) {
        this.data = data;
    }
}
