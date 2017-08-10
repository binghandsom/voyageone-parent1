package com.voyageone.task2.cms.mqjob.usa;

import com.mongodb.BulkWriteResult;
import com.voyageone.base.dao.mongodb.JongoUpdate;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.ListUtils;
import com.voyageone.service.dao.cms.mongo.CmsBtProductDao;
import com.voyageone.service.impl.cms.PlatformProductUploadService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.usa.CmsBtProductUpdatePriceMQMessageBody;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
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
        $info("接收到批量修改价格消息体,messageBody:" + JacksonUtil.bean2Json(messageBody));
        if (messageBody != null) {
            String sender = messageBody.getSender();
            String channelId = messageBody.getChannelId();
            Map<String, Object> params = messageBody.getParams();
            String changedPriceType = (String) params.get("changedPriceType");
            //设置最大最小值的类型
            String minMaxChangedPriceType = null;
            if ("clientMsrpPrice".equals(changedPriceType)) {
                minMaxChangedPriceType = "pPriceMsrp";
            } else {
                minMaxChangedPriceType = "pPriceSale";
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
                                update(sender, channelId, changedPriceType, minMaxChangedPriceType, basePriceType, optionType, value, flag, cartId, productCode, platform);
                            }
                        } else {
                            //cartId=0,修改所有平台的价格
                            usPlatforms.forEach((cartId1, platform) -> {
                                Integer cartId2 = Integer.parseInt(cartId1.replace("P", ""));
                                //设置最大最小值的类型
                                String minMaxChangedPriceType1 = null;
                                if ("clientMsrpPrice".equals(changedPriceType)) {
                                    minMaxChangedPriceType1 = "pPriceMsrp";
                                } else {
                                    minMaxChangedPriceType1 = "pPriceSale";
                                }
                                update(sender, channelId, changedPriceType, minMaxChangedPriceType1, basePriceType, optionType, value, flag, cartId2, productCode, platform);
                            });
                        }

                    }
                }
            }
        }
    }

    private void update(String sender, String channelId, String changedPriceType, String minMaxChangedPriceType, String basePriceType, String optionType, Double value, String flag, Integer cartId, String productCode, CmsBtProductModel_Platform_Cart platform) {
        String status = platform.getStatus();
        List<BaseMongoMap<String, Object>> skus = platform.getSkus();

        if (skus != null) {
            Double[] minMaxPrices = new Double[skus.size()];
            int i = 0;
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
                if ("1".equals(flag)) {
                    //将价格取整
                    Long round = Math.round(newPrice);
                    newPrice = round.doubleValue();
                }

                minMaxPrices[i] = newPrice;
                JongoUpdate jongoUpdate = new JongoUpdate();
                jongoUpdate.setQuery("{\"usPlatforms.P" + cartId + ".skus.skuCode\":#}");
                jongoUpdate.setQueryParameters(skuCode);
                //判断修改的字段
                if ("clientMsrpPrice".equals(changedPriceType)){
                    jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + ".skus.$." + changedPriceType + "\":#}}");
                    jongoUpdate.setUpdateParameters(newPrice);
                }else {
                    //这里需要修改两个价格clientNetPrice,clientRetailPrice
                    jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + ".skus.$." + changedPriceType + "\":#,\"usPlatforms.P" + cartId + ".skus.$.clientNetPrice\":#}}");
                    jongoUpdate.setUpdateParameters(newPrice,newPrice);
                }
                BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));

                $info("修改sku价格,channelId:" + channelId  +" skuCode:" + skuCode + " bulkWriteResult:" + bulkWriteResult);
                if (CmsConstants.ProductStatus.Approved.name().equals(platform.getStatus()) && (CmsConstants.PlatformStatus.OnSale == platform.getpStatus() || CmsConstants.PlatformStatus.InStock == platform.getpStatus() )) {
                    platformProductUploadService.saveCmsBtUsWorkloadModel(channelId, cartId, productCode, null, 0, sender);
                }
                i++;
            }
            Double newStPrice = null;
            Double newEdPrice = null;
            Arrays.sort(minMaxPrices);
            if (minMaxPrices.length != 0) {
                newStPrice = minMaxPrices[0];
                newEdPrice = minMaxPrices[minMaxPrices.length - 1];
            }
            JongoUpdate jongoUpdate = new JongoUpdate();
            //通过code定位
            jongoUpdate.setQuery("{\"common.fields.code\":#}");
            jongoUpdate.setQueryParameters(productCode);
            if ("pPriceSale".equals(minMaxChangedPriceType)){
                jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "St\":#,\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "Ed\":#,\"usPlatforms.P" + cartId + "." + "pPriceRetail" + "St\":#,\"usPlatforms.P" + cartId + "." + "pPriceRetail" + "Ed\":#}}");
            }else {
                jongoUpdate.setUpdate("{$set:{\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "St\":#,\"usPlatforms.P" + cartId + "." + minMaxChangedPriceType + "Ed\":#}}");
            }
            jongoUpdate.setUpdateParameters(newStPrice, newEdPrice);
            BulkWriteResult bulkWriteResult = cmsBtProductDao.bulkUpdateWithJongo(channelId, Collections.singletonList(jongoUpdate));
            $info("同步价格最大值最小值,channelId:" + channelId  +" productCode:" + productCode + " bulkWriteResult:" + bulkWriteResult);
        }
    }

}

