package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.vomq.vomessage.body.BatchUpdateProductMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsBatchUpdateService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 批量更新商品Job
 *
 * @Author rex
 * @Create 2017-01-03 14:03
 */
@Service
@VOSubRabbitListener
public class CmsBatchUpdateProductMQJob extends TBaseMQCmsSubService<BatchUpdateProductMQMessageBody> {

    @Autowired
    private CmsBatchUpdateService batchUpdateService;

    @Override
    public void onStartup(BatchUpdateProductMQMessageBody messageBody) {

        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = batchUpdateService.updateProductComField(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
