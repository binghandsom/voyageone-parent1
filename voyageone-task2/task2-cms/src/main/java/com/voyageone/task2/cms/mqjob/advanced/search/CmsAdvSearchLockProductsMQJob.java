package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.AdvSearchLockProductsMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import com.voyageone.task2.cms.service.platform.CmsPlatformActiveLogService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author piao
 * @decscription 批量lock产品
 */

@Service
@VOSubRabbitListener
public class CmsAdvSearchLockProductsMQJob extends TBaseMQCmsSubService<AdvSearchLockProductsMQMessageBody> {

    @Autowired
    private CmsPlatformActiveLogService cmsPlatformActiveLogService;
    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(AdvSearchLockProductsMQMessageBody messageBody) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("channelId", messageBody.getChannelId());
        params.put("cartId", messageBody.getCartId());
        params.put("creator", messageBody.getSender());
        params.put("comment", messageBody.getComment());
        params.put("codeList", messageBody.getProductCodes());
        params.put("lock",messageBody.getLock());
        List<CmsBtOperationLogModel_Msg> instockErrors = new ArrayList<CmsBtOperationLogModel_Msg>();

        /**执行批量下架*/
        if(messageBody.getActiveStatus() != null){
            params.put("activeStatus", messageBody.getActiveStatus());
            instockErrors = cmsPlatformActiveLogService.setProductOnSaleOrInStock(params);
        }

        /**执行批量lock商品*/
        List<CmsBtOperationLogModel_Msg> lockErrors  = productService.batchLockProducts(params);

        List<CmsBtOperationLogModel_Msg> listFinal = new ArrayList<>();
        if(instockErrors.size() > 0)
            listFinal.addAll(instockErrors);
        if(lockErrors.size() > 0)
            listFinal.addAll(lockErrors);

        if (listFinal.size() > 0) {
            String comment = String.format("高级检索批量lock商品处理总件数(%s), 处理失败件数(%s)", messageBody.getProductCodes().size(), listFinal.size());
            cmsSuccessIncludeFailLog(messageBody, comment, listFinal);
        }
    }
}

