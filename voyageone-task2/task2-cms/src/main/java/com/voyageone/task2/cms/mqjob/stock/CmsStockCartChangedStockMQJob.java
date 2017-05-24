package com.voyageone.task2.cms.mqjob.stock;

import com.voyageone.common.util.JacksonUtil;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductStockService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.stock.CmsStockCartChangedStockMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author piao
 * @description 库存更新     WMS =》CMS
 * 接收WMS推送过来的渠道库存
 */
@Service
@VOSubRabbitListener
public class CmsStockCartChangedStockMQJob extends TBaseMQCmsSubService<CmsStockCartChangedStockMQMessageBody> {

    @Autowired
    ProductStockService productStockService;

    @Override
    public void onStartup(CmsStockCartChangedStockMQMessageBody messageBody) throws Exception {
        $info("WMS->CMS批量更新库存，消息内容个数：" + messageBody.getCartChangedStocks().size());
        messageBody.setSender("CmsStockCartChangedStockMQJob");
        List<CmsBtOperationLogModel_Msg> failList = productStockService.updateProductStock(messageBody.getCartChangedStocks());
        if (CollectionUtils.isEmpty(failList)) {
            cmsSuccessLog(messageBody, "WMS->CMS批量更新库存");
        } else {
            cmsSuccessIncludeFailLog(messageBody, "WMS->CMS批量更新库存", failList);
        }
    }
}
