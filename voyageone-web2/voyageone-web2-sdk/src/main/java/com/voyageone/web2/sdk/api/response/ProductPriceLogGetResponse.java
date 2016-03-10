package com.voyageone.web2.sdk.api.response;

import com.voyageone.web2.sdk.api.VoApiListResponse;
import com.voyageone.web2.sdk.api.domain.CmsBtPriceLogModel;

import java.util.List;


/**
 * /product/priceLog/get Response
 * Created on 2015-12-14
 *
 * @author chuanyu.liang
 * @version 2.0.0
 * @since. 2.0.0
 */
public class ProductPriceLogGetResponse extends VoApiListResponse {

    /**
     * 数据体信息
     */
    private List<CmsBtPriceLogModel> priceList;

    public List<CmsBtPriceLogModel> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<CmsBtPriceLogModel> priceList) {
        this.priceList = priceList;
    }
}
