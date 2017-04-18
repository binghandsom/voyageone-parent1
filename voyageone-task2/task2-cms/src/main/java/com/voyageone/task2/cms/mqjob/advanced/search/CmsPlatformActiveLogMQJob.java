package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.vomq.vomessage.body.PlatformActiveLogMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import com.voyageone.task2.cms.service.platform.CmsPlatformActiveLogService;
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
@VOSubRabbitListener
public class CmsPlatformActiveLogMQJob extends TBaseMQCmsSubService<PlatformActiveLogMQMessageBody> {

    @Autowired
    private CmsPlatformActiveLogService cmsPlatformActiveLogService;

    @Override
    public void onStartup(PlatformActiveLogMQMessageBody messageBody) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channelId", messageBody.getChannelId());
        params.put("cartId", messageBody.getCartId());
        params.put("activeStatus", messageBody.getActiveStatus());
        params.put("creator", messageBody.getSender());
        params.put("comment", messageBody.getComment());
        params.put("codeList", messageBody.getProductCodes());

        super.count = messageBody.getProductCodes().size();

        List<CmsBtOperationLogModel_Msg> failList = cmsPlatformActiveLogService.setProductOnSaleOrInStock(params);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }

    }
}
