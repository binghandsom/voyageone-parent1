package com.voyageone.web2.cms.views.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.web2.base.BaseAppService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 查询产品的各种变更历史
 * Created by jiangjusheng on 2016/07/15
 * @author jiangjusheng
 */
@Service
public class CmsProductHistoryLogService extends BaseAppService {

    /**
     * 查询商品上下架操作历史
     */
    public List<CmsBtPlatformActiveLogModel> getPutOnOffLogList(Map<String, Object> params) {




        return null;
    }
}
