package com.voyageone.task2.cms.mqjob;

import com.taobao.api.ApiException;
import com.voyageone.common.configs.Shops;
import com.voyageone.common.configs.beans.ShopBean;
import com.voyageone.components.tmall.service.TbPromotionService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsTeJiaBaoDelMQMessageBody;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        for(String numIId : messageBody.getNumIId()){

            try{
                tbPromotionService.removePromotion(shopBean, Long.parseLong(numIId), messageBody.getTejiabaoId());
            }catch (ApiException e){
                $error(e);
                cmsBusinessExLog(messageBody, "numIId="+numIId+" message: "+e.getMessage());
            }

        }
    }
}
