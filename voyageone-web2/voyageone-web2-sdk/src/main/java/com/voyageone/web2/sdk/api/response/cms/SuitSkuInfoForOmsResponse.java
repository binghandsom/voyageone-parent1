package com.voyageone.web2.sdk.api.response.cms;

import com.voyageone.service.bean.cms.product.CombinedSkuInfoBean;
import com.voyageone.service.bean.cms.product.ProductForOmsBean;
import com.voyageone.web2.sdk.api.VoApiResponse;

import java.util.List;

/**
 * Created by rex.wu on 2016/12/1.
 */
public class SuitSkuInfoForOmsResponse extends VoApiResponse {

    private List<CombinedSkuInfoBean> resultInfo;

    public List<CombinedSkuInfoBean> getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo(List<CombinedSkuInfoBean> resultInfo) {
        this.resultInfo = resultInfo;
    }
}
