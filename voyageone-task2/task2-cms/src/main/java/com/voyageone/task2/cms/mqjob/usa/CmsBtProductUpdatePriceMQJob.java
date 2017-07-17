package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_UsPlatform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/7/17.
 */
@Service
@RabbitListener()
public class CmsBtProductUpdatePriceMQJob extends TBaseMQCmsService<CmsBtProductUpdatePriceMQMessageBody> {



    @Autowired
    ProductService productService;

    @Autowired
    CmsBtProductDao cmsBtProductDao;

    @Override
    public void onStartup(CmsBtProductUpdatePriceMQMessageBody messageBody) throws Exception {
        if (messageBody != null){
            Map<String, Object> params = messageBody.getParams();
            String changedPriceType = (String) params.get("changedPriceType");
            String basePriceType = (String) params.get("basePriceType");
            String optionType = (String) params.get("optionType");
            String value = (String) params.get("value");

            List<String> productCodes = messageBody.getProductCodes();
            if (ListUtils.notNull(productCodes)){
                super.count = productCodes.size();
                Integer cartId = messageBody.getCartId();
                for (String productCode : productCodes) {
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
                    if (cmsBtProductModel != null){
                        CmsBtProductModel_Platform_Cart platform = cmsBtProductModel.getPlatform(cartId);
                        if (platform != null){
                            List<BaseMongoMap<String, Object>> skus = platform.getSkus();
                            if (skus != null){
                                for (BaseMongoMap<String, Object> sku : skus) {
                                    //获取到对应的skuCode
                                    String skuCode = (String) sku.get("skuCode");
                                    Double newPrice = null;
                                    Double basePrice = (Double) sku.get(basePriceType);
                                    Double value1 = (Double) sku.get(value);
                                    if ("*".equals(optionType)){
                                        newPrice = basePrice * value1;
                                    }
                                    if ("/".equals(optionType)){
                                        newPrice = basePrice / value1;
                                    }
                                    if ("+".equals(optionType)){
                                        newPrice = basePrice + value1;
                                    }
                                    if ("-".equals(optionType)){
                                        newPrice = basePrice - value1;
                                    }
                                    JongoUpdate jongoUpdate = new JongoUpdate();
                                    jongoUpdate.setQuery("{\"platforms.P23.skus.skuCode\":#}");
                                    jongoUpdate.setQueryParameters(skuCode.toLowerCase());
                                    jongoUpdate.setUpdate("{$set:{\"skus.$.qty\":#}}");
                                    //jongoUpdate.setUpdateParameters(skuTotal);


                                }
                            }

                        }
                    }
                }
            }
        }


      /*  for (String productCode : productCodes) {
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
        }*/
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

