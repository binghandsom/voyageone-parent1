package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateListDelistStatusMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/7/18.
 */
@Service
@RabbitListener()
public class CmsBtProductUpdateListDelistStatusMQJob extends TBaseMQCmsService<CmsBtProductUpdateListDelistStatusMQMessageBody> {
    @Autowired
    ProductService productService;
    @Autowired
    PlatformProductUploadService platformProductUploadService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;
    @Override
    public void onStartup(CmsBtProductUpdateListDelistStatusMQMessageBody messageBody) throws Exception {
        if (messageBody != null){
            Integer cartId = messageBody.getCartId();
            String channelId = messageBody.getChannelId();
            String activeStatus = messageBody.getActiveStatus();
            List<String> productCodes = messageBody.getProductCodes();
            if (ListUtils.notNull(productCodes)){
                for (String productCode : productCodes) {
                    //获取到商品对象
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, productCode);
                    Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                    if (usPlatforms!= null){
                        CmsBtProductModel_Platform_Cart usPlatform = usPlatforms.get("P" + cartId);
                        String status = usPlatform.getStatus();
                        //status为Approve状态才可以进行上下架
                        if ("Approve".equals(status)){

                            JongoUpdate jongoUpdate = new JongoUpdate();
                            jongoUpdate.setQuery("{\"common.fields.code\":#}");
                            jongoUpdate.setQueryParameters(productCode);
                            jongoUpdate.setUpdate("{$set:{\"usPlatforms.P"+ cartId +".pStatus\":#}}");
                            if ("list".equals(activeStatus)){
                                //上架操作
                                jongoUpdate.setUpdateParameters("OnSale");
                            }
                            if ("deList".equals(activeStatus)){
                                //下架操作
                                jongoUpdate.setUpdateParameters("InStock");
                            }
                            cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
                            platformProductUploadService.saveCmsBtUsWorkloadModel(channelId,cartId,productCode,null,0,messageBody.getSender());
                        }
                    }
                }
            }

        }

    }
}
