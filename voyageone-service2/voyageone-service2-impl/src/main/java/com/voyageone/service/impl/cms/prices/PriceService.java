package com.voyageone.service.impl.cms.prices;

import com.google.common.collect.Lists;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.Constants;
import com.voyageone.common.asserts.Assert;
import com.voyageone.common.configs.Carts;
import com.voyageone.common.configs.Channels;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Enums.CartEnums;
import com.voyageone.common.configs.Enums.ChannelConfigEnums;
import com.voyageone.common.configs.TypeChannels;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.configs.beans.TypeChannelBean;
import com.voyageone.common.util.JacksonUtil;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.impl.cms.sx.SxProductService;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel;
import com.voyageone.service.model.cms.mongo.feed.CmsBtFeedInfoModel_Sku;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants.Platform_SKU_COM;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Common;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    private final SxProductService sxProductService;

    @Autowired
    public PriceService(CmsMtFeeShippingService feeShippingService, CmsMtFeeTaxService feeTaxService,
                        CmsMtFeeCommissionService feeCommissionService, CmsMtFeeExchangeService feeExchangeService,
                        SxProductService sxProductService) {
        this.feeShippingService = feeShippingService;
        this.feeTaxService = feeTaxService;
        this.feeCommissionService = feeCommissionService;
        this.feeExchangeService = feeExchangeService;
        this.sxProductService = sxProductService;
    }

    /**
     * 为商品计算并保存价格
     *
     * @param product         需要计算价格的商品
     * @param synSalePriceFlg 是否同步指导价到最终售价
     *                        （当该店铺已配置自动同步机制时，该参数不起作用；
     *                        当该店铺未配置自动同步机制时，若该参数设为"true"，则表示要同步指导价到最终售价，
     *                        若该参数设为"false"，则表示不同步指导价到最终售价）
     * @throws IllegalPriceConfigException 计算价格前, 依赖的配置读取错误
     * @throws PriceCalculateException     计算价格时, 计算过程错误或结果错误
     */
    public Integer setPrice(CmsBtProductModel product, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        Integer chg = 0;
        Assert.notNull(product).elseThrowDefaultWithTitle("product");
        CmsBtProductModel old = JacksonUtil.json2Bean(JacksonUtil.bean2Json(product),CmsBtProductModel.class);

        Map<String, CmsBtProductModel_Platform_Cart> platforms = product.getPlatforms();

        for (Map.Entry<String, CmsBtProductModel_Platform_Cart> cartEntry : platforms.entrySet()) {

            CmsBtProductModel_Platform_Cart cart = cartEntry.getValue();

            Integer cartId = cart.getCartId();

            // 对特定平台进行跳过
            // 不需要为这些平台计算价格
            if (cartId < CmsConstants.ACTIVE_CARTID_MIN)
                continue;

            recountPrice(product, cartId, synSalePriceFlg);
            chg |= skuCompare(old, product, cartId);
        }
        return chg;
    }

    /**
     * 为商品在指定平台计算并保存价格
     *
     * @param product         需要计算价格的商品
     * @param cartId          平台ID
     * @param synSalePriceFlg 是否同步指导价到最终售价
     * @throws IllegalPriceConfigException 计算价格前, 依赖的配置读取错误
     * @throws PriceCalculateException     计算价格时, 计算过程错误或结果错误
     */
    public Integer setPrice(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        Integer chg = 0;
        Assert.notNull(product).elseThrowDefaultWithTitle("product");
        CmsBtProductModel old = JacksonUtil.json2Bean(JacksonUtil.bean2Json(product),CmsBtProductModel.class);

        // 对特定平台进行跳过
        // 不需要为这些平台计算价格
        if (cartId >= CmsConstants.ACTIVE_CARTID_MIN) {

            recountPrice(product, cartId, synSalePriceFlg);
            chg |= skuCompare(old, product, cartId);
        }
        return chg;
    }

    /**
     * 用于feed导入的时候初步计算中国相关价格
     * @param feed
     */
    public void setFeedPrice(CmsBtFeedInfoModel feed) throws PriceCalculateException, IllegalPriceConfigException{
        // 如果是子店采用旗舰店的价格公式计算

        String channelId = feed.getChannelId();
        List<TypeChannelBean> typeChannelBeans = TypeChannels.getTypeListSkuCarts(feed.getChannelId(), Constants.comMtTypeChannel.SKU_CARTS_53_A, "cn");
        for(TypeChannelBean typeChannelBean : typeChannelBeans){
            if (Channels.isUsJoi(typeChannelBean.getValue()) && typeChannelBeans.size() == 1) {
                channelId = "928";
                break;
            }
        };

        CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, "0");

        String priceCalculator;
        if (priceCalculatorConfig == null || StringUtils.isEmpty(priceCalculator = priceCalculatorConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("无法获取价格计算方式的配置: " + channelId);

        switch (priceCalculator) {
            case PRICE_CALCULATOR_SYSTEM:
                setFeedPriceBySystem(feed, channelId);
                break;
            case PRICE_CALCULATOR_FORMULA:
                setFeedPriceByFormula(feed, channelId);
                break;
            default:
                throw new IllegalPriceConfigException("获取的价格计算方式不合法: %s ('%s')", channelId, priceCalculator);
        }

    }

    /**
     * check输入的价格是否符合要求
     * @param channelId
     * @param platform
     * @param cartId
     * @return
     */
    public void priceChk(String channelId, CmsBtProductModel_Platform_Cart platform, Integer cartId) {

        // 获取中国建议售价的配置信息
        CmsChannelConfigBean autoSyncPriceMsrpConfig = getAutoSyncPriceMsrpOption(channelId, cartId);

        // 阀值
        CmsChannelConfigBean mandatoryBreakThresholdConfig = getMandatoryBreakThresholdOption(channelId, cartId);

        List<BaseMongoMap<String, Object>> cmsBtProductModel_skus = platform.getSkus();

        if (cmsBtProductModel_skus != null) {
            cmsBtProductModel_skus.forEach(sku -> {
                priceCheck(sku, autoSyncPriceMsrpConfig, mandatoryBreakThresholdConfig);
            });
        }
    }

    /**
     * check输入的价格是否符合要求
     * @param channelId
     * @param cmsBtProduct
     * @param cartId
     * @return
     */
    public void priceChk(String channelId, CmsBtProductModel cmsBtProduct, Integer cartId) {

        // 获取中国建议售价的配置信息
        CmsChannelConfigBean autoSyncPriceMsrpConfig = getAutoSyncPriceMsrpOption(channelId, cartId);

        // 阀值
        CmsChannelConfigBean mandatoryBreakThresholdConfig = getMandatoryBreakThresholdOption(channelId, cartId);

        List<BaseMongoMap<String, Object>> cmsBtProductModel_skus = cmsBtProduct.getPlatform(cartId).getSkus();

        if (cmsBtProductModel_skus != null) {
            cmsBtProductModel_skus.forEach(sku -> {
                priceCheck(sku, autoSyncPriceMsrpConfig, mandatoryBreakThresholdConfig);
            });
        }
    }

    /**
     * check单个sku的价格是否合法
     * @param skuInfo
     * @param autoSyncPriceMsrpConfig
     * @param mandatoryBreakThresholdConfig
     */
    public void priceCheck (BaseMongoMap<String, Object> skuInfo, CmsChannelConfigBean autoSyncPriceMsrpConfig, CmsChannelConfigBean mandatoryBreakThresholdConfig) {

        String isAutoPriceMsrp = autoSyncPriceMsrpConfig.getConfigValue1();
        final Boolean isCheckMandatory = "1".equals(mandatoryBreakThresholdConfig.getConfigValue1()) ? true : false;
        final Double breakThreshold = Double.parseDouble(mandatoryBreakThresholdConfig.getConfigValue2()) / 100D;

        Double priceMsrp = skuInfo.getDoubleAttribute("priceMsrp");
        Double minPriceRetail = Math.ceil(skuInfo.getDoubleAttribute("priceRetail") * (1 - breakThreshold));
        Double priceSale = skuInfo.getDoubleAttribute("priceSale");
        Boolean isSale = Boolean.valueOf(skuInfo.getStringAttribute("isSale"));
        if (isSale) {
            if (priceSale == 0.0D || ( "0".equals(isAutoPriceMsrp) && priceMsrp == 0.0D))
                throw new BusinessException("中国最终售价或者中国建议售价(可输入)不能为空");
            if (!"1".equals(autoSyncPriceMsrpConfig.getConfigValue1())
                    && priceMsrp.compareTo(priceSale) < 0)
                throw new BusinessException("中国建议售价(可输入)不能低于中国最终售价");
            if (isCheckMandatory && minPriceRetail.compareTo(priceSale) > 0)
                throw new BusinessException("4000094", minPriceRetail);
        }
    }

    /**
     * 为商品在指定平台计算并保存价格
     *
     * @param product         需要计算价格的商品
     * @param cartId          平台ID
     * @param synSalePriceFlg 是否同步指导价到最终售价
     * @throws IllegalPriceConfigException 计算价格前, 依赖的配置读取错误
     * @throws PriceCalculateException     计算价格时, 计算过程错误或结果错误
     */
    private void recountPrice(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg) throws IllegalPriceConfigException, PriceCalculateException {

        Assert.notNull(product).elseThrowDefaultWithTitle("product");

        Assert.notNull(cartId).elseThrowDefaultWithTitle("cartId");

        String channelId = product.getChannelId();

        CmsChannelConfigBean priceCalculatorConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, PRICE_CALCULATOR, cartId.toString());

        String priceCalculator;

        if (priceCalculatorConfig == null || StringUtils.isEmpty(priceCalculator = priceCalculatorConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("无法获取价格计算方式的配置: " + channelId);

        switch (priceCalculator) {
            case PRICE_CALCULATOR_SYSTEM:
                setPriceBySystem(product, cartId, synSalePriceFlg, priceCalculatorConfig);
                break;
            case PRICE_CALCULATOR_FORMULA:
                setPriceByFormula(product, cartId, synSalePriceFlg, priceCalculatorConfig);
                break;
            default:
                throw new IllegalPriceConfigException("获取的价格计算方式不合法: %s ('%s')", channelId, priceCalculator);
        }
    }

    /**
     * 获取AUTO_SYNC_PRICE_MSRP的配置信息
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return CmsChannelConfigBean
     */
    public CmsChannelConfigBean getAutoSyncPriceMsrpOption(String channelId, Integer cartId) {
        CmsChannelConfigBean autoSyncPriceMsrp = CmsChannelConfigs.getConfigBeanWithDefault(channelId, AUTO_SYNC_PRICE_MSRP, String.valueOf(cartId));
        if (autoSyncPriceMsrp != null
                && !Lists.newArrayList("0", "1", "2").contains(autoSyncPriceMsrp.getConfigValue1())) {
            throw new BusinessException("中国建议售价联动配置选项值错误: %s, %s", channelId, autoSyncPriceMsrp.getConfigValue1());
        }

        String commonSyncPriceMsrpVal = "0";
        if (autoSyncPriceMsrp == null) {
            String cartIdVal = String.valueOf(cartId);
            if (CartEnums.Cart.TT.getId().equals(cartIdVal)
                    || CartEnums.Cart.LTT.getId().equals(cartIdVal)
                    || CartEnums.Cart.LCN.getId().equals(cartIdVal)
                    || CartEnums.Cart.CN.getId().equals(cartIdVal)
                    || CartEnums.Cart.JM.getId().equals(cartIdVal)) {
                commonSyncPriceMsrpVal = "1";
            }
            autoSyncPriceMsrp = new CmsChannelConfigBean(channelId, AUTO_SYNC_PRICE_MSRP, cartId.toString(),"0", commonSyncPriceMsrpVal, "0", "SYSTEM");
        }

        return autoSyncPriceMsrp;
    }

    /**
     * 获取AUTO_SYNC_PRICE_SALE的配置信息
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return CmsChannelConfigBean
     */
    public CmsChannelConfigBean getAutoSyncPriceSaleOption(String channelId, Integer cartId) {
        CmsChannelConfigBean autoSyncPriceSale = CmsChannelConfigs.getConfigBeanWithDefault(channelId, AUTO_SYNC_PRICE_SALE, String.valueOf(cartId));
        if (autoSyncPriceSale != null
                && !Lists.newArrayList("0", "1", "2").contains(autoSyncPriceSale.getConfigValue1())) {
            throw new BusinessException("中国最终售价联动配置选项值错误: %s, %s", channelId, autoSyncPriceSale.getConfigValue1());
        }

        String commonSyncPriceSaleVal = "0";
        if (autoSyncPriceSale == null) {
            String cartIdVal = String.valueOf(cartId);
            if (CartEnums.Cart.TT.getId().equals(cartIdVal)
                    || CartEnums.Cart.LTT.getId().equals(cartIdVal)
                    || CartEnums.Cart.LCN.getId().equals(cartIdVal)
                    || CartEnums.Cart.CN.getId().equals(cartIdVal)
                    || CartEnums.Cart.JM.getId().equals(cartIdVal)) {
                commonSyncPriceSaleVal = "1";
            }

            autoSyncPriceSale = new CmsChannelConfigBean(channelId, AUTO_SYNC_PRICE_SALE, cartId.toString(),"0", commonSyncPriceSaleVal, "0", "SYSTEM");
        }
        return autoSyncPriceSale;
    }

    /**
     * 获取SHIPPING_TYPE的配置信息
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return CmsChannelConfigBean
     */
    public CmsChannelConfigBean getShippingTypeOption(String channelId, Integer cartId) throws IllegalPriceConfigException{

        // 计算发货方式
        //存在cms_mt_channel_config里
        CmsChannelConfigBean shippingTypeConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, SHIPPING_TYPE, String.valueOf(cartId));

        // 还是没有
        if (shippingTypeConfig == null)
            // 那就渠道取系统默认, 查最低级配置
            shippingTypeConfig = CmsChannelConfigs.getConfigBeanNoCode(ChannelConfigEnums.Channel.NONE.getId(), SHIPPING_TYPE);

        if (shippingTypeConfig == null || StringUtils.isEmpty(shippingTypeConfig.getConfigValue1()))
            throw new IllegalPriceConfigException("没有找到渠道 %s (%s) 可用的发货方式", channelId, cartId);

        if (StringUtils.isEmpty(shippingTypeConfig.getConfigValue3()))
            shippingTypeConfig.setConfigValue3("0.5");

        return shippingTypeConfig;
    }

    /**
     * 获取MANDATORY_BREAK_THRESHOLD的配置信息
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return CmsChannelConfigBean
     */
    public CmsChannelConfigBean getMandatoryBreakThresholdOption(String channelId, Integer cartId){

        // 计算发货方式

        // 存在cms_mt_channel_config里
        CmsChannelConfigBean mandatoryBreakThresholdConfig = CmsChannelConfigs.getConfigBeanWithDefault(channelId, MANDATORY_BREAK_THRESHOLD, String.valueOf(cartId));

        // 还是没有
        if (mandatoryBreakThresholdConfig == null)
            // 设置默认设置
            mandatoryBreakThresholdConfig = new CmsChannelConfigBean(channelId, String.valueOf(cartId), MANDATORY_BREAK_THRESHOLD, "0", "0", "", "SYSTEM");
        else if (StringUtils.isEmpty(mandatoryBreakThresholdConfig.getConfigValue1())
                || "0".equals(mandatoryBreakThresholdConfig.getConfigValue1()))
            mandatoryBreakThresholdConfig.setConfigValue2("0");

        return mandatoryBreakThresholdConfig;
    }

    /**
     * 获取AUTO_SYNC_PRICE_PROMOTION的配置信息
     *
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return CmsChannelConfigBean
     */
    public CmsChannelConfigBean getAutoSyncPricePromotionOption(String channelId, Integer cartId) {
        CmsChannelConfigBean autoSyncPricePromotion = CmsChannelConfigs.getConfigBeanWithDefault(channelId, AUTO_SYNC_PRICE_PROMOTION, String.valueOf(cartId));
        if (autoSyncPricePromotion != null
                && !Lists.newArrayList("0", "1", "2").contains(autoSyncPricePromotion.getConfigValue1())) {
            throw new BusinessException("活动期间价格同步规则获取错误: %s, %s", channelId, autoSyncPricePromotion.getConfigValue1());
        }
        if (autoSyncPricePromotion == null)
            autoSyncPricePromotion = new CmsChannelConfigBean(channelId, AUTO_SYNC_PRICE_PROMOTION, cartId.toString(),"0", "", "", "SYSTEM");

        return autoSyncPricePromotion;
    }

    /**
     * 获取PRICE_MSRP_CALC_FORMULA的配置信息
     *
     * @param product   产品信息
     * @param cartId    平台Id
     * @return String
     */
    public String getPriceMsrpCalcFormulaOption(CmsBtProductModel product, Integer cartId) throws IllegalPriceConfigException{

        String channelId = product.getChannelId();
        // 尝试获取类目级别的价格计算公式
        // 根据主类目获取价格公式
        List<CmsChannelConfigBean> formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_MSRP_CALC_FORMULA, String.valueOf(cartId));

        // 返回平台级配置
        CmsChannelConfigBean formulaConfig = getPriceConfig(product, formulaConfigs);

        if (formulaConfig == null) {
            formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_MSRP_CALC_FORMULA, "0");

            // 返回平台级配置
            formulaConfig = getPriceConfig(product, formulaConfigs);
        }

        if (formulaConfig == null || StringUtils.isEmpty(formulaConfig.getConfigValue2()))
            throw new IllegalPriceConfigException("无法获取价格计算公式配置: %s, %s", channelId, PRICE_MSRP_CALC_FORMULA);

        return formulaConfig.getConfigValue2();
    }

    /**
     * 获取PRICE_RETAIL_CALC_FORMULA的配置信息
     *
     * @param product   产品信息
     * @param cartId    平台Id
     * @return String
     */
    public String getPriceRetailCalcFormulaOption(CmsBtProductModel product, Integer cartId) throws IllegalPriceConfigException{

        String channelId = product.getChannelId();
        // 尝试获取类目级别的价格计算公式
        // 根据主类目获取价格公式
        List<CmsChannelConfigBean> formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_RETAIL_CALC_FORMULA, String.valueOf(cartId));

        // 返回平台级配置
        CmsChannelConfigBean formulaConfig = getPriceConfig(product, formulaConfigs);

        if (formulaConfig == null) {
            formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_RETAIL_CALC_FORMULA, "0");

            // 返回平台级配置
            formulaConfig = getPriceConfig(product, formulaConfigs);
        }

        if (formulaConfig == null || StringUtils.isEmpty(formulaConfig.getConfigValue2()))
            throw new IllegalPriceConfigException("无法获取价格计算公式配置: %s, %s", channelId, PRICE_RETAIL_CALC_FORMULA);

        return formulaConfig.getConfigValue2();
    }

    /**
     * 获取PRICE_MSRP_CALC_FORMULA的配置信息(Feed用)
     *
     * @param product   产品信息
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return String
     */
    public String getFeedPriceMsrpCalcFormulaOption(CmsBtFeedInfoModel product, String channelId, Integer cartId) throws IllegalPriceConfigException{
        // 尝试获取类目级别的价格计算公式
        // 根据主类目获取价格公式
        List<CmsChannelConfigBean> formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_MSRP_CALC_FORMULA, String.valueOf(cartId));

        // 返回平台级配置
        CmsChannelConfigBean formulaConfig = getFeedPriceConfig(product, formulaConfigs);

        if (formulaConfig == null) {
            formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_MSRP_CALC_FORMULA, "0");

            // 返回平台级配置
            formulaConfig = getFeedPriceConfig(product, formulaConfigs);
        }

        if (formulaConfig == null || StringUtils.isEmpty(formulaConfig.getConfigValue2()))
            throw new IllegalPriceConfigException("无法获取价格计算公式配置: %s, %s", channelId, PRICE_MSRP_CALC_FORMULA);

        return formulaConfig.getConfigValue2();
    }

    /**
     * 获取PRICE_RETAIL_CALC_FORMULA的配置信息(Feed)
     *
     * @param product   产品信息
     * @param channelId 店铺Id
     * @param cartId    平台Id
     * @return String
     */
    public String getFeedPriceRetailCalcFormulaOption(CmsBtFeedInfoModel product, String channelId, Integer cartId) throws IllegalPriceConfigException{
        // 尝试获取类目级别的价格计算公式
        // 根据主类目获取价格公式
        List<CmsChannelConfigBean> formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_RETAIL_CALC_FORMULA, String.valueOf(cartId));

        // 返回平台级配置
        CmsChannelConfigBean formulaConfig = getFeedPriceConfig(product, formulaConfigs);

        if (formulaConfig == null) {
            formulaConfigs = CmsChannelConfigs.getConfigBeans(channelId, PRICE_RETAIL_CALC_FORMULA, "0");

            // 返回平台级配置
            formulaConfig = getFeedPriceConfig(product, formulaConfigs);
        }

        if (formulaConfig == null || StringUtils.isEmpty(formulaConfig.getConfigValue2()))
            throw new IllegalPriceConfigException("无法获取价格计算公式配置: %s, %s", channelId, PRICE_RETAIL_CALC_FORMULA);

        return formulaConfig.getConfigValue2();
    }

    /**
     * sku共同属性PriceDiffFlg计算方法
     * 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，比指导价高:3，向上击穿警告:4，向下击穿警告:5）
     *
     * @param channelId 渠道Id
     * @param sku sku数据
     * @param cartId 平台Id
     * @return 判断结果
     */
    public String getPriceDiffFlg(String channelId, BaseMongoMap<String, Object> sku, Integer cartId) {
        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = getMandatoryBreakThresholdOption(channelId, cartId);
        Double breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue2()) / 100D ;

        return getPriceDiffFlg(breakThreshold, sku.getDoubleAttribute(priceSale.name()), sku.getDoubleAttribute(priceRetail.name()));
    }

    /**
     * 通过SYSTEM的价格公式计算价格
     * @param product 产品信息
     * @param cartId  平台Id
     * @param synSalePriceFlg 是否自动同步(如果系统已配置,则设置无效)
     * @param priceCalculatorConfig 价格确认配置
     * @throws PriceCalculateException
     * @throws IllegalPriceConfigException
     */
    private void setPriceBySystem(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg, CmsChannelConfigBean priceCalculatorConfig) throws PriceCalculateException, IllegalPriceConfigException {

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

        Integer platformId = Integer.valueOf(Carts.getCart(cartId).getPlatform_id());

        // 计算是否计算 MSRP
        CmsChannelConfigBean autoSyncPriceMsrpConfig = getAutoSyncPriceMsrpOption(channelId, cartId);

        // 计算发货方式

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean shippingTypeConfig = getShippingTypeOption(channelId, cartId);

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

        /*
         * 1.如果无税号，价格计算 按默认值11.9（配置）中计算 中国建议售价,中国指导售价和中国最终售价
         * 2.如果税号从无 -》有，根据税号的税率重新计算 中国建议售价,中国指导售价和中国最终售价
         * 3.如果税号从有 -》有，现有逻辑不变，根据价格同步配置，重新计算 中国指导售价和中国最终售价（看配置）
         */
        Double taxRate;

        if (StringUtils.isEmpty(hsCode)) {
            //无税号时，税率取配置中的默认税率
            taxRate = feeTaxService.getDefaultTaxRate();
        } else {
            // 公式参数: 税率
            taxRate = feeTaxService.getTaxRate(hsCode, shippingTypeConfig.getConfigValue1());

            if (taxRate == null) {
                taxRate = feeTaxService.getDefaultTaxRate();
                $error(hsCode + " " + shippingTypeConfig.getConfigValue1() + " 没有找到税率");
            }
        }

        Double catCostRate = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_CATEGORY_COST);
        if (catCostRate == null || catCostRate.doubleValue() < 0) {
            catCostRate = 1D;
        }
        Double catCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_CATEGORY);
        if (catCommission == null) {
            catCommission = 0D;
        }

        // 获取中国指导售价/中国最终售价的价格设置
        CmsChannelConfigBean autoSyncPriceSaleConfig = getAutoSyncPriceSaleOption(channelId, cartId);

        // 进入计算阶段
        SystemPriceCalculator systemPriceCalculator = new SystemPriceCalculator()
                .setTaxRate(taxRate)
                .setPfCommission(platformCommission)
                .setReturnRate(returnRate)
                .setVoCommission(voyageOneCommission)
                .setExchangeRate(exchangeRate)
                .setOtherFee(otherFee)
                .setCatCostRate(catCostRate)
                .setCatCommission(catCommission);

        // 对设置到价格计算器上的参数
        // 在计算之前做一次检查
        if (!systemPriceCalculator.isValid())
            throw new PriceCalculateException("创建价格计算器失败. %s [ 以下是管理员查看 => %s, %s, %s, %s ]",
                    systemPriceCalculator.getErrorMessage(), channelId, platformId, cartId, catId);

        // 计算是否自动同步最终售价
        String isAutoApprovePrice = StringUtils.isEmpty(autoSyncPriceSaleConfig.getConfigValue1())
                ? (synSalePriceFlg ? "1" : "0")
                : autoSyncPriceSaleConfig.getConfigValue1();

        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
        List<BaseMongoMap<String, Object>> platformSkus = cart.getSkus();

        Map<String, CmsBtProductModel_Sku> commonSkuMap = commonSkus.stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        List<BaseMongoMap<String, Object>> unifySkus = new ArrayList<>();

        // 对 sku 进行匹配
        // 获取重量进行运费计算
        for (BaseMongoMap<String, Object> platformSku : platformSkus) {

            String skuCodeValue = platformSku.getStringAttribute(skuCode.name());

            // 自动联动
            if("1".equals(isAutoApprovePrice)){
                platformSku.setAttribute(isSale.name(),true);
            }

            if (!commonSkuMap.containsKey(skuCodeValue))
                continue;

            CmsBtProductModel_Sku commonSku = commonSkuMap.get(skuCodeValue);

            if (commonSku == null)
                continue;

            Double clientNetPrice = commonSku.getClientNetPrice();
            Double clientMsrp = commonSku.getClientMsrpPrice();

            // 获取sku的重量
            Double weight = commonSku.getWeight();
            if (weight == null || weight <= 0) {

                // 获取默认商品重量
                String weightString = shippingTypeConfig.getConfigValue2();
//
                if (StringUtils.isEmpty(weightString) || !StringUtils.isNumeric(weightString) || (weight = Double.valueOf(weightString)) <= 0)
                    throw new PriceCalculateException("没有为渠道 %s (%s) 的(SKU) %s 找到可用的商品重量", channelId, cartId, skuCodeValue);
            }
            weight = Double.sum(weight, Double.valueOf(shippingTypeConfig.getConfigValue3()));

            // 公式参数: 获取运费
            Double shippingFee = feeShippingService.getShippingFee(shippingTypeConfig.getConfigValue1(), weight);
            systemPriceCalculator.setShippingFee(shippingFee);

            // !! 最终价格计算 !!
            // 计算指导价(已包含处理小数点逻辑)
            Double retailPrice = roundDouble(systemPriceCalculator.calculate(clientNetPrice), autoSyncPriceSaleConfig.getConfigValue3());
            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, cartId, skuCodeValue, retailPrice);

            // 设置中国指导售价和中国指导确认售价
            setProductRetailPrice(platformSku, retailPrice, priceCalculatorConfig.getConfigValue2());

            // 计算原始中国建议售价(已包含处理小数点逻辑)
            Double originPriceMsrp = roundDouble(systemPriceCalculator.calculate(clientMsrp), autoSyncPriceMsrpConfig.getConfigValue3());

            if (originPriceMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, cartId, skuCodeValue, originPriceMsrp);

            // 设置原始中国建议售价
            platformSku.put(originalPriceMsrp.name(), originPriceMsrp);

            unifySkus.add(platformSku);
        }

        // 如果是Liking店铺做重复尺码check
        if(channelId.equals("928") || channelId.equals("024")) {
            repeatSizeChk(product, unifySkus, channelId);
        }

        // 设置中国最终售价
        unifySkuPriceSale(product, unifySkus, channelId, cartId, priceCalculatorConfig);
        // 设置中国建议售价
        unifySkuPriceMsrp(unifySkus, channelId, cartId);
    }

    /**
     * 使用固定公式计算价格, 并保存到商品模型上
     *
     * @param product         目标商品, 必须提供渠道、商品的 COMMON 信息等
     * @param cartId          目标平台ID, 如 23、27 等
     * @param synSalePriceFlg 是否同步指导价到最终售价
     * @param priceCalculatorConfig
     * @throws IllegalPriceConfigException 获取价格公式错误
     * @throws PriceCalculateException     价格计算错误
     */
    private void setPriceByFormula(CmsBtProductModel product, Integer cartId, boolean synSalePriceFlg, CmsChannelConfigBean priceCalculatorConfig) throws IllegalPriceConfigException, PriceCalculateException {

        String channelId = product.getChannelId();

        // 获取价格计算公式
        String msrpFormula = getPriceMsrpCalcFormulaOption(product, cartId);;
        String retailFormula = getPriceRetailCalcFormulaOption(product, cartId);

        // 获取价格处理逻辑规则
        CmsChannelConfigBean autoSyncPriceMsrp = getAutoSyncPriceMsrpOption(channelId, cartId);
        CmsChannelConfigBean autoSyncPriceSale = getAutoSyncPriceSaleOption(channelId, cartId);

        // 获取商品的 COMMON SKU 信息
        // 并转换为 MAP, 便于查找
        CmsBtProductModel_Common common = product.getCommon();
        List<CmsBtProductModel_Sku> skusInCommon = common.getSkus();
        Map<String, CmsBtProductModel_Sku> commonSkuMap = skusInCommon.stream().collect(toMap(CmsBtProductModel_Sku::getSkuCode, sku -> sku));

        // 计算是否自动同步最终售价
        String isAutoApprovePrice = StringUtils.isEmpty(autoSyncPriceSale.getConfigValue1())
                ? (synSalePriceFlg ? "1" : "0")
                : autoSyncPriceSale.getConfigValue1();

        // 去平台 SKU 信息
        // 遍历计算并保存价格
        CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);
        List<BaseMongoMap<String, Object>> skus = cart.getSkus();
        List<BaseMongoMap<String, Object>> unifySkus = new ArrayList<>();
        for (BaseMongoMap<String, Object> sku : skus) {

            String skuCodeValue = sku.getStringAttribute(skuCode.name());

            // 自动联动
            if("1".equals(isAutoApprovePrice)){
                sku.setAttribute(isSale.name(),true);
            }

            if (!commonSkuMap.containsKey(skuCodeValue))
                continue;

            CmsBtProductModel_Sku commonSku = commonSkuMap.get(skuCodeValue);
            if (commonSku == null)
                continue;

            // 计算指导价(已包含处理小数点逻辑)
            Double retailPrice = calculateByFormula(retailFormula, commonSku, autoSyncPriceSale.getConfigValue3());
            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, cartId, skuCodeValue, retailPrice);

            // 设置中国指导售价和中国指导确认售价
            setProductRetailPrice(sku, retailPrice, priceCalculatorConfig.getConfigValue2());

            // 计算原始中国建议售价(已包含处理小数点逻辑)
            Double originPriceMsrp = calculateByFormula(msrpFormula, commonSku, autoSyncPriceMsrp.getConfigValue3());
            if (originPriceMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, cartId, skuCodeValue, originPriceMsrp);

            // 设置原始中国建议售价
            sku.put(originalPriceMsrp.name(), originPriceMsrp);

            unifySkus.add(sku);
        }

        // 如果是Liking店铺做重复尺码check
        if(channelId.equals("928") || channelId.equals("024")) {
            repeatSizeChk(product, unifySkus, channelId);
        }

        // 设置中国最终售价
        unifySkuPriceSale(product, unifySkus, channelId, cartId, priceCalculatorConfig);
        // 设置中国建议售价
        unifySkuPriceMsrp(unifySkus, channelId, cartId);

    }

    /**
     * 通过SYSTEM的价格公式计算feed的价格
     * @param feed 产品信息
     * @param channelId  店铺Id
     * @throws PriceCalculateException
     * @throws IllegalPriceConfigException
     */
    private void setFeedPriceBySystem(CmsBtFeedInfoModel feed, String channelId) throws PriceCalculateException, IllegalPriceConfigException {

        // 公式参数: 其他费用
        final Double otherFee = 0.0d;

        // 公式参数: 汇率
        Double exchangeRate = feeExchangeService.getExchangeRateForUsd();

        Integer platformId = 2;

        // 计算是否计算 MSRP
        CmsChannelConfigBean autoSyncPriceMsrpConfig = getAutoSyncPriceMsrpOption(channelId, 0);

        // 计算发货方式

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean shippingTypeConfig = getShippingTypeOption(channelId, 0);

        CmsMtFeeCommissionService.CommissionQueryBuilder commissionQueryBuilder = feeCommissionService.new CommissionQueryBuilder()
                .withChannel(channelId)
                .withPlatform(platformId)
                .withCart(928)
                .withCategory(null);

        // 公式参数: 退货率
        Double returnRate = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_RETURN);

        // 公式参数: 公司佣金比例
        Double voyageOneCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_VOYAGE_ONE);

        // 公式参数: 平台佣金比例
        Double platformCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_PLATFORM);

        // 计算税号
        /*
         * 1.如果无税号，价格计算 按默认值11.9（配置）中计算 中国建议售价,中国指导售价和中国最终售价
         * 2.如果税号从无 -》有，根据税号的税率重新计算 中国建议售价,中国指导售价和中国最终售价
         * 3.如果税号从有 -》有，现有逻辑不变，根据价格同步配置，重新计算 中国指导售价和中国最终售价（看配置）
         */
        Double taxRate = feeTaxService.getDefaultTaxRate();

        Double catCostRate = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_CATEGORY_COST);
        if (catCostRate == null || catCostRate.doubleValue() < 0) {
            catCostRate = 1D;
        }
        Double catCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_CATEGORY);
        if (catCommission == null) {
            catCommission = 0D;
        }

        // 获取中国指导售价/中国最终售价的价格设置
        CmsChannelConfigBean autoSyncPriceSaleConfig = getAutoSyncPriceSaleOption(channelId, 0);

        // 进入计算阶段
        SystemPriceCalculator systemPriceCalculator = new SystemPriceCalculator()
                .setTaxRate(taxRate)
                .setPfCommission(platformCommission)
                .setReturnRate(returnRate)
                .setVoCommission(voyageOneCommission)
                .setExchangeRate(exchangeRate)
                .setOtherFee(otherFee)
                .setCatCostRate(catCostRate)
                .setCatCommission(catCommission);

        // 对设置到价格计算器上的参数
        // 在计算之前做一次检查
        if (!systemPriceCalculator.isValid())
            throw new PriceCalculateException("创建价格计算器失败. %s [ 以下是管理员查看 => %s, %s, %s, %s ]",
                    systemPriceCalculator.getErrorMessage(), channelId, platformId, 0, "");

        // 对 sku 进行匹配
        // 获取重量进行运费计算
        for (CmsBtFeedInfoModel_Sku feedSku : feed.getSkus()) {

            Double clientNetPrice = feedSku.getPriceNet();
            Double clientMsrp = feedSku.getPriceClientMsrp();

            // 获取sku的重量
            Double weight = Double.valueOf(feedSku.getWeightOrg());
            if (weight == null || weight <= 0) {

                // 获取默认商品重量
                String weightString = shippingTypeConfig.getConfigValue2();
//
                if (StringUtils.isEmpty(weightString) || !StringUtils.isNumeric(weightString) || (weight = Double.valueOf(weightString)) <= 0)
                    throw new PriceCalculateException("没有为渠道 %s (%s) 的(SKU) %s 找到可用的商品重量", channelId, 0, feedSku.getSku());
            }
            weight = Double.sum(weight, Double.valueOf(shippingTypeConfig.getConfigValue3()));

            // 公式参数: 获取运费
            Double shippingFee = feeShippingService.getShippingFee(shippingTypeConfig.getConfigValue1(), weight);
            systemPriceCalculator.setShippingFee(shippingFee);

            // !! 最终价格计算 !!
            // 计算指导价(已包含处理小数点逻辑)
            Double retailPrice = roundDouble(systemPriceCalculator.calculate(clientNetPrice), autoSyncPriceSaleConfig.getConfigValue3());
            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, 0, feedSku.getSku(), retailPrice);

            // 设置中国指导售价
            feedSku.setPriceCurrent(retailPrice);


            // 计算原始中国建议售价(已包含处理小数点逻辑)
            Double originPriceMsrp = roundDouble(systemPriceCalculator.calculate(clientMsrp), autoSyncPriceMsrpConfig.getConfigValue3());
            if (originPriceMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, 0, feedSku.getSku(), originPriceMsrp);

            // 设置原始中国建议售价
            feedSku.setPriceMsrp(originPriceMsrp);
        }
    }

    /**
     * 使用固定公式计算价格, 并保存到商品模型上
     *
     * @param feed         目标商品
     * @param channelId    价格计算公式渠道
     * @throws IllegalPriceConfigException 获取价格公式错误
     * @throws PriceCalculateException     价格计算错误
     */
    private void setFeedPriceByFormula(CmsBtFeedInfoModel feed, String channelId) throws IllegalPriceConfigException, PriceCalculateException {

        // 获取价格计算公式
        // 尝试获取类目级别的价格计算公式
        // 根据主类目获取价格公式
        String msrpFormula = getFeedPriceMsrpCalcFormulaOption(feed, channelId, 0);
        String retailFormula = getFeedPriceRetailCalcFormulaOption(feed, channelId, 0);

        // 获取价格处理逻辑规则
        CmsChannelConfigBean autoSyncPriceMsrp = getAutoSyncPriceMsrpOption(channelId, 0);
        CmsChannelConfigBean autoSyncPriceSale = getAutoSyncPriceSaleOption(channelId, 0);

        for (CmsBtFeedInfoModel_Sku sku : feed.getSkus()) {

            // 计算指导价(已包含处理小数点逻辑)
            Double retailPrice = calculateFeedByFormula(retailFormula, sku, autoSyncPriceSale.getConfigValue3());
            if (retailPrice <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的指导价不合法: %s", channelId, 0, sku.getSku(), retailPrice);

            // 设置中国指导售价和中国指导确认售价
            sku.setPriceCurrent(retailPrice);

            // 计算原始中国建议售价(已包含处理小数点逻辑)
            Double originPriceMsrp = calculateFeedByFormula(msrpFormula, sku, autoSyncPriceMsrp.getConfigValue3());
            if (originPriceMsrp <= 0)
                throw new PriceCalculateException("为渠道 %s (%s) 的(SKU) %s 计算出的 MSRP 不合法: %s", channelId, 0, sku.getSku(), originPriceMsrp);

            // 设置原始中国建议售价
            sku.setPriceMsrp(originPriceMsrp);
        }

    }

    /**
     * 使用公式为 sku 计算相应价格
     *
     * @param formula 价格计算公式, 从 {@code getCalculateFormula()} 获取
     * @param sku     包含公式参数的 sku 模型
     * @return 计算后的价格
     * @throws IllegalPriceConfigException sku 中不包含公式所需的参数
     */
    private Double calculateByFormula(String formula, CmsBtProductModel_Sku sku, String roundUp) throws IllegalPriceConfigException {

        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(formula);

        StandardEvaluationContext context = new StandardEvaluationContext(sku);

        try {
            BigDecimal price = expression.getValue(context, BigDecimal.class);

            return roundDouble(price.doubleValue(), roundUp);
        } catch (SpelEvaluationException sp) {
            throw new IllegalPriceConfigException("使用固定公式计算时出现错误", sp);
        }
    }

    /**
     * 使用公式为 sku 计算相应价格(Feed)
     *
     * @param formula 价格计算公式, 从 {@code getCalculateFormula()} 获取
     * @param sku     包含公式参数的 sku 模型
     * @return 计算后的价格
     * @throws IllegalPriceConfigException sku 中不包含公式所需的参数
     */
    private Double calculateFeedByFormula(String formula, CmsBtFeedInfoModel_Sku sku, String roundUp) throws IllegalPriceConfigException {

        Map<String, String> priceExchange = new HashMap<>();
        priceExchange.put("priceMsrp", "priceMsrp");
        priceExchange.put("priceRetail", "priceCurrent");
        priceExchange.put("clientMsrpPrice", "priceClientMsrp");
        priceExchange.put("clientRetailPrice", "priceClientRetail");
        priceExchange.put("clientNetPrice", "priceNet");
        for (Map.Entry<String, String> entry : priceExchange.entrySet()) {
            formula = formula.replaceAll(entry.getKey(), entry.getValue());
        }

        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression(formula);

        StandardEvaluationContext context = new StandardEvaluationContext(sku);

        try {
            BigDecimal price = expression.getValue(context, BigDecimal.class);

            return roundDouble(price.doubleValue(), roundUp);
        } catch (SpelEvaluationException sp) {
            throw new IllegalPriceConfigException("使用固定公式计算时出现错误", sp);
        }
    }

    /**
     * 设置中国最终售价和priceDiffFlg
     * @param product
     * @param unifySkus
     * @param channelId
     * @param cartId
     * @param priceCalculatorConfig
     */
    private void unifySkuPriceSale(CmsBtProductModel product, List<BaseMongoMap<String, Object>> unifySkus, String channelId, Integer cartId, CmsChannelConfigBean priceCalculatorConfig) {

        CmsChannelConfigBean autoSyncPriceSaleConfig = getAutoSyncPriceSaleOption(channelId, cartId);

        Double minRetail = null;
        Double maxRetail = null;

        for (BaseMongoMap<String, Object> platformSku : unifySkus) {
            Double retailPrice = platformSku.getDoubleAttribute(priceRetail.name());
            //计算Retail Price范围 只计算isSale = true
            if (platformSku.get(isSale.name()) != null && (Boolean) platformSku.get(isSale.name())) {
                if (minRetail == null || minRetail > retailPrice) {
                    minRetail = retailPrice;
                }
                if (maxRetail == null || maxRetail < retailPrice) {
                    maxRetail = retailPrice;
                }
            }
        }

        Integer configValue1 = 0;
        Integer configValue2 = 0;
        if (autoSyncPriceSaleConfig != null) {
            if (!StringUtils.isEmpty(autoSyncPriceSaleConfig.getConfigValue1())) {
                configValue1 = Integer.parseInt(autoSyncPriceSaleConfig.getConfigValue1());
            }
            if (!StringUtils.isEmpty(autoSyncPriceSaleConfig.getConfigValue2())) {
                configValue2 = Integer.parseInt(autoSyncPriceSaleConfig.getConfigValue2());
            }
        }

        Integer calculatorConfigValue3 = StringUtils.isEmpty(priceCalculatorConfig.getConfigValue3()) ? 0 : Integer.parseInt(priceCalculatorConfig.getConfigValue3());

        // 阀值
        CmsChannelConfigBean cmsChannelConfigBean = getMandatoryBreakThresholdOption(channelId, cartId);

        Double breakThreshold = Double.parseDouble(cmsChannelConfigBean.getConfigValue2()) / 100D;

        Map<String, String> goldSize = null;
        Double goldPrice = null;
        Map<String, CmsBtProductModel_Sku> skuinfo = new HashMap<>();

        // 取出sku的size 和 黄金尺码
        if (configValue2 == 3) {
            try {
                product.getCommonNotNull().getSkus().forEach(sku -> {
                    skuinfo.put(sku.getSkuCode(), sku);
                });
                goldSize = sxProductService.getSizeMap(channelId, product.getCommon().getFields().getBrand(), product.getCommon().getFields().getProductType(), product.getCommon().getFields().getSizeType(), true);
            } catch (BusinessException e) {
                $warn(e.getMessage());
            }
        }

        // 先处理 configValue1 = 1 和 =2 的场合
        for (BaseMongoMap<String, Object> skuInPlatform : unifySkus) {
            Double priceSale = skuInPlatform.getDoubleAttribute(Platform_SKU_COM.priceSale.name());
            Double retailPrice = skuInPlatform.getDoubleAttribute(priceRetail.name());

            // 中国最终售价 = 中国建议售价
            if (configValue1 == 1 || (configValue1 == 2 && retailPrice > priceSale)) {
                skuInPlatform.put(Platform_SKU_COM.priceSale.name(), retailPrice);
            }
            // 中国最终售价 = 按中国建议售价最高价统一
            if (configValue2 == 1 && maxRetail != null) {
                skuInPlatform.put(Platform_SKU_COM.priceSale.name(), maxRetail);
            }
            // 中国最终售价 = 按中国建议售价最低价统一
            else if (configValue2 == 2 && minRetail != null) {
                skuInPlatform.put(Platform_SKU_COM.priceSale.name(), minRetail);
            }
            // 算出黄金码最终售价最高价
            else if (configValue2 == 3 && goldSize != null) {
                if (skuInPlatform.get(isSale.name()) != null) {
                    CmsBtProductModel_Sku comSkuInfo = skuinfo.get(skuInPlatform.getStringAttribute(skuCode.name()));
                    if (comSkuInfo != null && comSkuInfo.getQty() > 0 && goldSize.containsKey(comSkuInfo.getSize())) {
                        if (goldPrice == null || goldPrice < skuInPlatform.getDoubleAttribute(Platform_SKU_COM.priceSale.name())) {
                            goldPrice = skuInPlatform.getDoubleAttribute(Platform_SKU_COM.priceSale.name());
                        }
                    }
                }
            }

            //
            if (priceSale <= 0 && configValue2 != 3)
                skuInPlatform.put(Platform_SKU_COM.priceSale.name(), retailPrice);

            // 保存击穿标识
            String priceDiffFlgValue = getPriceDiffFlg(channelId, skuInPlatform, cartId);
            // 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，向下击穿警告:5）
            skuInPlatform.put(priceDiffFlg.name(), priceDiffFlgValue);
        }

        if(goldPrice == null){
            goldPrice = maxRetail;
        }
        // 设置黄金码价格
        if (configValue2 == 3 && goldPrice != null) {
            for (BaseMongoMap<String, Object> skuInPlatform : unifySkus) {
                Double sale = skuInPlatform.getDoubleAttribute(priceSale.name());
                Double retailPrice = skuInPlatform.getDoubleAttribute(priceRetail.name());

                // 中国最终售价 = 按黄金码最终售价最高价统一
                if (sale < goldPrice) {
                    skuInPlatform.setAttribute(priceSale.name(), goldPrice);
                    sale = goldPrice;
                } else if (breakThreshold != null && sale > goldPrice && ((Double) (retailPrice * (1D - breakThreshold))).compareTo(goldPrice) != 1) {
                    skuInPlatform.setAttribute(priceSale.name(), goldPrice);
                    sale = goldPrice;
                } else if (breakThreshold != null && sale > goldPrice && ((Double) (retailPrice * (1D - breakThreshold))).compareTo(goldPrice) == 1) {
                    skuInPlatform.setAttribute(priceSale.name(), retailPrice);
                    skuInPlatform.setAttribute(isSale.name(), false);
                    sale = retailPrice;
                }

                // 设置商品不可售
                if (sale.compareTo(0.0) <= 0 && retailPrice.compareTo(0.0) <= 0) {
                    skuInPlatform.setAttribute(isSale.name(), false);
                } else if (retailPrice.compareTo(sale) > 0) {
                    if (calculatorConfigValue3 == 2 || (calculatorConfigValue3 == 1 && ((Double) (retailPrice * (1D - breakThreshold))).compareTo(sale) > 0)) {
                        skuInPlatform.setAttribute(isSale.name(), false);
                    }
                }

                //
                if (sale <= 0)
                    skuInPlatform.put(Platform_SKU_COM.priceSale.name(), retailPrice);

                // 保存击穿标识
                String priceDiffFlgValue = getPriceDiffFlg(channelId, skuInPlatform, cartId);
                // 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，向下击穿警告:5）
                skuInPlatform.put(priceDiffFlg.name(), priceDiffFlgValue);
            }
        }
    }

    /**
     * 根据Cart级Msrp统一配置设定msrp
     *
     * @param platformSkus
     * @param channelId
     * @param cartId
     * @throws IllegalPriceConfigException
     */
    public void unifySkuPriceMsrp(List<BaseMongoMap<String, Object>> platformSkus, String channelId, Integer cartId) throws IllegalPriceConfigException {
        CmsChannelConfigBean autoSyncPriceMsrpConfig = getAutoSyncPriceMsrpOption(channelId, cartId);
        String autoSyncPriceMsrp = autoSyncPriceMsrpConfig.getConfigValue1();

        List<Double> skuMsrpList = new ArrayList<>();
        List<Double> skuSaleList = new ArrayList<>();
        for (BaseMongoMap<String, Object> platformSku : platformSkus) {

            Double originalPriceMsrp = platformSku.getDoubleAttribute(Platform_SKU_COM.originalPriceMsrp.name());
            Double priceMsrp = platformSku.getDoubleAttribute(Platform_SKU_COM.priceMsrp.name()); // 当前中国建议售价
            Double priceSale = platformSku.getDoubleAttribute(Platform_SKU_COM.priceSale.name()); // 最新中国最终售价
            Double priceRetail = platformSku.getDoubleAttribute(Platform_SKU_COM.priceRetail.name()); // 最新中国指导售价

            // 直接联动
            if ("1".equals(autoSyncPriceMsrp)) {
                platformSku.put(Platform_SKU_COM.priceMsrp.name(), originalPriceMsrp);
            }
            // 自动联动
            else if ("2".equals(autoSyncPriceMsrp)) {
                // 当前中国建议售价<中国最终售价或当前中国建议售价<中国指导售价时，自动联动

                if (priceMsrp < priceSale || priceMsrp < priceRetail) {
                    double msrp = originalPriceMsrp;
                    if (msrp < priceSale) msrp = priceSale;
                    if (msrp < priceRetail) msrp = priceRetail;
                    platformSku.put(Platform_SKU_COM.priceMsrp.name(), msrp);
                }
            }
            else {
                // 如果不强制同步的话, 要看看是否原本是合法价格
                // 如果原本不是合法价格的话, 就同步设置
                resetPriceIfInvalid(platformSku, Platform_SKU_COM.priceMsrp, originalPriceMsrp);
            }

            skuMsrpList.add(platformSku.getDoubleAttribute(Platform_SKU_COM.priceMsrp.name()));
            skuSaleList.add(platformSku.getDoubleAttribute(Platform_SKU_COM.priceSale.name()));
        }

        Collections.sort(skuSaleList);
        Double maxSalePrice = Double.valueOf(skuSaleList.get(skuSaleList.size() - 1));
        if (skuMsrpList.size() > 0) {
            // 自然排序，从小到大

            Collections.sort(skuMsrpList);
            for (BaseMongoMap<String, Object> platformSku : platformSkus) {

                Double priceRetail = platformSku.getDoubleAttribute(Platform_SKU_COM.priceRetail.name()); // 最新中国指导售价

                // 中国建议售价 = 按中国建议售价最高价统一
                if ("1".equals(autoSyncPriceMsrpConfig.getConfigValue2())) {
                    platformSku.put(Platform_SKU_COM.priceMsrp.name(), Double.valueOf(skuMsrpList.get(skuMsrpList.size() - 1)));
                }
                // 中国建议售价 = 按中国建议售价最低价统一
                else if ("2".equals(autoSyncPriceMsrpConfig.getConfigValue2()) && skuMsrpList.get(0) >= maxSalePrice) {
                    platformSku.put(Platform_SKU_COM.priceMsrp.name(), Double.valueOf(skuMsrpList.get(0)));
                }
                // 中国建议售价 = max(priceSale)
                else if ("2".equals(autoSyncPriceMsrpConfig.getConfigValue2()) && skuMsrpList.get(0) < maxSalePrice) {
                    platformSku.put(Platform_SKU_COM.priceMsrp.name(), maxSalePrice);
                }

                // 获取
                platformSku.put(priceMsrpFlg.name(), compareRetailWithMsrpPrice(platformSku, Platform_SKU_COM.priceMsrp, priceRetail));
            }
        }
    }

    /**
     * 辅助方法: 获取商品的指定价格, 如果价格的值不合法, 就使用 {@code priceValue} 指定的值替换
     *
     * @param platformSku 平台 sku 模型
     * @param commonField 价格字段名
     * @param priceValue  商品原价格非法时使用的指定值
     */
    private void resetPriceIfInvalid(BaseMongoMap<String, Object> platformSku, Platform_SKU_COM commonField, Double priceValue) {
        Double _priceValue = getProductPrice(platformSku, commonField);
        if (_priceValue == null || _priceValue <= 0)
            platformSku.put(commonField.name(), priceValue);
    }

    /**
     * 比较中国指导售价和中国建议售价(中国建议售价 < 中国指导售价 : XU, 中国建议售价 > 中国知道售价 : XD, 相等 : 空字串)
     *
     * @param platformSku 平台 sku 模型
     * @param commonField 价格字段名
     * @param retailPrice 中国指导售价
     * @return 表示指导售价和建议售价的比较
     */
    private String compareRetailWithMsrpPrice(BaseMongoMap<String, Object> platformSku, Platform_SKU_COM commonField, Double retailPrice) {

        Double msrpPrice = getProductPrice(platformSku, commonField);

        if (msrpPrice == null)
            return "";

        if (msrpPrice < retailPrice)
            return "XU";
        else if (msrpPrice > retailPrice)
            return "XD";
        else
            return "";
    }

    /**
     * sku共同属性PriceDiffFlg计算方法(没有设置阀值时，则不与阀值比较)
     * 最终售价变化状态（价格为-1:空，等于指导价:1，比指导价低:2，比指导价高:3，向上击穿警告:4，向下击穿警告:5）
     *
     * @param breakThreshold 价格计算阀值
     * @param priceSale 中国最终售价
     * @param priceRetail 中国指导售价
     * @return 判断结果
     */
    private String getPriceDiffFlg(double breakThreshold, double priceSale, double priceRetail) {
        String diffFlg = "1";  // 最终售价与指导价相等
        // 如果价格计算有问题(-1)的时候，清空priceDiffFlg,防止高级检索画面查出来
        if (priceSale < 0.00d || priceRetail < 0.00d) {
            return "";
        }
        if (priceSale < priceRetail) {
            if (priceRetail * (1 - breakThreshold) <= priceSale) {
                diffFlg = "2"; // 最终售价比指导价低
            } else {
                diffFlg = "5"; // 最终售价向下击穿警告
            }
        }
        return diffFlg;
    }

    /**
     * 为商品设置 {@code retailPrice} 提供的指导价,
     * 如果 {@code isAutoApprovePrice} 为 {@code true},
     * 就同时设置到 {@code skuInPlatform} 的 {@code priceSale} 属性上。同时为商品这次的价格变动,
     * 更新波动标识 {@code priceChgFlg} 和击穿标识 {@code priceDiffFlg}
     *
     * @param skuInPlatform      商品的平台 sku 模型
     * @param retailPrice        计算出的人民币指导价
     * @param isAutoConfigPriceRetail 是否自动确认中国指导售价变化
     */
    private void setProductRetailPrice(BaseMongoMap<String, Object> skuInPlatform, Double retailPrice, String isAutoConfigPriceRetail) {

        // 设置中国指导确认售价
        if ("1".equals(isAutoConfigPriceRetail))
            skuInPlatform.put(confPriceRetail.name(), retailPrice);

        // 获取上一次确认指导价
        Double lastRetailPrice = getProductPrice(skuInPlatform, confPriceRetail);
        // 获取价格波动字符串
        String priceFluctuation = getPriceFluctuation(retailPrice, lastRetailPrice);
        // 保存价格波动
        skuInPlatform.put(priceChgFlg.name(), priceFluctuation);

        // 设置中国指导售价
        skuInPlatform.put(priceRetail.name(), retailPrice);
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
    private Double getProductPrice(BaseMongoMap<String, Object> platformSku, Platform_SKU_COM commonField) {

        Object value = platformSku.get(commonField.name());

        if (value == null)
            return null;

        return platformSku.getDoubleAttribute(commonField.name());
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
     * 找出尺码重复的sku 把有库存价格最低的 isSale=true 其他的设成false
     * @param product
     * @param unifySkus
     * @param channelId
     */
    private void repeatSizeChk(CmsBtProductModel product, List<BaseMongoMap<String, Object>> unifySkus, String channelId) {

        Map<String, String> goldSize = null;
        Map<String, CmsBtProductModel_Sku> skuinfo = new HashMap<>();
        try {
            product.getCommonNotNull().getSkus().forEach(sku -> {
                skuinfo.put(sku.getSkuCode(), sku);
            });
            goldSize = sxProductService.getSizeMap(channelId, product.getCommon().getFields().getBrand(), product.getCommon().getFields().getProductType(), product.getCommon().getFields().getSizeType());
        } catch (BusinessException e) {
            $warn(e.getMessage());
        }
        Map<String, List<BaseMongoMap<String, Object>>> sizeMap = new HashMap<>();
        // 先处理 configValue1 = 1 和 =2 的场合
        for (BaseMongoMap<String, Object> skuInPlatform : unifySkus) {
            CmsBtProductModel_Sku comSkuInfo = skuinfo.get(skuInPlatform.getStringAttribute(skuCode.name()));
            String size = (goldSize != null && goldSize.containsKey(comSkuInfo.getSize())) ? goldSize.get(comSkuInfo.getSize()) : comSkuInfo.getSize();
            if (!StringUtils.isEmpty(size)) {
                if (sizeMap.containsKey(size)) {
                    sizeMap.get(size).add(skuInPlatform);
                } else {
                    List<BaseMongoMap<String, Object>> temp = new ArrayList<>();
                    temp.add(skuInPlatform);
                    sizeMap.put(size, temp);
                }
            }
        }

        Map<String, Boolean> isSaleMap = new HashMap<>();
        sizeMap.forEach((s, baseMongoMaps) -> {
            if (baseMongoMaps.size() > 1) {
                //先找出有库存 价格最低的sku
                BaseMongoMap<String, Object> saleSku = baseMongoMaps.stream().filter(sku -> {
                    CmsBtProductModel_Sku comSkuInfo = skuinfo.get(sku.getStringAttribute(skuCode.name()));
                    return comSkuInfo.getQty() > 0;
                }).sorted((o1, o2) -> Double.compare(o1.getDoubleAttribute(priceRetail.name()),o2.getDoubleAttribute(priceRetail.name())))
                        .findFirst().orElse(null);
                //如果没有找到就那价格最低的sku
                if (saleSku == null) {
                    saleSku = baseMongoMaps.stream().sorted((o1, o2) -> Double.compare(o1.getDoubleAttribute(priceRetail.name()),o2.getDoubleAttribute(priceRetail.name()))).findFirst().orElse(null);
                }
                for (BaseMongoMap<String, Object> sku : baseMongoMaps) {
                    if (sku.getStringAttribute(skuCode.name()).equalsIgnoreCase(saleSku.getStringAttribute(skuCode.name()))) {
                        isSaleMap.put(sku.getStringAttribute(skuCode.name()), true);
                    } else {
                        isSaleMap.put(sku.getStringAttribute(skuCode.name()), false);
                    }
                }
                ;
            }
        });

        if (isSaleMap.size() > 0) {
            for (BaseMongoMap<String, Object> skuInPlatform : unifySkus) {
                Boolean isSale = isSaleMap.get(skuInPlatform.getStringAttribute(skuCode.name()));

                if (isSale != null) {
                    skuInPlatform.setAttribute(Platform_SKU_COM.isSale.name(), isSale);
                }
            }
        }

    }

    /**
     * 对输入数字取整或四舍五入
     *
     * @param input   输入的数字
     * @param roundUp 0:无特殊处理, 1:小数点向上取整, 2:个位向下取整, 3:个位向上取整
     * @return 取证或四舍五入后的结果
     */
    private Double roundDouble(Double input, String roundUp) {

        BigDecimal price = BigDecimal.valueOf(input);

        // 小数点向上取整
        if ("1".equals(roundUp)) {
            return price.setScale(0, BigDecimal.ROUND_CEILING).doubleValue();
        }
        // 个位向下取整
        else if ("2".equals(roundUp)) {
            // 个位向下取整
            BigDecimal multyValue = new BigDecimal("10");
            if (price.compareTo(multyValue) <= 0) {
                // 少于10的直接返回
                return price.setScale(2, BigDecimal.ROUND_CEILING).doubleValue();
            }

            return price.divide(multyValue).setScale(0, BigDecimal.ROUND_DOWN).multiply(multyValue).doubleValue();
        }
        // 个位向上取整
        else if ("3".equals(roundUp)) {
            // 个位向上取整
            BigDecimal multyValue = new BigDecimal("10");
            return price.divide(multyValue).setScale(1, BigDecimal.ROUND_UP).setScale(0, BigDecimal.ROUND_CEILING).multiply(multyValue).doubleValue();
        }
        // 无特殊处理
        else {
            return price.setScale(2, RoundingMode.UP).doubleValue();
        }
    }

    /**
     * 比较两个产品的价格和isSale有没有变化
     * @param product1 产品1
     * @param product2 产品2
     * @param cartId  渠道
     * @return 0：没有变化 1：isSale变化 2：价格变化  3：isSale和价格都有变化
     */
    private Integer skuCompare(CmsBtProductModel product1, CmsBtProductModel product2, Integer cartId){
        Integer ret = 0;
        if(cartId == null){
            return ret;
        }

        CmsBtProductModel_Platform_Cart platform1 = product1.getPlatform(cartId);
        CmsBtProductModel_Platform_Cart platform2 = product2.getPlatform(cartId);
        for(BaseMongoMap<String, Object> sku1:platform1.getSkus()){
            for(BaseMongoMap<String, Object> sku2:platform2.getSkus()){
                if(sku1.getStringAttribute(skuCode.name()).compareTo(sku2.getStringAttribute(skuCode.name())) == 0){
                    if(sku1.getAttribute(isSale.name()) != sku2.getAttribute(isSale.name())){
                        ret |= 1;
                    }

                    if (Double.compare(sku1.getDoubleAttribute(priceSale.name()), sku2.getDoubleAttribute(priceSale.name())) != 0) {
                        ret |= 2;
                    }
                    break;
                }
            }
        }
        return ret;
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

        private Double catCostRate;

        private Double catCommission;

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

        private SystemPriceCalculator setCatCostRate(Double catCostRate) {
            checkValid(this.catCostRate = catCostRate, "目录成本费率");
            return this;
        }

        private SystemPriceCalculator setCatCommission(Double catCommission) {
            checkValid(this.catCommission = catCommission, "目录佣金");
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
            double denominator = 100d - voCommission - pfCommission - returnRate - taxRate - catCommission;

            // 如果分母不合法。。。
            if (denominator == 0)
                throw new IllegalPriceConfigException("根据这些参数 [VoyageOne Commission:%s], [Platform Commission:%s], [Return Rate:%s], [Tax Rate:%s], [Category Commission:%s] 计算出公式的分母为 0"
                        , voCommission, pfCommission, returnRate, taxRate, catCommission, denominator);

            Double price = ((inputPrice * catCostRate + shippingFee + otherFee) * exchangeRate * 100d) / denominator;

            return price;
        }
    }

    /**
     * 获取价格公式
     * @param product 产品信息
     * @param formulaConfigs 价格公式列表
     * @return
     */
    private CmsChannelConfigBean getPriceConfig(CmsBtProductModel product, List<CmsChannelConfigBean> formulaConfigs) {

        // 查找主类目
        formulaConfigs.sort((o1, o2) -> o1.getConfigValue1().compareTo(o2.getConfigValue1()) * -1);
        CmsChannelConfigBean formulaConfig = null;
        for (CmsChannelConfigBean config : formulaConfigs) {
            if (!StringUtils.isEmpty(product.getCommon().getCatPathEn())
                    && product.getCommon().getCatPathEn().indexOf(config.getConfigValue1()) == 0) {
                formulaConfig = config;
                break;
            }
        }

        // 查找feed类目
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if (!StringUtils.isEmpty(product.getFeed().getCatPath())
                        && product.getFeed().getCatPath().indexOf(config.getConfigValue1()) == 0) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 查找feed类目
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if (!StringUtils.isEmpty(product.getCommon().getFields().getModel())
                        && product.getCommon().getFields().getModel().indexOf(config.getConfigValue1()) == 0) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 查找默认为0的
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if ("0".equals(config.getConfigValue1())) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 返回第一个
        if (formulaConfig == null && formulaConfigs.size() > 0) {
            formulaConfig = formulaConfigs.get(0);
        }

        return formulaConfig;
    }

    /**
     * 获取价格公式(Feed)
     * @param product 产品信息
     * @param formulaConfigs 价格公式列表
     * @return
     */
    private CmsChannelConfigBean getFeedPriceConfig(CmsBtFeedInfoModel product, List<CmsChannelConfigBean> formulaConfigs) {

        // 查找主类目
        formulaConfigs.sort((o1, o2) -> o1.getConfigValue1().compareTo(o2.getConfigValue1()) * -1);
        CmsChannelConfigBean formulaConfig = null;
        for (CmsChannelConfigBean config : formulaConfigs) {
            if (!StringUtils.isEmpty(product.getMainCategoryEn())
                    && product.getMainCategoryEn().indexOf(config.getConfigValue1()) == 0) {
                formulaConfig = config;
                break;
            }
        }

        // 查找feed类目
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if (!StringUtils.isEmpty(product.getCategory())
                        && product.getCategory().indexOf(config.getConfigValue1()) == 0) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 查找feed类目
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if (!StringUtils.isEmpty(product.getModel())
                        && product.getModel().indexOf(config.getConfigValue1()) == 0) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 查找默认为0的
        if (formulaConfig == null) {
            for (CmsChannelConfigBean config : formulaConfigs) {
                if ("0".equals(config.getConfigValue1())) {
                    formulaConfig = config;
                    break;
                }
            }
        }

        // 返回第一个
        if (formulaConfig == null && formulaConfigs.size() > 0) {
            formulaConfig = formulaConfigs.get(0);
        }

        return formulaConfig;
    }

}