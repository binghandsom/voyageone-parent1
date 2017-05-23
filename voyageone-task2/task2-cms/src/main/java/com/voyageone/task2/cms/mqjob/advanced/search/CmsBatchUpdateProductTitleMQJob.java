package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsBatchUpdateProductTitleMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Field;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsSubService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
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

        List<BulkUpdateModel> bulkUpdateModels = new ArrayList<>();

        CmsBtProductModel productModel = null;
        CmsBtProductModel_Field fields = null;
        for (String code : productCodes) {
            productModel =productService.getProductByCode(channelId, code);
            if (productModel == null) {
                // TODO: 2017/5/23
                continue;
            }
            fields = productModel.getCommon().getFields();

            HashMap<String, Object> updateMap = new HashMap<>();
            HashMap<String, Object> queryMap = new HashMap<>();

            if ("prefix".equals(titlePlace)) {
                updateMap.put("common.fields.originalTitleCn", title + fields.getOriginalTitleCn());
            } else if ("suffix".equals(titlePlace)) {
                updateMap.put("common.fields.originalTitleCn", fields.getOriginalTitleCn() + title);
            } else {
                updateMap.put("common.fields.originalTitleCn", title);
            }

            bulkUpdateModels.add(createBulkUpdateModel(updateMap, queryMap));



            queryMap.put("channelId", channelId);
            queryMap.put("common.fields.code", fields.getCode());
//            productService.

            
        }




    }

    private BulkUpdateModel createBulkUpdateModel(HashMap<String, Object> updateMap, HashMap<String, Object> queryMap) {
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        return model;
    }
}
