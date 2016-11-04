package com.voyageone.task2.cms.service;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.DateTimeUtil;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.com.mq.config.MqRoutingKey;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.task2.base.BaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by james on 2016/11/4.
 */
@Service
@RabbitListener(queues = MqRoutingKey.CMS_BATCH_PlatformFieldsTaskJob)
public class CmsBatchEditPlatformFieldsMqService extends BaseMQCmsService {

    @Autowired
    private ProductService productService;

    @Autowired
    private CmsBtProductDao cmsBtProductDao;

    @Autowired
    private SxProductService sxProductService;

    @Override
    protected void onStartup(Map<String, Object> messageMap) throws Exception {
        Map<String, Object> mqMessage = new HashedMap();

        List<String> productCodes = (List<String>) messageMap.get("productCodes");
        String channelId = (String) messageMap.get("channelId");
        Integer cartId = (Integer) messageMap.get("cartId");
        String fieldsId = (String) messageMap.get("fieldsId");
        Object fieldsValue = messageMap.get("fieldsValue");
        String userName = (String) messageMap.get("userName");
        productCodes.forEach(code -> {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(channelId, code);
            if(cmsBtProductModel != null && cmsBtProductModel.getPlatform(cartId) != null){
                CmsBtProductModel_Platform_Cart cmsBtProductModel_platform_cart = cmsBtProductModel.getPlatform(cartId);
                cmsBtProductModel_platform_cart.getFields().setAttribute(fieldsId, fieldsValue);

                HashMap<String, Object> queryMap = new HashMap<>();
                queryMap.put("common.fields.code", code);

                List<BulkUpdateModel> bulkList = new ArrayList<>();
                HashMap<String, Object> updateMap = new HashMap<>();
                updateMap.put("platforms.P" + cartId +".fields." + fieldsId, fieldsValue);
                BulkUpdateModel model = new BulkUpdateModel();
                model.setUpdateMap(updateMap);
                model.setQueryMap(queryMap);
                bulkList.add(model);
                cmsBtProductDao.bulkUpdateWithMap(channelId, bulkList, userName, "$set");

                if (CmsConstants.ProductStatus.Approved.toString().equalsIgnoreCase(cmsBtProductModel_platform_cart.getStatus())) {
                    sxProductService.insertSxWorkLoad(channelId, new ArrayList<String>(Arrays.asList(code)), cartId, userName);
                }
                productService.insertProductHistory(channelId, cmsBtProductModel);
            }
        });
    }
}
