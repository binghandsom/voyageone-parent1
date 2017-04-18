package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.prices.PlatformPriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 指定SKU价格变更，异步变更商品价格
 *
 * @Author rex
 * @Create 2017-01-03 16:06
 */
@Service
@VOSubRabbitListener
public class CmsProductPriceUpdateMQJob extends TBaseMQCmsSubService<ProductPriceUpdateMQMessageBody> {

    @Autowired
    private PlatformPriceService platformPriceService;

    @Override
    public void onStartup(ProductPriceUpdateMQMessageBody messageBody) throws Exception {
        
        // TODO: 2017/2/8 edward 这个job会废止掉,直接改成一个共通方法,只计算code级别的价格,而不是计算group级别的价格,不再发送mq
        platformPriceService.updateProductAndGroupPrice(messageBody);
    }
}
