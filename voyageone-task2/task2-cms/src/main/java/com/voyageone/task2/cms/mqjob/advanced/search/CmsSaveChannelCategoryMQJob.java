package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsSaveChannelCategoryService;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private CmsSaveChannelCategoryService saveChannelCategoryService;

    @Override
    public void onStartup(SaveChannelCategoryMQMessageBody messageBody) throws Exception {

        List<String> codeList = (List) messageBody.getParams().get("productIds");
        super.count = codeList.size();
        List<CmsBtOperationLogModel_Msg> failList = saveChannelCategoryService.onStartup(messageBody.getParams());
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", codeList.size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
            super.isFailed = true;
        }
    }
}
