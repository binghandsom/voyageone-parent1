package com.voyageone.task2.cms.mqjob.usa;

import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.CmsPlatformCategoryUpdateMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdateCategoryMQMessageBody;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_UsPlatform_Cart;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
            String channelId = messageBody.getChannelId();
            Map<String, Object> params = messageBody.getParams();
            String changedPriceType = (String) params.get("changedPriceType");
            String basePriceType = (String) params.get("basePriceType");
            String optionType = (String) params.get("optionType");
            Double value = Double.parseDouble((String)params.get("value"));
            //"1":取整,"0":不取整
            String flag = (String) params.get("flag");
            List<String> productCodes = messageBody.getProductCodes();
            if (ListUtils.notNull(productCodes)){
                super.count = productCodes.size();
                Integer cartId = messageBody.getCartId();
                for (String productCode : productCodes) {
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
                    if (cmsBtProductModel != null){
                        Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                        CmsBtProductModel_Platform_Cart platform = usPlatforms.get("P" + cartId);
                        if (platform != null){
                            List<BaseMongoMap<String, Object>> skus = platform.getSkus();
                            if (skus != null){
                                for (BaseMongoMap<String, Object> sku : skus) {
                                    //获取到对应的skuCode
                                    String skuCode = (String) sku.get("skuCode");
                                    Double newPrice = null;
                                    Double basePrice = null;
                                    if ("clientMsrpPrice".equals(basePriceType)||"clientRetailPrice".equals(basePriceType)){
                                        basePrice = (Double) sku.get(basePriceType);
                                        if ("*".equals(optionType)){
                                            newPrice = basePrice * value;
                                        }
                                        if ("/".equals(optionType)){
                                            newPrice = basePrice / value;
                                        }
                                        if ("+".equals(optionType)){
                                            newPrice = basePrice + value;
                                        }
                                        if ("-".equals(optionType)){
                                            newPrice = basePrice - value;
                                        }
                                    }else {
                                        //固定值类型
                                        newPrice = value;
                                    }
                                    if (flag == "1"){
                                        //将价格取整
                                        Math.round(newPrice);
                                    }
                                    JongoUpdate jongoUpdate = new JongoUpdate();
                                    jongoUpdate.setQuery("{\"usPlatforms.P"+ cartId +".skus.skuCode\":#}");
                                    jongoUpdate.setQueryParameters(skuCode);
                                    jongoUpdate.setUpdate("{$set:{\"usPlatforms.P"+ cartId +".skus.$."+ changedPriceType+"\":#}}");
                                    jongoUpdate.setUpdateParameters(newPrice);
                                    cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    }

