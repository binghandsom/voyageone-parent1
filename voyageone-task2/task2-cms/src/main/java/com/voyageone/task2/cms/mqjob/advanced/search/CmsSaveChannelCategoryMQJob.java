package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.vomq.vomessage.body.SaveChannelCategoryMQMessageBody;
import com.voyageone.service.impl.cms.vomqjobservice.CmsSaveChannelCategoryService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

        Map<String, String> errorMap = saveChannelCategoryService.onStartup(messageBody.getParams());
        List<String> codeList = (List) messageBody.getParams().get("productIds");
        if (errorMap != null && errorMap.size() > 0) {
            cmsSuccessIncludeFailLog(messageBody, String.format("Code总数(%s) 失败(%s) \\r\\n %s", codeList.size(), errorMap.size(), JacksonUtil.bean2Json(errorMap)));
        } else {
            cmsSuccessLog(messageBody, String.format("Code总数(%s)", codeList.size()));
        }
    }
}
