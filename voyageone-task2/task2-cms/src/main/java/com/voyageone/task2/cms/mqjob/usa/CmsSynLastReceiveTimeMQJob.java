package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.WriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.service.impl.cms.feed.FeedInfoService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSynLastReceiveTimeMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by james on 2017/7/12.
 * 同步LastReceiveTime 来自wms的MQ
 */
@Service
@RabbitListener()
public class CmsSynLastReceiveTimeMQJob extends TBaseMQCmsService<CmsSynLastReceiveTimeMQMessageBody>{

    private final ProductService productService;

    private final FeedInfoService feedInfoService;

    @Autowired
    public CmsSynLastReceiveTimeMQJob(ProductService productService, FeedInfoService feedInfoService) {
        this.productService = productService;
        this.feedInfoService = feedInfoService;
    }

    @Override
    public void onStartup(CmsSynLastReceiveTimeMQMessageBody messageBody) throws Exception {

        $info(JacksonUtil.bean2Json(messageBody));
        String lastReceivedOn = DateTimeUtil.format(new Date(messageBody.getReceiveTime()),null);
        JongoUpdate jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"common.fields.code\":{$in:#}}");
        jongoUpdate.setQueryParameters(messageBody.getCodes());
        jongoUpdate.setUpdate("{$set:{\"common.fields.lastReceivedOn\":#}}");
        jongoUpdate.setUpdateParameters(lastReceivedOn);
        WriteResult writeResult = productService.updateMulti(jongoUpdate,"001");
        $info(writeResult.toString());

        jongoUpdate = new JongoUpdate();
        jongoUpdate.setQuery("{\"code\":{$in:#}}");
        jongoUpdate.setQueryParameters(messageBody.getCodes());
        jongoUpdate.setUpdate("{$set:{\"lastReceivedOn\":#}}");
        jongoUpdate.setUpdateParameters(lastReceivedOn);
        feedInfoService.updateMulti(jongoUpdate, "001");
        $info(writeResult.toString());
    }
}
