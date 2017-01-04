package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsAddChannelCategoryService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 保存店铺内分类Job
 *
 * @Author rex
 * @Create 2017-01-03 14:20
 */
@Service
@RabbitListener()
public class CmsSaveChannelCategoryMQJob extends TBaseMQCmsService<SaveChannelCategoryMQMessageBody> {

    @Autowired
    private CmsAddChannelCategoryService saveChannelCategoryService;

    @Override
    public void onStartup(SaveChannelCategoryMQMessageBody messageBody) throws Exception {
        saveChannelCategoryService.onStartup(messageBody.getParams());
    }
}
