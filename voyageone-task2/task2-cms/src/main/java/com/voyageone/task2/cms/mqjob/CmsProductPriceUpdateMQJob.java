package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.impl.cms.product.CmsProductPriceUpdateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
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
@RabbitListener()
public class CmsProductPriceUpdateMQJob extends TBaseMQCmsService<ProductPriceUpdateMQMessageBody> {

    @Autowired
    private CmsProductPriceUpdateService cmsProductPriceUpdateService;

    @Override
    public void onStartup(ProductPriceUpdateMQMessageBody messageBody) throws Exception {
        cmsProductPriceUpdateService.updateProductAndGroupPrice(messageBody.getParams());
    }
}
