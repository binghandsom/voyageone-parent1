package com.voyageone.task2.cms.mqjob.advanced.search;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by james on 2017/1/13.
 */
@Service
@RabbitListener()
public class CmsPlatformCategoryUpdateMQJob extends TBaseMQCmsService<CmsPlatformCategoryUpdateMQMessageBody> {

    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(CmsPlatformCategoryUpdateMQMessageBody messageBody) throws Exception {

        List<String> productCodes = messageBody.getProductCodes();
        Integer cartId = messageBody.getCartId();

        List<BulkUpdateModel> bulkList = new ArrayList<>(productCodes.size());
        for (String productCode : productCodes) {
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P" + cartId + ".pCatPath", messageBody.getpCatPath());
            updateMap.put("platforms.P" + cartId + ".pCatId", messageBody.getpCatId());
            updateMap.put("platforms.P" + cartId + ".pCatStatus", 1);
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", productCode);
            HashMap<String, Object> queryMap2 = new HashMap<>();
            queryMap2.put("$in", new String[]{null, ""});
            queryMap.put("platforms.P" + cartId + ".pCatPath", queryMap2);
            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        BulkWriteResult bulkWriteResult = productService.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, messageBody.getSender(), "$set");
        cmsSuccessLog(messageBody, "更新了"+bulkWriteResult.getModifiedCount()+"条");
    }
}
