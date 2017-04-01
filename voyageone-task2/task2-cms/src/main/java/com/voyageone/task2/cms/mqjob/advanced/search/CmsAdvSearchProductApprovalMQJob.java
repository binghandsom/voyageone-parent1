package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.components.rabbitmq.namesub.IMQSubBeanNameAll;
import com.voyageone.service.impl.cms.product.search.CmsAdvSearchProductApprovalService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchProductApprovalMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 高级检索-商品审批
 *
 * @Author dell
 * @Create 2017-01-13 13:06
 */
@Service
@VOSubRabbitListener
public class CmsAdvSearchProductApprovalMQJob extends TBaseMQCmsSubService<AdvSearchProductApprovalMQMessageBody> {

    @Autowired
    protected CmsAdvSearchProductApprovalService cmsAdvSearchProductApprovalService;

    @Override
    public void onStartup(AdvSearchProductApprovalMQMessageBody messageBody) throws Exception {

        super.count = messageBody.getProductCodes().size();
        List<CmsBtOperationLogModel_Msg> failList = cmsAdvSearchProductApprovalService.approval(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
