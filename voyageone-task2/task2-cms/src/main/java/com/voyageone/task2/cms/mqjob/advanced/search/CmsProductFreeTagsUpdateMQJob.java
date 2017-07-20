package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProductFreeTagsUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsProductFreeTagsUpdateService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * CmsProductFreeTagsUpdateMQJob    高级搜索-设置自由标签
 *
 * @author sunpt on 2017/01/12
 * @version 2.0.0
 */
@Service
@VOSubRabbitListener//(queues = CmsMqRoutingKey.CMS_TASK_AdvSearch_GetBIDataJob)
public class CmsProductFreeTagsUpdateMQJob extends TBaseMQCmsSubService<CmsProductFreeTagsUpdateMQMessageBody> {

    @Autowired
    CmsProductFreeTagsUpdateService service;

    @Override
    public void onStartup(CmsProductFreeTagsUpdateMQMessageBody messageBody) throws Exception {
        List<String> productCodeList;
        if("usa".equals(messageBody.getType())){
            productCodeList =  service.setUsProductFreeTags(messageBody);
        }else{
            productCodeList =  service.setProductFreeTags(messageBody);
        }
        super.count = productCodeList.size();
    }
}
