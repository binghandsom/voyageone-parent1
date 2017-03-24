package com.voyageone.task2.cms.mqjob.stock;

import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBrandBlockMQMessageBody;
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
public class CmsStockCartChangedStockMQJob extends TBaseMQCmsService<CmsBrandBlockMQMessageBody> {


    @Override
    public void onStartup(CmsBrandBlockMQMessageBody messageBody) throws Exception {

    }
}
