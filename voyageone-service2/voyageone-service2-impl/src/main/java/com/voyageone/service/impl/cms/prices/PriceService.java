package com.voyageone.service.impl.cms.prices;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
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
import java.util.*;

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
    public PriceService(CmsMtFeeShippingService feeShippingService, CmsMtFeeTaxService feeTaxService, CmsMtFeeCommissionService feeCommissionService, CmsMtFeeExchangeService feeExchangeService, ProductSkuService productSkuService) {
        this.feeShippingService = feeShippingService;
        this.feeTaxService = feeTaxService;
        this.feeCommissionService = feeCommissionService;
        this.feeExchangeService = feeExchangeService;
        this.productSkuService = productSkuService;
    }

    /**
     * 计算product中各个sku的retailPrice, 全部cart
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product) {

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


    public CmsBtProductModel setRetailPrice(CmsBtProductModel product, Integer cartId) {

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

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean channelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE, String.valueOf(cartId));

        if (channelConfigBean == null) {
            channelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE);
        }

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

        // 发货方式
        String shippingType = channelConfigBean.getConfigValue1();

        CmsMtFeeCommissionService.CommissionQueryBuilder commissionQueryBuilder = feeCommissionService.new CommissionQueryBuilder()
                .withChannel(channelId)
                .withPlatform(platformId)
                .withCart(cartId)
                .withCategory(catId);

        // 公式参数: 退货率
        Double returnRate = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_RETURN);

        // 公式参数: 公司佣金比例
        Double defaultVoCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_VOYAGE_ONE);

        // 公式参数: 平台佣金比例
        Double platformCommission = commissionQueryBuilder.getCommission(CmsMtFeeCommissionService.COMMISSION_TYPE_PLATFORM);

        String hsCodeType = Codes.getCodeName(HSCODE_TYPE, shippingType);

        if (StringUtils.isEmpty(hsCodeType))
            throw new BusinessException("税号类型为空");

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
            throw new BusinessException(String.format("税号为空: hsCodeType: %s, product: %s", hsCodeType, code));

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
                .setVoCommission(defaultVoCommission)
                .setExchangeRate(exchangeRate)
                .setOtherFee(otherFee);

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

            // 公式参数: 获取运费
            Double shippingFee = feeShippingService.getShippingFee(shippingType, weight);

            // !! 最终价格计算 !!

            // 计算新的指导价
            Double retailPrice = priceCalculator.setShippingFee(shippingFee).calculate(clientNetPrice);

            if (retailPrice > 0) {
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
            } else {
                // 如果计算值不合法, 保存 -1 阻止上新
                setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceRetail, -1D);
            }

            Double originPriceMsrp = priceCalculator.calculate(clientMsrp);

            if (originPriceMsrp > 0) {

                if (isAutoSyncPriceMsrp)
                    setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceMsrp, originPriceMsrp);

                setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.originalPriceMsrp, originPriceMsrp);
            } else {
                setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.originalPriceMsrp, 0D);
            }

            // 最终检查计算器
            // 如果中间有错误, 就获取统一的错误信息, 进行记录
            if (!priceCalculator.isValid()) {
                // TODO
                priceCalculator.getErrorMessage();
            }
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
            return StringUtils.join(messageList, ";");
        }

        private boolean isValid() {
            return this.valid;
        }

        /**
         * 基于原始价格计算新价格
         *
         * @param inputPrice 原始价格
         * @return 新价格, 如果计算结果不合法 (0/null), 返回 -1
         */
        private Double calculate(Double inputPrice) {

            if (!valid)
                return -1d;

            // 计算公式分母
            double denominator = 100d - voCommission - pfCommission - returnRate - taxRate;

            // 如果分母不合法。。。
            if (denominator == 0) {
                messageList.add(String.format("非法的价格参数[voCommission:%s], [platformCommission:%s], [returnRate:%s], [taxRate:%s]", voCommission, pfCommission, returnRate, taxRate));
                valid = false;
                return -1D;
            }

            Double price = ((inputPrice + shippingFee + otherFee) * exchangeRate * 100d) / denominator;

            if (roundUp) {
                // 需要向上取整
                price = Math.ceil(price);
            } else {
                // 不需要, 就保留两位, 四舍五入
                price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
            }

            // 根据最终计算价格
            // 选择该返回啥
            return price > 0 ? price : -1D;
        }
    }
}
