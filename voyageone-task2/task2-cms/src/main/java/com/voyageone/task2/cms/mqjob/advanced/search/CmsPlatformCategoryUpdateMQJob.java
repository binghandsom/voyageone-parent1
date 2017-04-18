package com.voyageone.task2.cms.mqjob.advanced.search;

import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.components.rabbitmq.annotation.VOSubRabbitListener;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
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

    private final ProductService productService;

    private final SxProductService sxProductService;

    @Autowired
    public CmsPlatformCategoryUpdateMQJob(SxProductService sxProductService, ProductService productService) {
        this.sxProductService = sxProductService;
        this.productService = productService;
    }

    @Override
    public void onStartup(CmsPlatformCategoryUpdateMQMessageBody messageBody) throws Exception {

        List<String> productCodes = messageBody.getProductCodes();
        super.count = productCodes.size();
        Integer cartId = messageBody.getCartId();

        for (String productCode : productCodes) {
            CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
            if(cmsBtProductModel != null && cmsBtProductModel.getPlatform(cartId) != null) {
                CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(cartId);
                if (!messageBody.getpCatId().equalsIgnoreCase(platform.getpCatId()) || !messageBody.getpCatPath().equalsIgnoreCase(platform.getpCatPath())) {
                    if (CartEnums.Cart.TT.getValue() != cartId
                            && CartEnums.Cart.LTT.getValue() != cartId
                            && CartEnums.Cart.JM.getValue() != cartId) {
                        if (!"Approved".equalsIgnoreCase(platform.getStatus())) {
                            update(productCode, messageBody);
                        }
                    } else {
                        update(productCode, messageBody);
                        if("Approved".equalsIgnoreCase(platform.getStatus())){
                            sxProductService.insertSxWorkLoad(messageBody.getChannelId(), productCode, cartId, getTaskName());
                        }
                    }
                }
            }
        }
    }

    private void update(String productCode, CmsPlatformCategoryUpdateMQMessageBody messageBody){
        Integer cartId = messageBody.getCartId();
        List<BulkUpdateModel> bulkList = new ArrayList<>(1);
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("platforms.P" + cartId + ".pCatPath", messageBody.getpCatPath());
        updateMap.put("platforms.P" + cartId + ".pCatId", messageBody.getpCatId());
        updateMap.put("platforms.P" + cartId + ".pCatStatus", 1);
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("common.fields.code", productCode);
        BulkUpdateModel model = new BulkUpdateModel();
        model.setUpdateMap(updateMap);
        model.setQueryMap(queryMap);
        bulkList.add(model);
        productService.bulkUpdateWithMap(messageBody.getChannelId(), bulkList, messageBody.getSender(), "$set");
    }
}
