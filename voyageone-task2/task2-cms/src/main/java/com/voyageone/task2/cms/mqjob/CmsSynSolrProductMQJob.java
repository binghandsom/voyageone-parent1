package com.voyageone.task2.cms.mqjob;

import com.voyageone.common.components.issueLog.enums.SubSystem;
import com.voyageone.common.masterdate.schema.utils.StringUtil;
import com.voyageone.components.rabbitmq.bean.IMQMessageBody;
import com.voyageone.task2.base.BaseMQAnnoService;
import com.voyageone.task2.base.BaseMQCmsService;
import com.voyageone.task2.cms.job.MqResumeJob;
import com.voyageone.task2.cms.service.search.CmsProductTotalImportToSearchService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by james on 2017/3/31.
 */
@Service
@RabbitListener(queues = "CmsSynSolrProductMQQueue")
public class CmsSynSolrProductMQJob extends BaseMQCmsService {

    @Autowired
    CmsProductTotalImportToSearchService cmsProductTotalImportToSearchService;

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {

        String channelId = (String) messageMap.get("channelId");
        if(StringUtil.isEmpty(channelId)) return;
        cmsProductTotalImportToSearchService.importDataToSearchFromMongo(channelId);
    }
}
