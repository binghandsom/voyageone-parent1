package com.voyageone.task2.cms.mqjob.stock;

import com.voyageone.service.impl.cms.vomq.vomessage.body.stock.CmsStockCartChangedStockMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @description 库存更新	 WMS =》CMS
 *              接收WMS推送过来的渠道库存
 * @author  piao
 */
@Service
@RabbitListener
public class CmsStockCartChangedStockMQJob extends TBaseMQCmsService<CmsStockCartChangedStockMQMessageBody> {


    @Override
    public void onStartup(CmsStockCartChangedStockMQMessageBody messageBody) throws Exception {

    }
}
