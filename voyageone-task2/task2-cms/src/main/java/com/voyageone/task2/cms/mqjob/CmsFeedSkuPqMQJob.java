package com.voyageone.task2.cms.mqjob;


import com.voyageone.service.impl.cms.feed.FeedToCmsService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedSkuPqMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;


/**
 * @description VMS的价格和库存改变时请求MQ
 *              更新CMS=》feedInfo信息
 * @author  piao
 */
@Service
@RabbitListener()
public class CmsFeedSkuPqMQJob extends TBaseMQCmsService<CmsFeedSkuPqMQMessageBody> {

    @Autowired
    FeedToCmsService feedToCmsService;

    @Override
    public void onStartup(CmsFeedSkuPqMQMessageBody messageBody) throws Exception {
        String modifier = null;     //触发者
        
        if(messageBody.getModifier() == null)
            modifier = "VMS监控库存价格";

        Map<String, List<CmsBtOperationLogModel_Msg>> result = feedToCmsService.updateFeedSkuPrice(messageBody.getChannelId(), messageBody.getSkuInfo(), modifier);

        List<CmsBtOperationLogModel_Msg> success = result.get("success"),
                failed = result.get("failed");

        /** 写入错误信息日志 */
        if (result.get("failed").size() > 0) {
            String comment = String.format("处理总数(%s), 处理成功数(%s),处理失败数(%s)",
                    messageBody.getSkuInfo().size(),
                    success.size(),
                    failed.size());

            cmsSuccessIncludeFailLog(messageBody, comment, failed);
        }

    }
}
