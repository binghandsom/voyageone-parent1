package com.voyageone.service.impl.cms.product;

import com.voyageone.common.util.StringUtils;
import com.voyageone.service.bean.cms.product.ProductPriceBean;
import com.voyageone.service.bean.cms.product.ProductSkuPriceBean;
import com.voyageone.service.daoext.cms.CmsBtPriceLogDaoExt;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsBtPriceLogModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  Product PriceLog Service
 *
 * @author jeff.duan 16/4/19
 * @version 2.0.1
 * @since 2.0.0
 */
@Service
public class ProductPriceLogService extends BaseService {

    @Autowired
    private CmsBtPriceLogDaoExt cmsBtPriceLogDaoExt;


    /**
     * 价格履历更新
     * @param channelId 渠道id
     * @param productAfter ProductPrice Model更新后
     * @param productBefore ProductPrice Model更新前
     * @param comment 价格履历表中的注释字段
     * @param modifier 修改者
     */
    public void insertPriceLog(String channelId, ProductPriceBean productAfter, ProductPriceBean productBefore, String comment, String modifier) {

        if (productAfter == null || productBefore == null) {
            throw new RuntimeException("ProductPrice not found!");
        }

        if (productAfter.getProductId() == null || productAfter.getProductCode() == null) {
            throw new RuntimeException("ProductPrice ProductId and ProductCode not found!");
        }

        // 插入价格履历对象
        List<CmsBtPriceLogModel> logList = new ArrayList<>();

        if (productAfter.getSkuPrices() != null && !productAfter.getSkuPrices().isEmpty()) {
            // 循环原始sku列表
            for (ProductSkuPriceBean skuBefore : productBefore.getSkuPrices()) {
                // 循环变更sku列表
                for (ProductSkuPriceBean skuAfter : productAfter.getSkuPrices()) {
                    if (StringUtils.isEmpty(skuAfter.getSkuCode())) {
                        throw new RuntimeException("SkuPrices.SkuCode not found!");
                    }
                    if (skuAfter.getSkuCode().equals(skuBefore.getSkuCode())) {
                        // 判断价格是否发生变化
                        if (isPriceChanged(skuBefore, skuAfter)) {
                            CmsBtPriceLogModel cmsBtPriceLogModel = createPriceLogModel(channelId, productAfter.getProductId(), productAfter.getProductCode(), skuBefore, skuAfter, comment, modifier);
                            logList.add(cmsBtPriceLogModel);
                        }
                        break;
                    }
                }
            }
        }

        // 插入log履历
        if (!logList.isEmpty()) {
            cmsBtPriceLogDaoExt.insertCmsBtPriceLogList(logList);
        }
    }

    /**
     * 判断价格是否变更
     * @param skuBefore 变更前SKU信息
     * @param skuAfter 变更后SKU信息
     * @return true:有变更；false：没有变更
     */
    private boolean isPriceChanged(ProductSkuPriceBean skuBefore, ProductSkuPriceBean skuAfter) {
        BigDecimal clientPriceMsrpBefore = null;
        if (skuBefore.getClientMsrpPrice() != null) {
            clientPriceMsrpBefore = new BigDecimal(skuBefore.getClientMsrpPrice());
        }
        BigDecimal clientPriceRetailBefore = null;
        if (skuBefore.getClientRetailPrice() != null) {
            clientPriceRetailBefore = new BigDecimal(skuBefore.getClientRetailPrice());
        }
        BigDecimal clientPriceNetBefore = null;
        if (skuBefore.getClientNetPrice() != null) {
            clientPriceNetBefore = new BigDecimal(skuBefore.getClientNetPrice());
        }

        BigDecimal clientPriceMsrpAfter = null;
        if (skuAfter.getClientMsrpPrice() != null) {
            clientPriceMsrpAfter = new BigDecimal(skuAfter.getClientMsrpPrice());
        }
        BigDecimal clientPriceRetailAfter = null;
        if (skuAfter.getClientRetailPrice() != null) {
            clientPriceRetailAfter = new BigDecimal(skuAfter.getClientRetailPrice());
        }
        BigDecimal clientPriceNetAfter = null;
        if (skuAfter.getClientNetPrice() != null) {
            clientPriceNetAfter = new BigDecimal(skuAfter.getClientNetPrice());
        }

        BigDecimal priceMsrpBefore = null;
        if (skuBefore.getPriceMsrp() != null) {
            priceMsrpBefore = new BigDecimal(skuBefore.getPriceMsrp());
        }
        BigDecimal priceRetailBefore = null;
        if (skuBefore.getPriceRetail() != null) {
            priceRetailBefore = new BigDecimal(skuBefore.getPriceRetail());
        }
        BigDecimal priceSaleBefore = null;
        if (skuBefore.getPriceSale() != null) {
            priceSaleBefore = new BigDecimal(skuBefore.getPriceSale());
        }

        BigDecimal priceMsrpAfter = null;
        if (skuAfter.getPriceMsrp() != null) {
            priceMsrpAfter = new BigDecimal(skuAfter.getPriceMsrp());
        }
        BigDecimal priceRetailAfter = null;
        if (skuAfter.getPriceRetail() != null) {
            priceRetailAfter = new BigDecimal(skuAfter.getPriceRetail());
        }
        BigDecimal priceSaleAfter = null;
        if (skuAfter.getPriceSale() != null) {
            priceSaleAfter = new BigDecimal(skuAfter.getPriceSale());
        }

        // true:变更前=变更后（没有变更）；false：变更前<>变更后（变更了）
        boolean clientPriceMsrpFlg = true;
        boolean clientPriceRetailFlg = true;
        boolean clientPriceNetFlg = true;
        boolean priceMsrpFlg = true;
        boolean priceRetailFlg = true;
        boolean priceSaleFlg = true;

        // 变更后和变更前都存在的情况下进行比较，
        // 变更后值存在,变更前值不存在的情况下，认为变更
        if (clientPriceMsrpBefore != null && clientPriceMsrpAfter != null) {
            if (clientPriceMsrpBefore.compareTo(clientPriceMsrpAfter) != 0) {
                clientPriceMsrpFlg = false;
            }
        } else if (clientPriceMsrpAfter != null) {
            clientPriceMsrpFlg = false;
        }

        if (clientPriceRetailBefore != null && clientPriceRetailAfter != null) {
            if (clientPriceRetailBefore.compareTo(clientPriceRetailAfter) != 0) {
                clientPriceRetailFlg = false;
            }
        } else if (clientPriceRetailAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            clientPriceRetailFlg = false;
        }

        if (clientPriceNetBefore != null && clientPriceNetAfter != null) {
            if (clientPriceNetBefore.compareTo(clientPriceNetAfter) != 0) {
                clientPriceNetFlg = false;
            }
        } else if (clientPriceNetAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            clientPriceNetFlg = false;
        }

        if (priceMsrpBefore != null && priceMsrpAfter != null) {
            if (priceMsrpBefore.compareTo(priceMsrpAfter) != 0) {
                priceMsrpFlg = false;
            }
        } else if (priceMsrpAfter != null) {
            priceMsrpFlg = false;
        }

        if (priceRetailBefore != null && priceRetailAfter != null) {
            if (priceRetailBefore.compareTo(priceRetailAfter) != 0) {
                priceRetailFlg = false;
            }
        } else if (priceRetailAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            priceRetailFlg = false;
        }

        if (priceSaleBefore != null && priceSaleAfter != null) {
            if (priceSaleBefore.compareTo(priceSaleAfter) != 0) {
                priceSaleFlg = false;
            }
        } else if (priceSaleAfter != null) {
            // 变更后值存在,变更前值不存在的情况下，认为变更
            priceSaleFlg = false;
        }

        return !(clientPriceMsrpFlg && clientPriceRetailFlg && clientPriceNetFlg && priceMsrpFlg && priceRetailFlg && priceSaleFlg);
    }

    /**
     * 创建 SKU PriceLog Model
     * @param channelId 渠道id
     * @param productId 商品id
     * @param code 商品Code
     * @param skuBefore ProductPrice and SKUPrice Model更新前
     * @param skuAfter ProductPrice and SKUPrice Model更新后
     * @param comment 价格履历表中的注释字段
     * @param modifier 修改者
     * @return SKU PriceLog Model对象
     */
    private CmsBtPriceLogModel createPriceLogModel(String channelId, Long productId, String code , ProductSkuPriceBean skuBefore , ProductSkuPriceBean skuAfter, String comment, String modifier) {
        CmsBtPriceLogModel cmsBtPriceLogModel = new CmsBtPriceLogModel();
        cmsBtPriceLogModel.setChannelId(channelId);
        cmsBtPriceLogModel.setProductId(productId.intValue());
        cmsBtPriceLogModel.setCode(code);
        cmsBtPriceLogModel.setSku(skuAfter.getSkuCode());

        Double priceMsrp  = skuBefore.getPriceMsrp();
        if (skuAfter.getPriceMsrp() != null) {
            priceMsrp = skuAfter.getPriceMsrp();
        }
        cmsBtPriceLogModel.setMsrpPrice(priceMsrp.toString());

        Double priceRetail  = skuBefore.getPriceRetail();
        if (skuAfter.getPriceRetail() != null) {
            priceRetail = skuAfter.getPriceRetail();
        }
        cmsBtPriceLogModel.setRetailPrice(priceRetail.toString());

        Double priceSale  = skuBefore.getPriceSale();
        if (skuAfter.getPriceSale() != null) {
            priceSale = skuAfter.getPriceSale();
        }
        cmsBtPriceLogModel.setSalePrice(priceSale.toString());

        Double clientMsrpPrice  = skuBefore.getClientMsrpPrice();
        if (skuAfter.getClientMsrpPrice() != null) {
            clientMsrpPrice = skuAfter.getClientMsrpPrice();
        }
        cmsBtPriceLogModel.setClientMsrpPrice(clientMsrpPrice.toString());

        Double clientRetailPrice = skuBefore.getClientRetailPrice();
        if (skuAfter.getClientRetailPrice() != null) {
            clientRetailPrice = skuAfter.getClientRetailPrice();
        }
        cmsBtPriceLogModel.setClientRetailPrice(clientRetailPrice.toString());

        Double clientNetPriceSale  = skuBefore.getClientNetPrice();
        if (skuAfter.getClientNetPrice() != null) {
            clientNetPriceSale = skuAfter.getClientNetPrice();
        }
        cmsBtPriceLogModel.setClientNetPrice(clientNetPriceSale.toString());

        cmsBtPriceLogModel.setComment(comment);
        cmsBtPriceLogModel.setCreater(modifier);
        return cmsBtPriceLogModel;
    }
}
