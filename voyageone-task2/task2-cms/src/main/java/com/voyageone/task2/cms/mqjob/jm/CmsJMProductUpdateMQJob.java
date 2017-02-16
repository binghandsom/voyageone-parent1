package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.service.bean.cms.OperationResult;
import com.voyageone.service.impl.cms.jumei2.JuMeiProductPlatform3Service;
import com.voyageone.service.impl.cms.vomq.vomessage.body.JMProductUpdateMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public void onStartup(JMProductUpdateMQMessageBody messageBody) {

        List<OperationResult> listResult = service.updateJmByPromotionId(messageBody.getCmsBtJmPromotionId());

        super.count = 0;
        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        listResult.forEach(f -> {
            if (!f.isResult()) {
                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode(f.getCode());

                StringBuilder sb = new StringBuilder();
                errorInfo.setMsg(sb.append(f.getMsg()).append("errorId:").append(f.getId()).toString());
                failList.add(errorInfo);
            } else {
                super.count ++;
            }
        });

        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", listResult.size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
