package com.voyageone.service.impl.cms.prices;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.product.ProductSkuService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.voyageone.common.CmsConstants.ChannelConfig.*;
import static com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM.*;
import static java.util.stream.Collectors.toMap;

/**
 * 为商品提供价格的统一计算入口
 * <p>
 * Created by Ethan Shi on 2016/7/13.
 *
 * @author jonas
 * @author Ethan Shi
 * @version 2.4.0
 * @since 2.3.0
 */
@Service
public class PriceService extends BaseService {

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
     * 为商品计算并保存价格
     *
     * @param product 需要计算价格的商品
     * @param synSalePriceFlg 是否同步指导价到最终售价
     *                        （当该店铺已配置自动同步机制时，该参数不起作用；
     *                          当该店铺未配置自动同步机制时，若该参数设为"true"，则表示要同步指导价到最终售价，
     *                                                      若该参数设为"false"，则表示不同步指导价到最终售价）
     *
     * @throws IllegalPriceConfigException 计算价格前, 依赖的配置读取错误
     * @throws PriceCalculateException     计算价格时, 计算过程错误或结果错误
     */
    public void setPrice(CmsBtProductModel product, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        Assert.notNull(product).elseThrowDefaultWithTitle("product");

        Map<String, CmsBtProductModel_Platform_Cart> platforms = product.getPlatforms();

        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> cartEntry : platforms.entrySet()) {

            CmsBtProductModel_Platform_Cart cart = cartEntry.getValue();

            Integer cartId = cart.getCartId();

            // 对特定平台进行跳过
            // 不需要为这些平台计算价格
            if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                continue;

            setPrice(product, cartId, synSalePriceFlg);
        }
    }

    /**
     * 为商品在指定平台计算并保存价格
     *
     * @param product 需要计算价格的商品
     * @param cartId  平台ID
     * @param synSalePriceFlg 是否同步指导价到最终售价
     *
     * @throws IllegalPriceConfigException 计算价格前, 依赖的配置读取错误
     * @throws PriceCalculateException     计算价格时, 计算过程错误或结果错误
     */
    public void setPrice(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        Assert.notNull(product).elseThrowDefaultWithTitle("product");

        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");

        String channelId = product.getChannelId();

        CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, PRICE_CALCULATOR);

        String priceCalculator;

        if (priceCalculatorConfig == null || StringUtils.isEmpty(priceCalculator = priceCalculatorConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("无法获取价格计算方式的配置: " + channelId);

        switch (priceCalculator) {
            case PRICE_CALCULATOR_SYSTEM:
                setPriceBySystem(product, cartId, synSalePriceFlg);
                break;
            case PRICE_CALCULATOR_FORMULA:
                setPriceByFormula(product, cartId, synSalePriceFlg);
                break;
            default:
                throw new IllegalPriceConfigException("获取的价格计算方式不合法: %s ('%s')", channelId, priceCalculator);
        }
    }

    /**
     * 使用固定公式计算价格, 并保存到商品模型上
     *
     * @param product 目标商品, 必须提供渠道、商品的 COMMON 信息等
     * @param cartId  目标平台ID, 如 23、27 等
     * @param synSalePriceFlg 是否同步指导价到最终售价
     *
     * @throws IllegalPriceConfigException 获取价格公式错误
     * @throws PriceCalculateException     价格计算错误
     */
    private void setPriceByFormula(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        // 获取双价格公式
        String msrpFormula = getCalculateFormula(product, PRICE_MSRP_CALC_FORMULA);
        String retailFormula = getCalculateFormula(product, PRICE_RETAIL_CALC_FORMULA);

        // 获取商品的 COMMON SKU 信息
        // 并转换为 MAP, 便于查找
        CmsBtProductModel_Common common = product.getCommon();
        List<CmsBtProductModel_Sku> skusInCommon = common.getSkus();
        Map<String, CmsBtProductModel_Sku> commonSkuMap = skusInCommon.stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        String channelId = product.getChannelId();

        // 获取价格处理的部分配置
        boolean roundUp = isRoundUp(channelId);
        boolean isAutoApprovePrice = isAutoApprovePrice(channelId) || synSalePriceFlg;
        boolean isAutoSyncPriceMsrp = isAutoSyncPriceMsrp(channelId);

        // 去平台 SKU 信息
        // 遍历计算并保存价格

        CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);

        List<BaseMongoMap<String, Object>> skus = cart.getSkus();

        for (BaseMongoMap<String, Object> sku : skus) {

            String skuCodeValue = sku.getStringAttribute(skuCode.name());

            CmsBtProductModel_Sku skuInCommon = commonSkuMap.get(skuCodeValue);

            // 计算指导价
            Double retailPrice = calculateByFormula(retailFormula, skuInCommon, roundUp);

            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, cartId, skuCodeValue, retailPrice);

            setProductRetailPrice(sku, retailPrice, isAutoApprovePrice, channelId);

            // 计算 MSRP
            Double originMsrp = calculateByFormula(msrpFormula, skuInCommon, roundUp);

            if (originMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, cartId, skuCodeValue, originMsrp);

            setProductMsrp(sku, originMsrp, isAutoSyncPriceMsrp);
        }
    }

    /**
     * 计算 product 中 cart 下的各个 sku 的 retailPrice 和 originPriceMsrp, 当打开部分配置时, 会同步 price sale 和 msrp
     * <p>
     * 计算所需的商品模型必须提供以下内容
     * <ul>
     * <li>channelId</li>
     * <li>common
     * <ul>
     * <li>fields
     * <ul>
     * <li>commissionRate</li>
     * <li>hsCodePrivate</li>
     * <li>code</li>
     * </ul></li>
     * <li>skus
     * <ul>
     * <li>clientNetPrice</li>
     * <li>clientMsrpPrice</li>
     * <li>weight</li>
     * <li>skuCode</li>
     * </ul></li>
     * </ul></li>
     * <li>platforms
     * <ul>
     * <li>pBrandId</li>
     * <li>pCateId</li>
     * <li>skus
     * <ul>
     * <li>skuCode</li>
     * </ul></li>
     * </ul></li>
     * </ul>
     *
     * @param product 包含计算所需参数的商品模型
     * @param cartId  平台 ID
     * @param synSalePriceFlg 是否同步指导价到最终售价
     *
     * @throws PriceCalculateException 当价格计算公式中, 参数无法正确获取时, 或计算结果不合法时, 抛出该错误
     */
    private void setPriceBySystem(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg) throws PriceCalculateException, IllegalPriceConfigException {

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

        // 计算是否自动同步最终售价
        boolean isAutoApprovePrice = isAutoApprovePrice(channelId) || synSalePriceFlg;

        // 计算是否计算 MSRP
        boolean isAutoSyncPriceMsrp = isAutoSyncPriceMsrp(channelId);

        // 计算发货方式

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean shippingTypeConfig = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE, String.valueOf(cartId));

        if (shippingTypeConfig == null) {
            shippingTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE);
        }

        String shippingType;

        if (shippingTypeConfig == null || StringUtils.isEmpty(shippingType = shippingTypeConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("没有找到渠道 %s (%s) 可用的发货方式", channelId, cartId);

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

        // 计算税号

        String hsCode = product.getCommon().getFields().getHsCodePrivate();

        if (!StringUtils.isEmpty(hsCode)) {

            String[] strings = hsCode.split(",");

            hsCode = strings[0];
        }

        if (StringUtils.isEmpty(hsCode)) {

            // 最终计算税号依然不能正确获取
            // 就标记价格为异常价格

            cart.getSkus().forEach(sku -> {
                sku.put(priceRetail.name(), -1D);
                sku.put(originalPriceMsrp.name(), 0D);
                resetPriceIfInvalid(sku, priceMsrp, -1D);
                resetPriceIfInvalid(sku, priceSale, 0D);
            });

            return;
        }

        // 公式参数: 税率
        Double taxRate = feeTaxService.getTaxRate(hsCode);

        // 进入计算阶段
        SystemPriceCalculator systemPriceCalculator = new SystemPriceCalculator()
                .setRoundUp(isRoundUp(channelId))
                .setTaxRate(taxRate)
                .setPfCommission(platformCommission)
                .setReturnRate(returnRate)
                .setVoCommission(voyageOneCommission)
                .setExchangeRate(exchangeRate)
                .setOtherFee(otherFee);

        // 对设置到价格计算器上的参数
        // 在计算之前做一次检查
        if (!systemPriceCalculator.isValid())
            throw new IllegalPriceConfigException("创建价格计算器失败. " + systemPriceCalculator.getErrorMessage());

        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
        List<BaseMongoMap<String, Object>> platformSkus = cart.getSkus();

        Map<String, CmsBtProductModel_Sku> commonSkuMap = commonSkus.stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        // 对 sku 进行匹配
        // 获取重量进行运费计算
        for (BaseMongoMap<String, Object> platformSku : platformSkus) {

            String skuCodeValue = platformSku.getStringAttribute("skuCode");

            if (!commonSkuMap.containsKey(skuCodeValue))
                continue;

            CmsBtProductModel_Sku commonSku = commonSkuMap.get(skuCodeValue);

            if (commonSku == null)
                continue;

            Double clientNetPrice = commonSku.getClientNetPrice();
            Double clientMsrp = commonSku.getClientMsrpPrice();

            Double weight = commonSku.getWeight();

            if (weight == null || weight <= 0) {

                String weightString = shippingTypeConfig.getConfigValue2();

                if (StringUtils.isEmpty(weightString) || !StringUtils.isNumeric(weightString) || (weight = Double.valueOf(weightString)) <= 0)
                    throw new PriceCalculateException("没有为渠道 %s (%s) 的(SKU) %s 找到可用的商品重量", channelId, cartId, skuCodeValue);
            }

            // 公式参数: 获取运费
            Double shippingFee = feeShippingService.getShippingFee(shippingType, weight);
            systemPriceCalculator.setShippingFee(shippingFee);

            // !! 最终价格计算 !!

            // 计算指导价
            Double retailPrice = systemPriceCalculator.calculate(clientNetPrice);

            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, cartId, skuCodeValue, retailPrice);

            setProductRetailPrice(platformSku, retailPrice, isAutoApprovePrice, channelId);

            // 计算 MSRP
            Double originPriceMsrp = systemPriceCalculator.calculate(clientMsrp);

            if (originPriceMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, cartId, skuCodeValue, originPriceMsrp);

            setProductMsrp(platformSku, originPriceMsrp, isAutoSyncPriceMsrp);
        }
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
    public String getPriceFluctuation(Double retailPrice, Double lastRetailPrice) {

        // 老价格为空, 表示新建, 则不需要设置波动
        // 新老价格相同也同样(价格=-1时，返回空)
        if (retailPrice == null || retailPrice < 0D
                || lastRetailPrice == null || lastRetailPrice < 0D
                || lastRetailPrice.equals(retailPrice))
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

    /**
     * 辅助方法: 获取商品价格
     * <p>
     * 先尝试获取无类型值, 判断是否为 null, 再获取具体的数字
     *
     * @param platformSku 平台 sku 模型
     * @param commonField 价格字段名
     * @return 有可能为 null 的价格数字
     */
    private Double getProductPrice(BaseMongoMap<String, Object> platformSku, CmsBtProductConstants.Platform_SKU_COM commonField) {

        Object value = platformSku.get(commonField.name());

        if (value == null)
            return null;

        return platformSku.getDoubleAttribute(commonField.name());
    }

    /**
     * 辅助方法: 获取商品的指定价格, 如果价格的值不合法, 就使用 {@code priceValue} 指定的值替换
     *
     * @param platformSku 平台 sku 模型
     * @param commonField 价格字段名
     * @param priceValue  商品原价格非法时使用的指定值
     */
    private void resetPriceIfInvalid(BaseMongoMap<String, Object> platformSku, CmsBtProductConstants.Platform_SKU_COM commonField, Double priceValue) {
        Double _priceValue = getProductPrice(platformSku, commonField);
        if (_priceValue == null || _priceValue <= 0)
            platformSku.put(commonField.name(), priceValue);
    }

    /**
     * 为商品设置 {@code retailPrice} 提供的指导价,
     * 如果 {@code isAutoApprovePrice} 为 {@code true},
     * 就同时设置到 {@code skuInPlatform} 的 {@code priceSale} 属性上。同时为商品这次的价格变动,
     * 更新波动标识 {@code priceChgFlg} 和击穿标识 {@code priceDiffFlg}
     *
     * @param skuInPlatform      商品的平台 sku 模型
     * @param retailPrice        计算出的人民币指导价
     * @param isAutoApprovePrice 是否同步设置最终售价
     * @param channelId          这次计算商品所在渠道
     */
    private void setProductRetailPrice(BaseMongoMap<String, Object> skuInPlatform, Double retailPrice, boolean isAutoApprovePrice, String channelId) {

        // 指导价合法
        // 则, 需要进行指导价波动计算
        // 如果打开了同步开关, 则需要同步设置最终售价

        // 获取上一次指导价
        Double lastRetailPrice = getProductPrice(skuInPlatform, priceRetail);
        // 获取价格波动字符串
        String priceFluctuation = getPriceFluctuation(retailPrice, lastRetailPrice);
        // 保存价格波动
        skuInPlatform.put(priceChgFlg.name(), priceFluctuation);

        skuInPlatform.put(priceRetail.name(), retailPrice);

        if (isAutoApprovePrice)
            skuInPlatform.put(priceSale.name(), retailPrice);
        else
            // 如果不强制同步的话, 要看看是否原本是合法价格
            // 如果原本不是合法价格的话, 就同步设置
            resetPriceIfInvalid(skuInPlatform, priceSale, retailPrice);

        // 保存击穿标识
        String priceDiffFlgValue = productSkuService.getPriceDiffFlg(channelId, skuInPlatform);
        // 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，比指导价高:3，向上击穿警告:4，向下击穿警告:5）
        skuInPlatform.put(priceDiffFlg.name(), priceDiffFlgValue);

    }

    /**
     * 为商品设置 {@code originPriceMsrp} 提供的人民币建议零售价, 如果 {@code isAutoSyncPriceMsrp} 为 {@code true},
     * 就同步设置价格到 {@code priceMsrp} 属性
     *
     * @param skuInPlatform       商品的平台 sku 模型
     * @param originPriceMsrp     根据客户建议零售价计算的人民币建议零售价
     * @param isAutoSyncPriceMsrp 是否同步设置人民币建议零售价
     */
    private void setProductMsrp(BaseMongoMap<String, Object> skuInPlatform, Double originPriceMsrp, boolean isAutoSyncPriceMsrp) {

        if (isAutoSyncPriceMsrp)
            skuInPlatform.put(priceMsrp.name(), originPriceMsrp);
        else
            // 如果不强制同步的话, 要看看是否原本是合法价格
            // 如果原本不是合法价格的话, 就同步设置
            resetPriceIfInvalid(skuInPlatform, priceMsrp, originPriceMsrp);

        skuInPlatform.put(originalPriceMsrp.name(), originPriceMsrp);
    }

    private boolean isAutoSyncPriceMsrp(String channelId) {
        boolean isAutoSyncPriceMsrp = false;

        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_SYNC_PRICE_MSRP);

        if (autoSyncPriceMsrp != null && "1".equals(autoSyncPriceMsrp.getConfigValue1()))
            isAutoSyncPriceMsrp = true;
        return isAutoSyncPriceMsrp;
    }

    private boolean isAutoApprovePrice(String channelId) {

        boolean isAutoApprovePrice = false;

        CmsChannelConfigBean autoApprovePrice = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.AUTO_APPROVE_PRICE);

        if (autoApprovePrice != null && autoApprovePrice.getConfigValue1() != null && "1".equals(autoApprovePrice.getConfigValue1()))
            isAutoApprovePrice = true;

        return isAutoApprovePrice;
    }

    private boolean isRoundUp(String channelId) {

        boolean isRoundUp = true;

        CmsChannelConfigBean configBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.PRICE_ROUND_UP_FLG);

        if (configBean != null && "0".equals(configBean.getConfigValue1())) {
            isRoundUp = false;
        }
        return isRoundUp;
    }

    /**
     * 获取商品的价格计算公式
     *
     * @param product    目标商品
     * @param formulaKey 目标价格计算公式的配置键
     * @return 价格计算公式
     * @throws IllegalPriceConfigException 无法获取公式配置
     */
    private String getCalculateFormula(CmsBtProductModel product, String formulaKey) throws IllegalPriceConfigException {

        String channelId = product.getChannelId();

        // 尝试获取类目级别的价格计算公式
        CmsChannelConfigBean formulaConfig = CmsChannelConfigs.getConfigBean(channelId, formulaKey, product.getFeed().getCatPath());

        if (formulaConfig == null)
            // 尝试获取 Model 级别的价格计算公式
            formulaConfig = CmsChannelConfigs.getConfigBean(channelId, formulaKey, product.getCommon().getFields().getModel());

        if (formulaConfig == null)
            // 最终, 尝试获取无限制渠道级别计算公式
            formulaConfig = CmsChannelConfigs.getConfigBeanNoCode(channelId, formulaKey);

        String formula;

        if (formulaConfig == null || StringUtils.isEmpty(formula = formulaConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("无法获取价格计算公式配置: %s, %s", channelId, formulaKey);

        return formula;
    }

    /**
     * 使用公式为 sku 计算相应价格
     *
     * @param formula 价格计算公式, 从 {@code getCalculateFormula()} 获取
     * @param sku     包含公式参数的 sku 模型
     * @return 计算后的价格
     * @throws IllegalPriceConfigException sku 中不包含公式所需的参数
     */
    private Double calculateByFormula(String formula, CmsBtProductModel_Sku sku, boolean roundUp) throws IllegalPriceConfigException {

        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(formula);

        StandardEvaluationContext context = new StandardEvaluationContext(sku);

        try {
            Double price = expression.getValue(context, Double.class);
            return roundDouble(price, roundUp);
        } catch (SpelEvaluationException sp) {
            throw new IllegalPriceConfigException("使用固定公式计算时出现错误", sp);
        }
    }

    /**
     * 对输入数字取整或四舍五入
     *
     * @param input   输入的数字
     * @param roundUp true 时就向上取整, 否则对数字四舍五入
     * @return 取证或四舍五入后的结果
     */
    private Double roundDouble(Double input, boolean roundUp) {
        if (roundUp) {
            // 需要向上取整
            input = Math.ceil(input);
        } else {
            // 不需要, 就保留两位, 四舍五入
            input = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return input;
    }

    /**
     * 体系价格计算器
     * <p>
     * 在同一款商品进行价格计算时, 用来保持部分参数. 同时包含对参数和价格、计算部分的校验
     * <p>
     * 如果 {@code isValid()} 为 {@code false}, 可以通过 {@code getErrorMessage()} 获取合并后的错误信息
     * <p>
     * 真正触发计算并获取结果, 需要调用 {@code calculate()}
     */
    private class SystemPriceCalculator {

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

            if (inputFee != null)
                return;

            // 如果是已经存在的错误信息, 就不需要再加了
            if (messageList.contains(title))
                return;

            messageList.add(title + "读取错误");
            valid = false;
        }

        private SystemPriceCalculator setShippingFee(Double shippingFee) {
            checkValid(this.shippingFee = shippingFee, "运费");
            return this;
        }

        private SystemPriceCalculator setExchangeRate(Double exchangeRate) {
            checkValid(this.exchangeRate = exchangeRate, "汇率");
            return this;
        }

        private SystemPriceCalculator setVoCommission(Double voCommission) {
            checkValid(this.voCommission = voCommission, "公司佣金比例");
            return this;
        }

        private SystemPriceCalculator setPfCommission(Double pfCommission) {
            checkValid(this.pfCommission = pfCommission, "平台佣金比例");
            return this;
        }

        private SystemPriceCalculator setReturnRate(Double returnRate) {
            checkValid(this.returnRate = returnRate, "退货率");
            return this;
        }

        private SystemPriceCalculator setTaxRate(Double taxRate) {
            checkValid(this.taxRate = taxRate, "税率");
            return this;
        }

        private SystemPriceCalculator setOtherFee(Double otherFee) {
            checkValid(this.otherFee = otherFee, "其他费用");
            return this;
        }

        private SystemPriceCalculator setRoundUp(boolean roundUp) {
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
         * @throws IllegalPriceConfigException 在计算前, 尝试计算公式的分母, 如果分母为 0 则报错
         */
        private Double calculate(Double inputPrice) throws IllegalPriceConfigException {

            if (!valid)
                return -1D;

            // 计算公式分母
            double denominator = 100d - voCommission - pfCommission - returnRate - taxRate;

            // 如果分母不合法。。。
            if (denominator == 0)
                throw new IllegalPriceConfigException("根据这些参数 [VoyageOne Commission:%s], [Platform Commission:%s], [Return Rate:%s], [Tax Rate:%s] 计算出公式的分母为 0"
                        , voCommission, pfCommission, returnRate, taxRate, denominator);

            Double price = ((inputPrice + shippingFee + otherFee) * exchangeRate * 100d) / denominator;

            return roundDouble(price, roundUp);
        }
    }
}
