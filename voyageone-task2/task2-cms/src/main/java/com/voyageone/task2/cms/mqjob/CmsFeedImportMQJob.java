package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.feed.FeedToCms2Service;
import com.voyageone.service.impl.cms.vomq.CmsMqRoutingKey;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsFeedImportMQMessageBody;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.task2.base.BaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by james on 2017/3/9.
 */
@Service
@RabbitListener()
public class CmsFeedImportMQJob extends TBaseMQCmsService<CmsFeedImportMQMessageBody> {

    @Autowired
    FeedToCms2Service feedToCms2Service;

    @Override
    public void onStartup(CmsFeedImportMQMessageBody messageBody) throws Exception {



    }
}
