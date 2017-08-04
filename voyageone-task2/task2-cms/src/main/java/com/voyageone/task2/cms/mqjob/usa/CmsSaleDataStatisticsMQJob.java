package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.service.impl.cms.usa.UsaSaleDataStatisticsService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsSaleDataStatisticsMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by james on 2017/7/20.
 * 自定义销量 数据准备
 */
@Service
@RabbitListener()
public class CmsSaleDataStatisticsMQJob extends TBaseMQCmsService<CmsSaleDataStatisticsMQMessageBody> {

    @Autowired
    private UsaSaleDataStatisticsService usaSaleDataStatisticsService;

    @Override
    public void onStartup(CmsSaleDataStatisticsMQMessageBody messageBody) throws Exception {
        usaSaleDataStatisticsService.SaleDataStatistics(messageBody);

    }


}
