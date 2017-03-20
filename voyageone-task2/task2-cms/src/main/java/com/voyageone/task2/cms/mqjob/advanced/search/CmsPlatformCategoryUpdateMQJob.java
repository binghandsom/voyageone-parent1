package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by james on 2017/1/13.
 */
@Service
@VOSubRabbitListener
public class CmsPlatformCategoryUpdateMQJob extends TBaseMQCmsSubService<CmsPlatformCategoryUpdateMQMessageBody> {

    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(CmsPlatformCategoryUpdateMQMessageBody messageBody) throws Exception {

        List<String> productCodes = messageBody.getProductCodes();
        super.count = productCodes.size();
        Integer cartId = messageBody.getCartId();

        List<BulkUpdateModel> bulkList = new ArrayList<>(productCodes.size());
        for (String productCode : productCodes) {
            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("platforms.P" + cartId + ".pCatPath", messageBody.getpCatPath());
            updateMap.put("platforms.P" + cartId + ".pCatId", messageBody.getpCatId());
            updateMap.put("platforms.P" + cartId + ".pCatStatus", 1);
            HashMap<String, Object> queryMap = new HashMap<>();
            queryMap.put("common.fields.code", productCode);

            // 聚美和官网同构,平台类目可无条件更新
            if (CartEnums.Cart.TT.getValue() != cartId
                && CartEnums.Cart.LTT.getValue() != cartId
                && CartEnums.Cart.JM.getValue() != cartId) {

                HashMap<String, Object> queryMap2 = new HashMap<>();
                queryMap2.put("$in", new String[]{null, ""});
                queryMap.put("platforms.P" + cartId + ".pCatPath", queryMap2);
            }

            BulkUpdateModel model = new BulkUpdateModel();
            model.setUpdateMap(updateMap);
            model.setQueryMap(queryMap);
            bulkList.add(model);
        }

        productService.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, messageBody.getSender(), "$set");

    }
}
