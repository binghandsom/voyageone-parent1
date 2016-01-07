package com.voyageone.web2.cms.views.pop.price;

import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.sdk.api.VoApiDefaultClient;
import com.voyageone.web2.sdk.api.request.ProductPriceLogGetRequest;
import com.voyageone.web2.sdk.api.response.ProductPriceLogGetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Service
public class CmsPriceHistoryService extends BaseAppService {

    @Autowired
    protected VoApiDefaultClient voApiClient;

    /**
     * @param params 检索条件
     * @return 价格修改记录列表
     */
    public Map<String, Object> getPriceHistory(Map<String, Object> params, String channelId) {
        ProductPriceLogGetRequest requestModel = new ProductPriceLogGetRequest(channelId);
        Map<String, Object> result = new HashMap<>();
        requestModel.setOffset((int)params.get("offset"));
        requestModel.setRows((int) params.get("rows"));

        boolean flag = (boolean) params.get("flag");
        if (flag) {
            String code = (String) params.get("code");
            requestModel.setProductCode(code);
        } else {
            String sku = (String) params.get("sku");
            requestModel.setProductSkuCode(sku);
        }
        //SDK取得Product 数据
        ProductPriceLogGetResponse response = voApiClient.execute(requestModel);
        result.put("list", response.getPriceList());
        result.put("total", response.getTotalCount());
        return result;
    }
}
