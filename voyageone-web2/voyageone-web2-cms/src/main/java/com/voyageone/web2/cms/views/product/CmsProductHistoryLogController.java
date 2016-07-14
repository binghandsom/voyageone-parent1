package com.voyageone.web2.cms.views.product;

import com.voyageone.service.model.cms.mongo.product.CmsBtPlatformActiveLogModel;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询产品的各种变更历史
 * Created by jiangjusheng on 2016/07/15
 * @author jiangjusheng
 */
@RestController
@RequestMapping(method = RequestMethod.POST, value = CmsUrlConstants.PRODUCT.HISTORY.ROOT)
public class CmsProductHistoryLogController extends CmsController {

    @Autowired
    private CmsProductHistoryLogService productHistoryLogService;

    /**
     * 查询商品上下架操作历史
     */
    @RequestMapping(CmsUrlConstants.PRODUCT.HISTORY.GET_PUTONOFF_LOG_LIST)
    public AjaxResponse getPutOnOffLogList(@RequestBody Map params) {
        Map<String, Object> result = new HashMap<>();
        List<CmsBtPlatformActiveLogModel> rsList = productHistoryLogService.getPutOnOffLogList(params, getUser(), getLang());
        int count = 0;
        if (rsList != null) {
            count = rsList.size();
        }
        result.put("loglist", rsList);
        result.put("total", count);
        return success(result);
    }

}
