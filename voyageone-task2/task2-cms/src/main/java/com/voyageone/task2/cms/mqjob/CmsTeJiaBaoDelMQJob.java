package com.voyageone.task2.cms.mqjob;

import com.taobao.api.ApiException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.service.TbPromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsTeJiaBaoDelMQMessageBody;
import com.voyageone.service.model.cms.mongo.CmsBtOperationLogModel_Msg;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 2017/1/16.
 */
@Service
@RabbitListener()
public class CmsTeJiaBaoDelMQJob extends TBaseMQCmsService<CmsTeJiaBaoDelMQMessageBody> {

    @Autowired
    private TbPromotionService tbPromotionService;

    @Override
    public void onStartup(CmsTeJiaBaoDelMQMessageBody messageBody) throws Exception {

        ShopBean shopBean = Shops.getShop(messageBody.getChannelId(), messageBody.getCartId());
        super.count = messageBody.getNumIId().size();

        List<CmsBtOperationLogModel_Msg> failList = new ArrayList<>();
        for(String numIId : messageBody.getNumIId()){

            try{
                tbPromotionService.removePromotion(shopBean, Long.parseLong(numIId), messageBody.getTejiabaoId());
            }catch (ApiException e){
                $error(e);

                CmsBtOperationLogModel_Msg errorInfo = new CmsBtOperationLogModel_Msg();
                errorInfo.setSkuCode("numIId:" + numIId);
                errorInfo.setMsg(e.getMessage());
                failList.add(errorInfo);
            }
        }

        if (failList.size() > 0) {
            //写业务错误日志
            String comment = String.format("处理总件数(%s), 处理失败件数(%s)", messageBody.getNumIId().size(), failList.size());
            cmsSuccessIncludeFailLog(messageBody, comment, failList);
        }

    }
}
