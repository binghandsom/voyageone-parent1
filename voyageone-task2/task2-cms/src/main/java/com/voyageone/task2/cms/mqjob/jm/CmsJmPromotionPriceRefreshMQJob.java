package com.voyageone.task2.cms.mqjob.jm;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.idsnowflake.FactoryIdWorker;
import com.voyageone.service.impl.cms.jumei.CmsBtJmPromotionSkuService;
import com.voyageone.service.impl.cms.product.ProductService;
import com.voyageone.service.impl.cms.vomq.vomessage.body.jm.JMRefreshPriceMQMessageBody;
import com.voyageone.service.model.cms.CmsBtJmPromotionSkuModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import com.voyageone.task2.cms.mqjob.TBaseMQCmsService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * JmBtPromotion PriceRefresh Service  聚美活动 参考价刷新
 *
 * @author peitao 2017/01/03.
 * @version 2.0.0
 * @since 2.0.0
 */
@Service
@RabbitListener()//queues = CmsMqRoutingKey.CMS_BATCH_JmSynPromotionDealPrice
public class CmsJmPromotionPriceRefreshMQJob extends TBaseMQCmsService<JMRefreshPriceMQMessageBody> {

    @Autowired
    private CmsBtJmPromotionSkuService cmsBtJmPromotionSkuService;

    @Autowired
    private ProductService productService;

    @Override
    public void onStartup(JMRefreshPriceMQMessageBody messageBody) {
        Integer jmPromotionId = messageBody.getCmsBtJmPromotionId();

        Map<String, Object> param = new HashedMap();
        param.put("cmsBtJmPromotionId", jmPromotionId);
        List<CmsBtJmPromotionSkuModel> skus = cmsBtJmPromotionSkuService.selectList(param);
        skus.sort((o1, o2) -> o1.getProductCode().compareToIgnoreCase(o2.getProductCode()));
        StringBuilder sbError = new StringBuilder();//错误信息
        CmsBtProductModel cacheProduct = null;
        int errorCount = 0;
        for (CmsBtJmPromotionSkuModel sku : skus) {
            try {
                cacheProduct = updateJMPromotionSkuPrice(jmPromotionId, cacheProduct, sku);
            } catch (Exception ex) {
                errorCount++;
                long errorId = FactoryIdWorker.nextId();
                sbError.append("skuCode:").append(sku.getSkuCode()).append(":").append(ex.getMessage()).append("errorId:").append(errorId).append("\\r\\n");
                $error(sku.getSkuCode() + ":" + errorId, ex);
            }
        }

        if (sbError.length() > 0) {
            cmsSuccessIncludeFailLog(messageBody, String.format("skuCode总数(%s) 失败(%s) \\r\\n %s", skus.size(), errorCount, sbError.toString()));
        } else {
            cmsSuccessLog(messageBody, String.format("执行成功 skuCode总数(%s)", skus.size()));
        }
    }

    private CmsBtProductModel updateJMPromotionSkuPrice(Integer jmPromotionId, CmsBtProductModel cacheProduct, CmsBtJmPromotionSkuModel sku) {
        if (cacheProduct == null || sku.getProductCode().compareToIgnoreCase(cacheProduct.getCommon().getFields().getCode()) != 0) {
            cacheProduct = productService.getProductByCode(sku.getChannelId(), sku.getProductCode());
        }
        if (cacheProduct == null || cacheProduct.getPlatform(CartEnums.Cart.JM) == null) return cacheProduct;

        BaseMongoMap<String, Object> productSku = cacheProduct.getPlatform(CartEnums.Cart.JM).getSkus()
                .stream().filter(pSku -> sku.getSkuCode().equalsIgnoreCase(pSku.getStringAttribute("skuCode")))
                .findFirst().orElse(null);


        CmsBtProductModel_Sku cmsBtProductModelSku = cacheProduct.getCommon().getSkus().stream().filter(cmsBtProductModel_sku -> sku.getSkuCode().equalsIgnoreCase(cmsBtProductModel_sku.getSkuCode()))
                .findFirst().orElse(null);
        boolean editFlg = false;
        if (productSku != null) {
            if (sku.getMsrpRmb().doubleValue() != productSku.getDoubleAttribute("priceMsrp")) {
                sku.setMsrpRmb(new BigDecimal(productSku.getDoubleAttribute("priceMsrp")));
                editFlg = true;
            }
            if (sku.getRetailPrice().doubleValue() != productSku.getDoubleAttribute("priceRetail")) {
                sku.setRetailPrice(new BigDecimal(productSku.getDoubleAttribute("priceRetail")));
                editFlg = true;
            }
            if (sku.getSalePrice().doubleValue() != productSku.getDoubleAttribute("priceSale")) {
                sku.setSalePrice(new BigDecimal(productSku.getDoubleAttribute("priceSale")));
                editFlg = true;
            }
        }
        if (cmsBtProductModelSku != null) {
            if (sku.getMsrpUsd().doubleValue() != cmsBtProductModelSku.getClientMsrpPrice()) {
                sku.setMsrpUsd(new BigDecimal(cmsBtProductModelSku.getClientMsrpPrice()));
                editFlg = true;
            }
        }
        if (editFlg) {
            $debug(String.format("jmPromotionId:%d sku:%s 价格有变化 MsrpUsd:%s MsrpRmb:%s RetailPrice:%s SalePrice:%s", jmPromotionId, sku.getSkuCode(), sku.getMsrpUsd().doubleValue(), sku.getMsrpRmb().doubleValue(), sku.getRetailPrice().doubleValue(), sku.getSalePrice().doubleValue()));
            cmsBtJmPromotionSkuService.updateWithDiscount(sku, sku.getChannelId(), getTaskName());
        }
        return cacheProduct;
    }

}
