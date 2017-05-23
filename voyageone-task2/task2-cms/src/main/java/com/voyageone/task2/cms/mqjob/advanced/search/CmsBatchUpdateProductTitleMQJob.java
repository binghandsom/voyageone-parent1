package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 高级检索批量修改商品Title消息Job
 *
 * @Author rex.wu
 * @Create 2017-05-23 14:44
 */
public class CmsBatchUpdateProductTitleMQJob extends TBaseMQCmsSubService<CmsBatchUpdateProductTitleMQMessageBody> {

    @Autowired
    private ProductService productService;


    @Override
    public void onStartup(CmsBatchUpdateProductTitleMQMessageBody messageBody) throws Exception {

        String channelId = messageBody.getChannelId();
        List<String> productCodes = messageBody.getProductCodes();
        String title = messageBody.getTitle();
        String titlePlace = messageBody.getTitlePlace();

        CmsBtProductModel productModel = null;
        for (String code : productCodes) {
            productModel =productService.getProductByCode(channelId, code);
            if (productModel == null) {
                // TODO: 2017/5/23
                continue;
            }

//            productService.

            
        }




    }
}
