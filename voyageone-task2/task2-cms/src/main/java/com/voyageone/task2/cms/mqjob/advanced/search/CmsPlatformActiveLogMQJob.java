package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.vomq.vomessage.body.PlatformActiveLogMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
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
        params.put("cartIdList", messageBody.getCartList());
        params.put("activeStatus", messageBody.getActiveStatus());
        params.put("creator", messageBody.getSender());
        params.put("comment", messageBody.getComment());
        params.put("codeList", messageBody.getProductCodes());
        params.put("statusVal",messageBody.getStatusVal());

        try {
            List<Map<String, String>> failList = cmsPlatformActiveLogService.setProductOnSaleOrInStock(params);
            if (failList != null && failList.size() > 0) {
                cmsSuccessIncludeFailLog(messageBody, JacksonUtil.bean2Json(failList));
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsBusinessExLog(messageBody, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }

    }
}
