package com.voyageone.service.impl.cms.prices;

import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductConstants;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public PriceService(CmsMtFeeShippingService feeShippingService, CmsMtFeeTaxService feeTaxService, CmsMtFeeCommissionService feeCommissionService, CmsMtFeeExchangeService feeExchangeService) {
        this.feeShippingService = feeShippingService;
        this.feeTaxService = feeTaxService;
        this.feeCommissionService = feeCommissionService;
        this.feeExchangeService = feeExchangeService;
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

            // 最终价格计算

            Double priceMsrp = priceCalculator.setShippingFee(shippingFee).calculate(clientMsrp);
            Double retailPrice = priceCalculator.setShippingFee(shippingFee).calculate(clientNetPrice);

            setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceMsrp, priceMsrp);
            setProductPrice(platformSku, CmsBtProductConstants.Platform_SKU_COM.priceRetail, retailPrice);

            if (!priceCalculator.isValid())
                // TODO
                priceCalculator.getErrorMessage();
        }

        return product;
    }

    private boolean isAutoSyncPriceMsrp() {
        
    }

    /**
     * 为商品赋值价格, 当价格值非法时, 赋值 -1, 强制阻断上新
     *
     * @param platformSku 商品的某 SKU 部分
     * @param commonField 商品价格字段
     * @param priceValue  具体价格
     */
    private void setProductPrice(BaseMongoMap<String, Object> platformSku, CmsBtProductConstants.Platform_SKU_COM commonField, Double priceValue) {
        platformSku.put(commonField.name(), (priceValue == null || priceValue < 1) ? -1d, priceValue);
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

        private List<String> messageList = new ArrayList<>();

        private void checkValid(Double inputFee, String title) {

            if (inputFee != null && inputFee > 0)
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

        private String getErrorMessage() {
            return StringUtils.join(messageList, ";");
        }

        private boolean isValid() {
            return this.valid;
        }

        private Double calculate(Double inputPrice) {

            if (!valid)
                return null;

            if ((100 - voCommission - pfCommission - returnRate - taxRate) > 0) {
                Double retailPrice = ((inputPrice + shippingFee + otherFee) * exchangeRate * 100) / (100 - voCommission - pfCommission - returnRate - taxRate);
                return Math.ceil(retailPrice);
            }

            messageList.add(String.format("非法的价格参数[voCommission:%s], [platformCommission:%s], [returnRate:%s], [taxRate:%s]", voCommission, pfCommission, returnRate, taxRate));

            valid = false;

            return null;
        }
    }
}
