package com.voyageone.service.impl.cms;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
import com.voyageone.common.configs.Codes;
import com.voyageone.common.configs.beans.CmsChannelConfigBean;
import com.voyageone.common.util.StringUtils;
import com.voyageone.service.dao.cms.CmsMtFeeCommissionDao;
import com.voyageone.service.dao.cms.CmsMtFeeExchangeDao;
import com.voyageone.service.dao.cms.CmsMtFeeShippingDao;
import com.voyageone.service.dao.cms.CmsMtFeeTaxDao;
import com.voyageone.service.impl.BaseService;
import com.voyageone.service.model.cms.CmsMtFeeCommissionModel;
import com.voyageone.service.model.cms.CmsMtFeeExchangeModel;
import com.voyageone.service.model.cms.CmsMtFeeShippingModel;
import com.voyageone.service.model.cms.CmsMtFeeTaxModel;
import com.voyageone.service.model.cms.enums.CartType;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Platform_Cart;
import com.voyageone.service.model.cms.mongo.product.CmsBtProductModel_Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Ethan Shi on 2016/7/13.
 *
 * @author Ethan Shi
 * @since 2.3.0
 */

@Service
public class PriceService extends BaseService {

    public static final String COMMISSION_TYPE_VO = "VO";
    public static final String COMMISSION_TYPE_PF = "PF";
    public static final String COMMISSION_TYPE_RT = "RT";

    private static final Byte BY_WEIGHT = 0;
    private static final Byte BY_PC = 1;
    private static int JM_CART = 27;

    public static final String HSCODE_TYPE_8_DIGIT = "8_DIGIT";
    public static final String HSCODE_TYPE_10_DIGIT = "10_DIGIT";
    public static final String HSCODE_TYPE = "HSCODE_TYPE";

    private final CmsMtFeeShippingDao cmsMtFeeShippingDao;

    private final CmsMtFeeExchangeDao cmsMtFeeExchangeDao;

    private final CmsMtFeeCommissionDao cmsMtFeeCommissionDao;

    private final CmsMtFeeTaxDao cmsMtFeeTaxDao;

    @Autowired
    public PriceService(CmsMtFeeTaxDao cmsMtFeeTaxDao, CmsMtFeeExchangeDao cmsMtFeeExchangeDao,
                        CmsMtFeeShippingDao cmsMtFeeShippingDao, CmsMtFeeCommissionDao cmsMtFeeCommissionDao) {
        this.cmsMtFeeTaxDao = cmsMtFeeTaxDao;
        this.cmsMtFeeExchangeDao = cmsMtFeeExchangeDao;
        this.cmsMtFeeShippingDao = cmsMtFeeShippingDao;
        this.cmsMtFeeCommissionDao = cmsMtFeeCommissionDao;
    }

    public CmsBtProductModel setRetailPrice(CmsBtProductModel product, Integer cartId) {
        Double exchangeRate = getExchangeRate("USD");
        String channelId = product.getChannelId();
        ChannelCartParams channelCartParams = new ChannelCartParams(cartId, channelId);
        ProductParams productParams = new ProductParams(product, cartId, channelCartParams.getShippingType());
        return setPlatformRetailPrice(product, cartId, exchangeRate, channelCartParams, productParams);
    }

    /**
     * 计算product中各个sku的retailPrice,全部cart
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product) {
        Double exchangeRate = getExchangeRate("USD");
        return setEveryPlatformRetailPriceWithExchange(product, exchangeRate);
    }

    /**
     * 批量计算product中各个sku的retailPrice
     */
    public List<CmsBtProductModel> setRetailPriceList(List<CmsBtProductModel> productList, Integer cartId) {
        for (CmsBtProductModel product : productList) {
            //为了减少数据库访问次数,尽量提前读取exchangeRate和相关参数
            String channelId = product.getChannelId();
            Double exchangeRate = getExchangeRate("USD");
            ChannelCartParams channelCartParams = new ChannelCartParams(cartId, channelId);

            ProductParams productParams = new ProductParams(product, cartId, channelCartParams.getShippingType());

            setPlatformRetailPrice(product, cartId, exchangeRate, channelCartParams, productParams);
        }

        return productList;
    }

    /**
     * 批量计算product中各个sku的retailPrice,全部cart
     */
    public List<CmsBtProductModel> setRetailPriceList(List<CmsBtProductModel> productList) {
        //为了减少数据库访问次数,尽量提前读取exchangeRate
        Double exchangeRate = getExchangeRate("USD");
        for (CmsBtProductModel product : productList) {
            setEveryPlatformRetailPriceWithExchange(product, exchangeRate);
        }
        return productList;
    }

    /**
     * 计算product中各个sku的retailPrice,全部cart
     */
    private CmsBtProductModel setEveryPlatformRetailPriceWithExchange(CmsBtProductModel product, Double exchangeRate) {

        String channelId = product.getChannelId();

        for (CartType cartType : CartType.values()) {

            if (cartType.getPlatformId().equals(0))
                continue;

            Integer cartId = cartType.getCartId();

            ChannelCartParams channelCartParams = new ChannelCartParams(cartId, channelId);

            ProductParams productParams = new ProductParams(product, cartId, channelCartParams.getShippingType());

            setPlatformRetailPrice(product, cartId, exchangeRate, channelCartParams, productParams);
        }

        return product;
    }

    /**
     * 计算product中各个sku的retailPrice
     */
    private CmsBtProductModel setPlatformRetailPrice(CmsBtProductModel product, Integer cartId, Double exchangeRate,
                                                     ChannelCartParams channelCartParams, ProductParams productParams) {

        String shippingType = channelCartParams.getShippingType();
        Double defaultVoCommission = channelCartParams.getDefaultVoCommission();
        Double returnRate = channelCartParams.getReturnRate();
        Double otherFee = channelCartParams.getOtherFee();

        Double pfCommission = productParams.getPlatformCommission();
        Double taxRate = productParams.getTaxRate();


        //产品级VO佣金比例
        Double productCommission = product.getCommon().getFields().getCommissionRate();
        Double voCommission = productCommission > 0 ? productCommission : defaultVoCommission;

        CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);
        List<CmsBtProductModel_Sku> commonSkus = product.getCommon().getSkus();
        List<BaseMongoMap<String, Object>> platformSkus = cart.getSkus();
        for (CmsBtProductModel_Sku commonSku : commonSkus) {
            Double clientNetPrice = commonSku.getClientNetPrice();
            Double weight = commonSku.getWeight();
            Double shippingFee = getShippingFee(shippingType, weight);

            Double retailPrice = getRetailPrice(clientNetPrice, shippingFee, exchangeRate, voCommission, pfCommission, returnRate, taxRate, otherFee);

            BaseMongoMap<String, Object> platformSku = platformSkus.stream().filter(w -> commonSku.getSkuCode().equals(w.getStringAttribute("skuCode"))).findFirst().orElse(null);

            if (platformSku != null) {
                platformSku.setAttribute("priceRetail", retailPrice);
            }
        }
        return product;
    }

    /**
     * 计算retailPrice
     */
    private Double getRetailPrice(Double clientNetPrice, Double shippingFee, Double exchangeRate,
                                  Double voCommission, Double pfCommission, Double returnRate, Double taxRate, Double otherFee) {
        List<String> msgs = new ArrayList<>();

        if (shippingFee == null || shippingFee == 0) {
            msgs.add("读取shippingFee出错");
        }
        if (exchangeRate == null || exchangeRate == 0) {
            msgs.add("读取exchangeRate出错");
        }
        if (voCommission == null || voCommission == 0) {
            msgs.add("读取voCommission出错");
        }
        if (pfCommission == null || pfCommission == 0) {
            msgs.add("读取pfCommission出错");
        }
        if (returnRate == null || returnRate == 0) {
            msgs.add("读取returnRate出错");
        }
        if (taxRate == null || taxRate == 0) {
            msgs.add("读取taxRate出错");
        }
        if (otherFee == null) {
            msgs.add("读取otherFee出错");
        }

        String errorMsg = Joiner.on(";").join(msgs);
        if (!StringUtils.isNullOrBlank2(errorMsg)) {
            errorMsg += "!";
            throw new BusinessException("", errorMsg);
        }

        if ((100 - voCommission - pfCommission - returnRate - taxRate) > 0) {
            Double retailPrice = ((clientNetPrice + shippingFee + otherFee) * exchangeRate * 100) / (100 - voCommission - pfCommission - returnRate - taxRate);
            return Math.ceil(retailPrice);
        } else {
            errorMsg = String.format("非法的价格参数[voCommission:%s], [platformCommission:%s], [returnRate:%s], [taxRate:%s]", voCommission, pfCommission, returnRate, taxRate);
            throw new BusinessException("", errorMsg);
        }
    }

    /**
     * 取运费
     */
    private Double getShippingFee(String shippingType, double weight) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("shippingType", shippingType);

        CmsMtFeeShippingModel shippingModel = cmsMtFeeShippingDao.selectOne(queryMap);

        if (BY_WEIGHT.equals(shippingModel.getFeeType())) {
            int firstWeight = shippingModel.getFirstWeight();
            double firstWeightFee = shippingModel.getFirstWeightFee();
            int additionalWeight = shippingModel.getAdditionalWeight();
            double additionalWeightFee = shippingModel.getAdditionalWeightFee();

            if (weight <= firstWeight) {
                return firstWeightFee;
            } else {
                return firstWeightFee + Math.ceil((weight - firstWeight) / additionalWeight) * additionalWeightFee;
            }
        } else {
            return shippingModel.getPcFee();
        }
    }

    /**
     * 取汇率
     */
    private Double getExchangeRate(String currencyType) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("currencyType", currencyType);

        String sortString = "modified DESC";
        Map<String, Object> map = MySqlPageHelper.build(queryMap).sort(sortString).toMap();

        CmsMtFeeExchangeModel cmsMtFeeExchangeModel = cmsMtFeeExchangeDao.selectOne(map);
        return cmsMtFeeExchangeModel.getExchangeRate();
    }

    /**
     * 用于计算并获取渠道在相应平台的退货率、其他费用、公司佣金比例的帮助类
     */
    private class ChannelCartParams {

        private Integer cartId;
        private String channelId;
        private String shippingType;
        private Double returnRate;
        private Double otherFee;
        private Double defaultVoCommission;

        ChannelCartParams(Integer cartId, String channelId) {
            this.cartId = cartId;
            this.channelId = channelId;

            caleReturnRateAndOtherFeeAndCommission();
        }

        String getShippingType() {
            return shippingType;
        }

        Double getReturnRate() {
            return returnRate;
        }

        Double getOtherFee() {
            return otherFee;
        }

        Double getDefaultVoCommission() {
            return defaultVoCommission;
        }

        void caleReturnRateAndOtherFeeAndCommission() {

            Integer platformId = CartType.getPlatformIdById(cartId);
            //ShippingType存在cms_mt_channel_config里
            CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE, String.valueOf(cartId));

            if (cmsChannelConfigBean == null) {
                cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE);
            }

            shippingType = cmsChannelConfigBean.getConfigValue1();

            returnRate = getReturn(channelId, platformId, cartId);
            otherFee = getOtherFee(channelId);
            defaultVoCommission = getVoyageOneCommission(channelId, platformId, cartId);
        }

        /**
         * 取退货率
         *
         * @param platformId 平台(非 cart, 如 taobao / tmall / tmall global 同属 ali 系)
         * @param cartId 平台
         * @return 退货率
         */
        private Double getReturn(String channelId, Integer platformId, Integer cartId) {

            //店铺级
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("commissionType", COMMISSION_TYPE_RT);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);
            queryMap.put("cartId", cartId);


            CmsMtFeeCommissionModel cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //渠道平台级
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_RT);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);

            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //渠道级默认值
            queryMap.clear();
            queryMap.put("channelId", channelId);
            queryMap.put("commissionType", COMMISSION_TYPE_RT);


            //系统级默认值
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_RT);

            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            return null;
        }

        /**
         * 取其他费用
         */
        private Double getOtherFee(String channelId) {
            // 这里暂时不会有其他费用
            // 所以默认直接返回 0, 暂时不实现具体内容
            return 0.0;
        }

        /**
         * 取VO佣金比例
         *
         * @param channelId 渠道
         * @param platformId 平台(非 cart, 如 taobao / tmall / tmall global 同属 ali 系)
         * @param cartId 平台
         * @return 佣金比例
         */
        private Double getVoyageOneCommission(String channelId, Integer platformId, Integer cartId) {

            //店铺级
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("commissionType", COMMISSION_TYPE_VO);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);
            queryMap.put("cartId", cartId);

            CmsMtFeeCommissionModel cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //渠道平台级
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_VO);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);
            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //渠道级默认值
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_VO);
            queryMap.put("channelId", channelId);
            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //渠道级默认值
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_VO);
            queryMap.put("platformId", platformId);
            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //系统级默认值
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_VO);
            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);
            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            return null;
        }
    }

    /**
     * 用于计算并获取商品在相应平台的关税税率、平台佣金比例的帮助类
     */
    private class ProductParams {

        private CmsBtProductModel product;
        private Integer cartId;
        private String shippingType;
        private Double taxRate;
        private Double platformCommission;

        ProductParams(CmsBtProductModel product, Integer cartId, String shippingType) {

            this.product = product;
            this.cartId = cartId;
            this.shippingType = shippingType;

            caleTaxRateAndCommission();
        }

        Double getTaxRate() {
            return taxRate;
        }

        Double getPlatformCommission() {
            return platformCommission;
        }

        private void caleTaxRateAndCommission() {

            String channelId = product.getChannelId();

            String hsCodeType = Codes.getCodeName(HSCODE_TYPE, shippingType);

            String hsCode;

            if (HSCODE_TYPE_8_DIGIT.equals(hsCodeType)) {
                hsCode = product.getCommon().getFields().getHsCodePrivate();
            } else {
                hsCode = product.getCommon().getFields().getHsCodeCross();
            }

            if (!StringUtils.isNullOrBlank2(hsCode)) {
                hsCode = hsCode.split(",")[0];
            }

            // 计算税率
            taxRate = getTaxRate(shippingType, hsCode);

            CmsBtProductModel_Platform_Cart cart = product.getPlatform(cartId);

            //JM平台是按照品牌收取佣金
            String catId = cartId == JM_CART ? cart.getpBrandId() : cart.getpCatId();

            Integer platformId = CartType.getPlatformIdById(cartId);

            // 计算平台佣金比例
            platformCommission = getPlatformCommission(channelId, platformId, cartId, catId);
        }

        /**
         * 取关税税率
         *
         * @param shippingType 发货方式
         * @param hsCode       税号
         * @return 关税税率
         */
        private Double getTaxRate(String shippingType, String hsCode) {

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("shippingType", shippingType);
            queryMap.put("hsCode", hsCode);

            CmsMtFeeTaxModel cmsMtFeeTaxModel = cmsMtFeeTaxDao.selectOne(queryMap);

            if (cmsMtFeeTaxModel != null) {
                return cmsMtFeeTaxModel.getVaTaxRate() + cmsMtFeeTaxModel.getConsumptionTaxRate();
            }

            return null;
        }

        /**
         * 取平台佣金比例
         *
         * @param channelId  渠道
         * @param platformId 平台(非 cart, 如 taobao / tmall / tmall global 同属 ali 系)
         * @param cartId     平台
         * @param catId      类目 ID
         * @return 可能为空的佣金比例
         */
        private Double getPlatformCommission(String channelId, Integer platformId, Integer cartId, String catId) {

            //店铺分类级
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("commissionType", COMMISSION_TYPE_PF);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);
            queryMap.put("cartId", cartId);
            queryMap.put("catId", catId);

            CmsMtFeeCommissionModel cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //平台分类级
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_PF);
            queryMap.put("platformId", platformId);
            queryMap.put("catId", catId);

            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //店铺级默认,不分分类
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_PF);
            queryMap.put("channelId", channelId);
            queryMap.put("platformId", platformId);
            queryMap.put("cartId", cartId);

            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //平台级默认,不分分类
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_PF);
            queryMap.put("platformId", platformId);

            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);
            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            //系统级默认,不分分类
            queryMap.clear();
            queryMap.put("commissionType", COMMISSION_TYPE_PF);
            cmsMtFeeCommissionModel = cmsMtFeeCommissionDao.selectOne(queryMap);

            if (cmsMtFeeCommissionModel != null) {
                return cmsMtFeeCommissionModel.getCommissonRate();
            }

            return null;
        }
    }
}
