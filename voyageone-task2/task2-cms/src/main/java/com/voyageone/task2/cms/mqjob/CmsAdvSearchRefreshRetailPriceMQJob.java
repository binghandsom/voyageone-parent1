package com.voyageone.task2.cms.mqjob;

import com.voyageone.service.enums.cms.OperationLog_Type;
import com.voyageone.service.impl.cms.product.CmsProductPriceUpdateService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 高级检索-重新计算指导价Job消息实体
 *
 * @Author rex
 * @Create 2016-12-30 16:17
 */
@Service
@RabbitListener()
public class CmsAdvSearchRefreshRetailPriceMQJob extends TBaseMQCmsService<AdvSearchRefreshRetailPriceMQMessageBody> {

    @Autowired
    private CmsProductPriceUpdateService cmsProductPriceUpdateService;

    @Override
    public void onStartup(AdvSearchRefreshRetailPriceMQMessageBody messageBody) throws Exception {
        if (messageBody.getParams() == null || messageBody.getParams().size() <= 0) {
            this.cmsLog(messageBody, OperationLog_Type.parameterException, "批量重新计算指导价MQ参数为空");
            return;
        }
        cmsProductPriceUpdateService.updateProductRetailPrice(messageBody.getParams());
    }
}
