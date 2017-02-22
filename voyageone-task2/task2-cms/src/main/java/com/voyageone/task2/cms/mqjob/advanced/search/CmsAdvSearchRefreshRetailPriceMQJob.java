package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.prices.CmsBtProductPlatformPriceService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchRefreshRetailPriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private CmsBtProductPlatformPriceService cmsProductPriceUpdateService;

    @Override
    public void onStartup(AdvSearchRefreshRetailPriceMQMessageBody messageBody) throws Exception {

        super.count = messageBody.getCodeList().size();
        List<CmsBtOperationLogModel_Msg> failList = cmsProductPriceUpdateService.updateProductRetailPrice(messageBody);
        if (failList.size() > 0) {
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getCodeList().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }
    }
}
