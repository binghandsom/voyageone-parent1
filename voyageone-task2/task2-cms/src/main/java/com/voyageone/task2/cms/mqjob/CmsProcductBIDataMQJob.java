package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsProcductBIDataMQMessageBody;
import com.voyageone.service.impl.cms.vomqservice.CmsProcductBIDataService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 从bi基础数据表取得产品的bi信息，并保存 (浏览量 访客数 加购件数 收藏人数)
 *
 * @author jiangjusheng on 2016/08/30
 * @version 2.0.0
 */
@Service
@RabbitListener//(queues = CmsMqRoutingKey.CMS_TASK_AdvSearch_GetBIDataJob)
public class CmsProcductBIDataMQJob extends TBaseMQCmsService<CmsProcductBIDataMQMessageBody> {

    @Autowired
    CmsProcductBIDataService cmsProcductBIDataService;

    @Override
    public void onStartup(CmsProcductBIDataMQMessageBody messageMap) throws Exception {
        cmsProcductBIDataService.onStartup(messageMap);
    }
}
