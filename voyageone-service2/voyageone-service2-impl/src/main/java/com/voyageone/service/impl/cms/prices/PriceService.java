package com.voyageone.service.impl.cms.prices;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by Ethan Shi on 2016/7/13.
 *
 * @author Ethan Shi
 * @since 2.3.0
 */

@Service
public class PriceService extends BaseService {

    private static final String HSCODE_TYPE_8_DIGIT = "8_DIGIT";
    private static final String HSCODE_TYPE_10_DIGIT = "10_DIGIT";

    private static final String HSCODE_TYPE = "HSCODE_TYPE";

    private final CmsMtFeeCommissionService feeCommissionService;

    private final CmsMtFeeExchangeService feeExchangeService;

    private final CmsMtFeeTaxService feeTaxService;

    private final CmsMtFeeShippingService feeShippingService;

    private final ProductSkuService productSkuService;

    @Autowired
    public PriceService(CmsMtFeeShippingService feeShippingService, CmsMtFeeTaxService feeTaxService,
                        CmsMtFeeCommissionService feeCommissionService, CmsMtFeeExchangeService feeExchangeService,
                        ProductSkuService productSkuService) {
        this.feeShippingService = feeShippingService;
        this.feeTaxService = feeTaxService;
        this.feeCommissionService = feeCommissionService;
        this.feeExchangeService = feeExchangeService;
        this.productSkuService = productSkuService;
    }

    /**
     * 计算 product 中所有平台的所有 sku 的 retailPrice 和 originPriceMsrp, 当打开部分配置时, 会同步 price sale 和 msrp
     * <p>
     * 计算所需的商品模型必须提供以下内容
     * <pre class="code">
     * {
     *   channelId: String,
     *   common: {
     *     fields: {
     *       commissionRate: Double,
     *       hsCodePrivate: String,
     *       hsCodeCross: String,
     *       code: String
     *     },
     *     skus: [
     *       {
     *         clientNetPrice: Double,
     *         clientMsrpPrice: Double,
     *         weight: Double,
     *         skuCode: String
     *       }
     *     ]
     *   },
     *   platforms: {
     *     P*: {
     *       pBrandId: String,
     *       pCateId: String,
     *       skus: [
     *         {
     *           skuCode: String
     *         }
     *       ]
     *     }
     *   }
     * }
     * </pre>
     *
     * @param product 包含计算所需参数的商品模型
     * @return 包含计算后价格的商品模型
     * @throws PriceCalculateException 当价格计算公式中, 参数无法正确获取时, 或计算结果不合法时, 抛出该错误
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product) throws PriceCalculateException {

        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> cartEntry : product.getPlatforms().entrySet()) {

            CmsBtProductModel_Platform_Cart cart = cartEntry.getValue();

            Integer cartId = cart.getCartId();

            // 对特定平台进行跳过
            // 不需要为这些平台计算价格
            if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                continue;

            setRetailPrice(product, cartId);
        }

        return product;
    }

    /**
     * 计算 product 中 cart 下的各个 sku 的 retailPrice 和 originPriceMsrp, 当打开部分配置时, 会同步 price sale 和 msrp
     * <p>
     * 计算所需的商品模型必须提供以下内容
     * <pre class="code">
     * {
     *   channelId: String,
     *   common: {
     *     fields: {
     *       commissionRate: Double,
     *       hsCodePrivate: String,
     *       hsCodeCross: String,
     *       code: String
     *     },
     *     skus: [
     *       {
     *         clientNetPrice: Double,
     *         clientMsrpPrice: Double,
     *         weight: Double,
     *         skuCode: String
     *       }
     *     ]
     *   },
     *   platforms: {
     *     P*: {
     *       pBrandId: String,
     *       pCateId: String,
     *       skus: [
     *         {
     *           skuCode: String
     *         }
     *       ]
     *     }
     *   }
     * }
     * </pre>
     *
     * @param product 包含计算所需参数的商品模型
     * @param cartId  平台 ID
     * @return 包含计算后价格的商品模型
     * @throws PriceCalculateException 当价格计算公式中, 参数无法正确获取时, 或计算结果不合法时, 抛出该错误
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product, Integer cartId) throws PriceCalculateException {

        // 公式参数: 其他费用
        final Double otherFee = 0.0d;

        final int JM_CART = 27;

        CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);

        // JM平台是按照品牌收取佣金
        // 所以这里要根据店铺来选择类目参数, 使用品牌还是类目
        String catId = cartId == JM_CART ? cart.getpBrandId() : cart.getpCatId();

        // 公式参数: 汇率
        Double exchangeRate = feeExchangeService.getExchangeRateForUsd();

        String channelId = product.getChannelId();

        Integer platformId = CartType.getPlatformIdById(cartId);

        String code = product.getCommon().getFields().getCode();

        // 计算是否向上取整

        boolean isRoundUp = true;

        CmsChannelConfigBean configBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_ROUND_UP_FLG);

        if (configBean != null && "0".equals(configBean.getConfigValue1())) {
            isRoundUp = false;
        }

        // 计算是否自动同步最终售价

        boolean isAutoApprovePrice = false;

        CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);

        if (autoApprovePrice != null && autoApprovePrice.getConfigValue1() != null && "1".equals(autoApprovePrice.getConfigValue1()))
            isAutoApprovePrice = true;

        // 计算是否计算 MSRP

        boolean isAutoSyncPriceMsrp = false;

        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP);

        if (autoSyncPriceMsrp != null && "1".equals(autoSyncPriceMsrp.getConfigValue1()))
            isAutoSyncPriceMsrp = true;

        // 计算发货方式

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean shippingTypeConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE, String.valueOf(cartId));

        if (shippingTypeConfig == null) {
            shippingTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE);
        }

        String shippingType;

        if (shippingTypeConfig == null || StringUtils.isEmpty(shippingType = shippingTypeConfig.getConfigValue1()))
            throw new PriceCalculateException("没有找到渠道 %s (%s) 可用的发货方式", channelId, cartId);

        CmsMtFeeCommissionService.CommissionQueryBuilder commissionQueryBuilder = feeCommissionService.new CommissionQueryBuilder()
                .withChannel(channelId)
                .withPlatform(platformId)
                .withCart(cartId)
                .withCategory(catId);

        // 公式参数: 退货率
        Double returnRate = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_RETURN);

        // 公式参数: 公司佣金比例
        Double voyageOneCommission = product.getCommon().getFields().getCommissionRate();

        if (voyageOneCommission == null || voyageOneCommission <= 0)
            voyageOneCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_VOYAGE_ONE);

        // 公式参数: 平台佣金比例
        Double platformCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_PLATFORM);

        String hsCodeType = Codes.getCodeName(HSCODE_TYPE, shippingType);

        if (StringUtils.isEmpty(hsCodeType))
            throw new PriceCalculateException("%s 发货方式的税号类型没有配置", shippingType);

        String hsCode = null;

        switch (hsCodeType) {
            case HSCODE_TYPE_8_DIGIT:
                hsCode = product.getCommon().getFields().getHsCodePrivate();
                break;
            case HSCODE_TYPE_10_DIGIT:
                hsCode = product.getCommon().getFields().getHsCodeCross();
                break;
        }

        if (StringUtils.isEmpty(hsCode))
            throw new PriceCalculateException("税号为空: 税号类型: %s, 商品 Code: %s", hsCodeType, code);

        // 税号
        hsCode = hsCode.split(",")[0];

        // 公式参数: 税率
        Double taxRate = feeTaxService.getTaxRate(shippingType, hsCode);

        // 进入计算阶段
        PriceCalculator priceCalculator = new PriceCalculator()
                .setRoundUp(isRoundUp)
                .setTaxRate(taxRate)
                .setPfCommission(platformCommission)
                .setReturnRate(returnRate)
                .setVoCommission(voyageOneCommission)
                .setExchangeRate(exchangeRate)
                .setOtherFee(otherFee);

        // 对设置到价格计算器上的参数
        // 在计算之前做一次检查
        if (!priceCalculator.isValid())
            throw new PriceCalculateException("创建价格计算器失败. " + priceCalculator.getErrorMessage());

        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
        List<BaseMongoMap<String, Object>> platformSkus = cart.getSkus();

        Map<String, CmsBtProductModel_Sku> commonSkuMap = commonSkus.stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        // 对 sku 进行匹配
        // 获取重量进行运费计算
        for (BaseMongoMap<String, Object> platformSku : platformSkus) {

            String skuCode = platformSku.getStringAttribute("skuCode");

            if (!commonSkuMap.containsKey(skuCode))
                continue;

            CmsBtProductModel_Sku commonSku = commonSkuMap.get(skuCode);

            if (commonSku == null)
                continue;

            Double clientNetPrice = commonSku.getClientNetPrice();
            Double clientMsrp = commonSku.getClientMsrpPrice();
            Double weight = commonSku.getWeight();

            if (weight == null || weight < 1) {

                String weightString = shippingTypeConfig.getConfigValue2();

                if (StringUtils.isEmpty(weightString) || !StringUtils.isNumeric(weightString) || (weight = Double.valueOf(weightString)) <= 0)
                    throw new PriceCalculateException("没有为渠道 %s (%s) 的(SKU) %s 找到可用的商品重量", channelId, cartId, skuCode);
            }

            // 公式参数: 获取运费
            Double shippingFee = feeShippingService.getShippingFee(shippingType, weight);

            // !! 最终价格计算 !!

            // 计算指导价 Start
            Double retailPrice = priceCalculator
                    .setShippingFee(shippingFee)
                    .calculate(clientNetPrice);

            if (retailPrice < 1)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s");

            // 指导价合法
            // 则, 需要进行指导价波动计算
            // 如果打开了同步开关, 则需要同步设置最终售价

            // 获取上一次指导价
            Double lastRetailPrice = getProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceRetail);
            // 获取价格波动字符串
            String priceFluctuation = getPriceFluctuation(retailPrice, lastRetailPrice);
            // 保存价格波动
            platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceChgFlg.name(), priceFluctuation);

            if (isAutoApprovePrice)
                setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceSale, retailPrice);

            // 保存击穿标识
            String priceDiffFlg = productSkuService.getPriceDiffFlg(channelId, platformSku);
            platformSku.put(CmsBtProductConstants.Platform_SKU_COM.priceDiffFlg.name(), priceDiffFlg);

            setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceRetail, retailPrice);

            // 计算指导价 End

            // 计算 MSRP Start

            Double originPriceMsrp = priceCalculator.calculate(clientMsrp);

            if (originPriceMsrp < 1)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s");

            if (isAutoSyncPriceMsrp)
                setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceMsrp, originPriceMsrp);

            setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.originalPriceMsrp, originPriceMsrp);

            // 计算 MSRP End
        }

        return product;
    }

    /**
     * 计算新指导价波动
     * <p>
     * 结果使用波动字符串表示: <br/>
     * - 上涨前缀使用 U, 下降前缀使用 D. 波动程度使用百分比. 老价格为 0, 则波动 100%, 为 U100%<br/>
     * - 在新老价格相同, 或老价格为 null 时, 标记波动值为 "" 空字符串
     *
     * @param retailPrice     新指导价
     * @param lastRetailPrice 上一次的指导价
     * @return 波动字符串
     */
    private String getPriceFluctuation(Double retailPrice, Double lastRetailPrice) {

        // 老价格为空, 表示新建, 则不需要设置波动
        // 新老价格相同也同样
        if (lastRetailPrice == null || lastRetailPrice.equals(retailPrice))
            return "";

        Long range;

        if (lastRetailPrice < retailPrice) {
            if (lastRetailPrice.equals(0D))
                return "U100%";
            else {
                range = Math.round(((retailPrice - lastRetailPrice) / lastRetailPrice) * 100d);
                return "U" + range + "%";
            }
        } else {
            range = Math.round(((lastRetailPrice - retailPrice) / lastRetailPrice) * 100d);
            return "D" + range + "%";
        }
    }

    private Double getProductPrice(BaseMongoMap<String, Object> platformSku, CmsBtProductConstants.Platform_SKU_COM commonField) {

        Object value = platformSku.get(commonField.name());

        if (value == null)
            return null;

        return platformSku.getDoubleAttribute(commonField.name());
    }

    private void setProductPrice(BaseMongoMap<String, Object> platformSku, CmsBtProductConstants.Platform_SKU_COM commonField, Double priceValue) {
        platformSku.put(commonField.name(), priceValue);
    }

    /**
     * 价格计算器, 在同一款商品进行价格计算时, 可以用来保持部分参数. 同时包含对参数和价格、计算部分的校验
     * <p>
     * 如果 {@code isValid()} 为 {@code false}, 可以通过 {@code getErrorMessage()} 获取合并后的错误信息
     * <p>
     * 真正触发计算并获取结果, 需要调用 {@code calculate()}
     */
    private class PriceCalculator {

        private Double shippingFee;

        private Double exchangeRate;

        private Double voCommission;

        private Double pfCommission;

        private Double returnRate;

        private Double taxRate;

        private Double otherFee;

        private boolean valid = true;

        private boolean roundUp = false;

        private List<String> messageList = new ArrayList<>();

        private void checkValid(Double inputFee, String title) {

            if (inputFee != null && inputFee > 0)
                return;

            // 如果是已经存在的错误信息, 就不需要再加了
            if (messageList.contains(title))
                return;

            messageList.add(title + "读取错误");
            valid = false;
        }

        private PriceCalculator setShippingFee(Double shippingFee) {
            checkValid(shippingFee, "运费");
            this.shippingFee = shippingFee;
            return this;
        }

        private PriceCalculator setExchangeRate(Double exchangeRate) {
            checkValid(shippingFee, "汇率");
            this.exchangeRate = exchangeRate;
            return this;
        }

        private PriceCalculator setVoCommission(Double voCommission) {
            checkValid(shippingFee, "公司佣金比例");
            this.voCommission = voCommission;
            return this;
        }

        private PriceCalculator setPfCommission(Double pfCommission) {
            checkValid(shippingFee, "平台佣金比例");
            this.pfCommission = pfCommission;
            return this;
        }

        private PriceCalculator setReturnRate(Double returnRate) {
            checkValid(shippingFee, "退货率");
            this.returnRate = returnRate;
            return this;
        }

        private PriceCalculator setTaxRate(Double taxRate) {
            checkValid(shippingFee, "运费");
            this.taxRate = taxRate;
            return this;
        }

        private PriceCalculator setOtherFee(Double otherFee) {
            checkValid(shippingFee, "其他费用");
            this.otherFee = otherFee;
            return this;
        }

        private PriceCalculator setRoundUp(boolean roundUp) {
            this.roundUp = roundUp;
            return this;
        }

        private String getErrorMessage() {
            return StringUtils.join(messageList, ", ");
        }

        private boolean isValid() {
            return this.valid;
        }

        /**
         * 基于原始价格计算新价格
         *
         * @param inputPrice 原始价格
         * @return 新价格
         * @throws PriceCalculateException 在计算前, 尝试计算公式的分母, 如果分母为 0 则报错
         */
        private Double calculate(Double inputPrice) throws PriceCalculateException {

            if (!valid)
                return -1D;

            // 计算公式分母
            double denominator = 100d - voCommission - pfCommission - returnRate - taxRate;

            // 如果分母不合法。。。
            if (denominator == 0)
                throw new PriceCalculateException("根据这些参数 [VoyageOne Commission:%s], [Platform Commission:%s], [Return Rate:%s], [Tax Rate:%s] 计算出公式的分母为 0"
                        , voCommission, pfCommission, returnRate, taxRate, denominator);

            Double price = ((inputPrice + shippingFee + otherFee) * exchangeRate * 100d) / denominator;

            if (roundUp) {
                // 需要向上取整
                price = Math.ceil(price);
            } else {
                // 不需要, 就保留两位, 四舍五入
                price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }

            return price;
        }
    }
}
