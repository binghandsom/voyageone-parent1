package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoQuery;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mongodb.model.BulkUpdateModel;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
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

    @Autowired
    PlatformProductUploadService platformProductUploadService;

    @Override
    public void onStartup(CmsBtProductUpdatePriceMQMessageBody messageBody) throws Exception {
        if (messageBody != null) {
            String sender = messageBody.getSender();
            String channelId = messageBody.getChannelId();
            Map<String, Object> params = messageBody.getParams();
            String changedPriceType = (String) params.get("changedPriceType");
            //设置最大最小值的类型
            String minMaxChangedPriceType = null;
            if ("clientMsrpPrice".equals(changedPriceType)){
                minMaxChangedPriceType = "pPriceMsrp";
            }else {
                minMaxChangedPriceType = "pPriceRetail";
            }

            String basePriceType = (String) params.get("basePriceType");
            String optionType = (String) params.get("optionType");
            Double value = Double.parseDouble((String) params.get("value"));
            //"1":取整,"0":不取整
            String flag = (String) params.get("flag");
            List<String> productCodes = messageBody.getProductCodes();
            if (ListUtils.notNull(productCodes)) {
                super.count = productCodes.size();
                Integer cartId = messageBody.getCartId();
                for (String productCode : productCodes) {
                    CmsBtProductModel cmsBtProductModel = productService.getProductByCode(messageBody.getChannelId(), productCode);
                    if (cmsBtProductModel != null) {
                        Map<String, CmsBtProductModel_Platform_Cart> usPlatforms = cmsBtProductModel.getUsPlatforms();
                        if (cartId > 0) {
                            CmsBtProductModel_Platform_Cart platform = usPlatforms.get("P" + cartId);
                            if (platform != null) {
                                String status = platform.getStatus();
                                List<BaseMongoMap<String, Object>> skus = platform.getSkus();
                                if (skus != null) {
                                    for (BaseMongoMap<String, Object> sku : skus) {
                                        //获取到对应的skuCode
                                        String skuCode = (String) sku.get("skuCode");
                                        Double newPrice = null;
                                        Double basePrice = null;
                                        if ("clientMsrpPrice".equals(basePriceType) || "clientRetailPrice".equals(basePriceType)) {
                                            basePrice = (Double) sku.get(basePriceType);
                                            if ("*".equals(optionType)) {
                                                newPrice = basePrice * value;
                                            }
                                            if ("/".equals(optionType)) {
                                                newPrice = basePrice / value;
                                            }
                                            if ("+".equals(optionType)) {
                                                newPrice = basePrice + value;
                                            }
                                            if ("-".equals(optionType)) {
                                                newPrice = basePrice - value;
                                            }
                                        } else {
                                            //固定值类型
                                            newPrice = value;
                                        }
                                        if (flag == "1") {
                                            //将价格取整
                                            Math.round(newPrice);
                                        }
                                        JongoUpdate jongoUpdate = new JongoUpdate();
                                        jongoUpdate.setQuery("{\"usPlatforms.P" + cartId + ".skus.skuCode\":#}");
                                        jongoUpdate.setQueryParameters(skuCode);
                                        jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + ".skus.$." + changedPriceType + "\":#}}");
                                        jongoUpdate.setUpdateParameters(newPrice);
                                        cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
                                        if ("Approve".equals(status)) {
                                            platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, sender);
                                        }
                                    }
                                }
                                //修改对应的最大值最小值
                                Double stPrice = null;
                                Double edPrice = null;
                                if ("clientMsrpPrice".equals(changedPriceType)){
                                    stPrice = platform.getpPriceMsrpSt();
                                    edPrice = platform.getpPriceMsrpEd();

                                }else {
                                    stPrice = platform.getpPriceSaleSt();
                                    edPrice = platform.getpPriceSaleEd();
                                }
                                Double newStPrice = null;
                                Double newEdPrice = null;

                                if ("clientMsrpPrice".equals(basePriceType) || "clientRetailPrice".equals(basePriceType)) {
                                    if ("*".equals(optionType)) {
                                        newStPrice = stPrice * value;
                                        newEdPrice = edPrice * value;
                                    }
                                    if ("/".equals(optionType)) {
                                        newStPrice = stPrice / value;
                                        newEdPrice = edPrice / value;
                                    }
                                    if ("+".equals(optionType)) {
                                        newStPrice = stPrice + value;
                                        newEdPrice = edPrice + value;
                                    }
                                    if ("-".equals(optionType)) {
                                        newStPrice = stPrice - value;
                                        newEdPrice = edPrice - value;
                                    }
                                } else {
                                    //固定值类型
                                    newStPrice = value;
                                    newEdPrice = value;
                                }
                                if (flag == "1") {
                                    //将价格取整
                                    Math.round(newStPrice);
                                    Math.round(newEdPrice);
                                }

                                JongoUpdate jongoUpdate = new JongoUpdate();
                                //通过code定位
                                jongoUpdate.setQuery("{\"common.fields.code\":#}");
                                jongoUpdate.setQueryParameters(productCode);
                                jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "St\":#,\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "Ed\":#}}");
                                jongoUpdate.setUpdateParameters(newStPrice,newEdPrice);
                                cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));

                            }
                        } else {
                            //cartId=0,修改所有平台的价格
                            usPlatforms.forEach((cartId1, platform) -> {
                                Integer cartId2 = Integer.parseInt(cartId1.replace("P",""));
                                String status = platform.getStatus();
                                if (platform != null) {
                                    List<BaseMongoMap<String, Object>> skus = platform.getSkus();
                                    if (skus != null) {
                                        for (BaseMongoMap<String, Object> sku : skus) {
                                            //获取到对应的skuCode
                                            String skuCode = (String) sku.get("skuCode");
                                            Double newPrice = null;
                                            Double basePrice = null;
                                            if ("clientMsrpPrice".equals(basePriceType) || "clientRetailPrice".equals(basePriceType)) {
                                                basePrice = (Double) sku.get(basePriceType);
                                                if ("*".equals(optionType)) {
                                                    newPrice = basePrice * value;
                                                }
                                                if ("/".equals(optionType)) {
                                                    newPrice = basePrice / value;
                                                }
                                                if ("+".equals(optionType)) {
                                                    newPrice = basePrice + value;
                                                }
                                                if ("-".equals(optionType)) {
                                                    newPrice = basePrice - value;
                                                }
                                            } else {
                                                //固定值类型
                                                newPrice = value;
                                            }
                                            if (flag == "1") {
                                                //将价格取整
                                                Math.round(newPrice);
                                            }
                                            JongoUpdate jongoUpdate = new JongoUpdate();
                                            jongoUpdate.setQuery("{\"usPlatforms.P" + cartId2 + ".skus.skuCode\":#}");
                                            jongoUpdate.setQueryParameters(skuCode);
                                            jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId2 + ".skus.$." + changedPriceType + "\":#}}");
                                            jongoUpdate.setUpdateParameters(newPrice);
                                            cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
                                            if ("Approve".equals(status)) {
                                                platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId2, productCode, null, 0, sender);
                                            }
                                        }
                                    }

                                    //修改对应的最大值最小值
                                    Double stPrice = null;
                                    Double edPrice = null;
                                    if ("clientMsrpPrice".equals(changedPriceType)){
                                        stPrice = platform.getpPriceMsrpSt();
                                        edPrice = platform.getpPriceMsrpEd();

                                    }else {
                                        stPrice = platform.getpPriceSaleSt();
                                        edPrice = platform.getpPriceSaleEd();
                                    }
                                    Double newStPrice = null;
                                    Double newEdPrice = null;

                                    if ("clientMsrpPrice".equals(basePriceType) || "clientRetailPrice".equals(basePriceType)) {
                                        if ("*".equals(optionType)) {
                                            newStPrice = stPrice * value;
                                            newEdPrice = edPrice * value;
                                        }
                                        if ("/".equals(optionType)) {
                                            newStPrice = stPrice / value;
                                            newEdPrice = edPrice / value;
                                        }
                                        if ("+".equals(optionType)) {
                                            newStPrice = stPrice + value;
                                            newEdPrice = edPrice + value;
                                        }
                                        if ("-".equals(optionType)) {
                                            newStPrice = stPrice - value;
                                            newEdPrice = edPrice - value;
                                        }
                                    } else {
                                        //固定值类型
                                        newStPrice = value;
                                        newEdPrice = value;
                                    }
                                    if (flag == "1") {
                                        //将价格取整
                                        Math.round(newStPrice);
                                        Math.round(newEdPrice);
                                    }
                                    String changedPriceType1 = (String) params.get("changedPriceType");
                                    //设置最大最小值的类型
                                    String minMaxChangedPriceType1 = null;
                                    if ("clientMsrpPrice".equals(changedPriceType)){
                                        minMaxChangedPriceType1 = "pPriceMsrp";
                                    }else {
                                        minMaxChangedPriceType1 = "pPriceRetail";
                                    }
                                    JongoUpdate jongoUpdate = new JongoUpdate();
                                    //通过code定位
                                    jongoUpdate.setQuery("{\"common.fields.code\":#}");
                                    jongoUpdate.setQueryParameters(productCode);
                                    jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId2 + "." + minMaxChangedPriceType1 + "St\":#,\"usPlatforms.P" + cartId2 + "." + minMaxChangedPriceType1 + "Ed\":#}}");
                                    jongoUpdate.setUpdateParameters(newStPrice,newEdPrice);
                                    cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));

                                }
                            });
                        }

                    }
                }
            }
        }
    }
}

