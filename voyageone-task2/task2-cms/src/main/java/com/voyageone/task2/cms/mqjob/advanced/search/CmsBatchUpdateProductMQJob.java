package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.BatchUpdateProductMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsBatchUpdateService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 批量更新商品Job
 *
 * @Author rex
 * @Create 2017-01-03 14:03
 */
@Service
@RabbitListener()
public class CmsBatchUpdateProductMQJob extends TBaseMQCmsService<BatchUpdateProductMQMessageBody> {

    @Autowired
    private CmsBatchUpdateService batchUpdateService;

    @Override
    public void onStartup(BatchUpdateProductMQMessageBody messageBody) {
        Map<String, String> failMap = batchUpdateService.updateProductComField(messageBody);
        if (failMap != null && failMap.size() > 0) {
            cmsSuccessIncludeFailLog(messageBody, String.format("Code总数(%s) 失败(%s) \\r\\n %s", messageBody.getProductCodes().size(), failMap.size(), JacksonUtil.bean2Json(failMap)));
        } else {
            cmsSuccessLog(messageBody, String.format("Code总数(%s)", messageBody.getProductCodes().size()));
        }
    }
}
