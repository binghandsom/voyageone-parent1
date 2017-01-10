package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.service.bean.cms.OperationResult;
import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.jm.JMProductUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CmsJMProductUpdateMQJob  聚美平台上传更新
 *
 * @author peitao 2016/12/30.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()
public class CmsJMProductUpdateMQJob extends TBaseMQCmsService<JMProductUpdateMQMessageBody> {
    @Autowired
    private JuMeiProductPlatform3Service service;

    @Override
    public void onStartup(JMProductUpdateMQMessageBody messageBody) throws Exception {
        int id = messageBody.getCmsBtJmPromotionId();
        List<OperationResult> listResult = service.updateJmByPromotionId(id);
        StringBuilder sb = new StringBuilder();
        long errorCount = listResult.stream().filter(f -> !f.isResult()).count();
        listResult.forEach(f -> {
            if (!f.isResult()) {
                sb.append("code:").append(f.getCode()).append(":").append(f.getMsg()).append("errorId:").append(f.getId()).append("\\r\\n");
            }
        });
        if (errorCount > 0) {
            cmsBusinessExLog(messageBody, String.format("code总数(%s) 失败(%s) \\r\\n %s", listResult.size(), errorCount, sb.toString()));
        } else {
            cmsBusinessExLog(messageBody, "执行成功");
        }
    }
}
