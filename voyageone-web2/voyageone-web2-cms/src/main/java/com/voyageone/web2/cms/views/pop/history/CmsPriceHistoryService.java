package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
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
    public Map<String, Object> getPriceHistory(Map<String, Object> params, UserSessionBean userInfo, String language) {
        ProductPriceLogGetRequest requestModel = new ProductPriceLogGetRequest(userInfo.getSelChannelId());
        Map<String, Object> result = new HashMap<>();
        requestModel.setOffset((int)params.get("offset"));
        requestModel.setRows((int) params.get("rows"));
        requestModel.setPriceType((String) params.get("priceType"));

        String flag = (String) params.get("flag");
        if ("code".equals(flag)) {
            String code = (String) params.get("code");
            requestModel.setProductCode(code);
        } else if ("sku".equals(flag)){
            String sku = (String) params.get("sku");
            requestModel.setProductSkuCode(sku);
        }
        //SDK取得Product 数据
        ProductPriceLogGetResponse response = voApiClient.execute(requestModel);
        result.put("list", response.getPriceList());
        result.put("total", response.getTotalCount());

        // 获取PriceType
        result.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));
        return result;
    }
}
