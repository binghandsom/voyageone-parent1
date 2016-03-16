package com.voyageone.web2.cms.views.pop.history;

import com.voyageone.common.configs.Enums.TypeConfigEnums;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import com.voyageone.web2.base.BaseAppService;
import com.voyageone.web2.core.bean.UserSessionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gubuchun 15/12/18
 * @version 2.0.0
 */
@Service
public class CmsPriceHistoryService extends BaseAppService {

    @Autowired
    private ProductService productService;

    /**
     * @param params 检索条件
     * @return 价格修改记录列表
     */
    public Map<String, Object> getPriceHistory(Map<String, Object> params, UserSessionBean userInfo, String language) {
        Map<String, Object> result = new HashMap<>();

        List<CmsBtPriceLogModel> list = productService.getPriceLog(userInfo.getSelChannelId(), params);
        result.put("list", list);
        int total = productService.getPriceLogCnt(userInfo.getSelChannelId(), params);
        result.put("total", total);

        // 获取PriceType
        result.put("priceTypeList", TypeConfigEnums.MastType.priceType.getList(language));
        return result;
    }
}
