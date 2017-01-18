package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.exception.BusinessException;
import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.CmsProductPriceUpdateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.ProductPriceUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channelId", messageBody.getChannelId());
        params.put("productId", messageBody.getProdId());
        params.put("cartId", messageBody.getCartId());
        try {
            cmsProductPriceUpdateService.updateProductAndGroupPrice(params);
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                cmsLog(messageBody, OperationLog_Type.businessException, e.getMessage());
            } else {
                cmsLog(messageBody, OperationLog_Type.unknownException, e.getMessage());
            }
        }
    }
}
