package com.voyageone.task2.cms.mqjob;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.PlatformActiveLogMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.task2.cms.service.platform.CmsPlatformActiveLogService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 记录上下架操作历史 Job
 *
 * @Author rex
 * @Create 2017-01-04 19:34
 */
@Service
@RabbitListener()
public class CmsPlatformActiveLogMQJob extends TBaseMQCmsService<PlatformActiveLogMQMessageBody> {

    @Autowired
    private CmsPlatformActiveLogService cmsPlatformActiveLogService;

    @Override
    public void onStartup(PlatformActiveLogMQMessageBody messageBody) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channelId", messageBody.getChannelId());
        params.put("cartIdList", messageBody.getChannelId());
        params.put("activeStatus", messageBody.getActiveStatus());
        params.put("creater", messageBody.getUserName());
        params.put("comment", messageBody.getComment());
        params.put("codeList", messageBody.getProductCodes());

        try {
            List<Map<String, String>> failList = cmsPlatformActiveLogService.onStartup(params);
            if (failList != null && failList.size() > 0) {
                cmsLog(messageBody, OperationLog_Type.successIncludeFail, JacksonUtil.bean2Json(failList));
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsLog(messageBody, OperationLog_Type.businessException, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }

    }
}
