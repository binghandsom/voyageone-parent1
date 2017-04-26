package com.voyageone.task2.cms.mqjob.stock;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductStockService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.stock.CmsStockCartChangedStockMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
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

        /**执行批量lock商品*/
        List<CmsBtOperationLogModel_Msg> result = productStockService.updateProductStock(messageBody.getStockList());

        if (result.size() > 0) {
            String comment = String.format("高级检索批量lock商品处理总件数(%s), 处理失败件数(%s)", messageBody.getStockList().size(), result.size());
            cmsSuccessIncludeFailLog(messageBody, comment, result);
        }

    }
}
