package com.voyageone.web2.cms.views.system.error;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.CmsBtOperationLogService;
import com.voyageone.web2.base.ajax.AjaxResponse;
import com.voyageone.web2.cms.CmsController;
import com.voyageone.web2.cms.CmsUrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gjl on 2017/1/11.
 */
@RestController
@RequestMapping(value = CmsUrlConstants.SYSTEM.MQ_ERROR.ROOT, method = RequestMethod.POST)
public class CmsMqErrorListController extends CmsController {
    @Autowired
    private CmsBtOperationLogService cmsBtOperationLogService;

    /**
     * Mq数据检索
     */
    @RequestMapping(CmsUrlConstants.SYSTEM.MQ_ERROR.SEARCH)
    public AjaxResponse search(@RequestBody Map params){
        Map<String, Object> result = new HashMap<>();
        // 获取mqTypeList
        result.put("type", OperationLog_Type.getList());

        if (params.get("userName") == null) {
            params.put("userName", getUser().getUserName());
        }
        params.put("channelId", getUser().getSelChannelId());
        // 检索Master品牌匹配的数据
        result.put("mqErrorList", cmsBtOperationLogService.searchMqCmsBtOperationLogData(params));
        // 检索Master品牌匹配的数量
        result.put("mqErrorCnt", cmsBtOperationLogService.searchMqCmsBtOperationLogDataCnt(params));
        //返回数据的类型
        return success(result);
    }

}
