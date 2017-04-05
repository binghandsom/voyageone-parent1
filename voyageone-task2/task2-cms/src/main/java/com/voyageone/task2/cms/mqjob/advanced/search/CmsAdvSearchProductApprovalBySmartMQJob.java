package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchProductApprovalService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalBySmartMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 高级检索-智能上新
 *
 * @Author edward.lin
 * @Create 2017-03-14 16:06
 */
@Service
@VOSubRabbitListener
public class CmsAdvSearchProductApprovalBySmartMQJob extends TBaseMQCmsSubService<AdvSearchProductApprovalBySmartMQMessageBody> {

    @Autowired
    protected CmsAdvSearchProductApprovalService cmsAdvSearchProductApprovalService;

    @Override
    public void onStartup(AdvSearchProductApprovalBySmartMQMessageBody messageBody) throws Exception {

        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = cmsAdvSearchProductApprovalService.intelligent(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
