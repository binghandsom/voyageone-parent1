package com.voyageone.service.impl.cms;

import com.google.common.base.Joiner;
import com.voyageone.base.dao.mongodb.model.BaseMongoMap;
import com.voyageone.base.dao.mysql.paginator.MySqlPageHelper;
import com.voyageone.base.exception.BusinessException;
import com.voyageone.common.CmsConstants;
import com.voyageone.common.configs.CmsChannelConfigs;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ethan Shi on 2016/7/13.
 *
 * @author Ethan Shi
 * @since 2.3.0
 */

@Service
public class PriceService extends BaseService {

    private static final Byte BY_WEIGHT = 0;
    private static final Byte BY_PC = 1;

    public static final String COMMISSION_TYPE_VO = "VO";
    public static final String COMMISSION_TYPE_PF = "PF";
    public static final String COMMISSION_TYPE_RT = "RT";

    private static int JM_CART = 27;

    @Autowired
    CmsMtFeeShippingDao cmsMtFeeShippingDao;

    @Autowired
    CmsMtFeeExchangeDao cmsMtFeeExchangeDao;

    @Autowired
    CmsMtFeeCommissionDao cmsMtFeeCommissionDao;

    @Autowired
    CmsMtFeeTaxDao cmsMtFeeTaxDao;



    /**
     * 计算product中各个sku的retailPrice
     * @param product
     * @param cartId
     * @return
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product, Integer cartId)
    {
        String channelId = product.getChannelId();
        Integer platformId = CartType.getPlatformIdById(cartId);
        String hsCode = product.getCommon().getFields().getHsCodePrivate();
        if(!StringUtils.isNullOrBlank2(hsCode))
        {
            hsCode = hsCode.split(",")[0];
        }

        CmsBtProductModel_Platform_Cart cart =  product.getPlatform(cartId);
        //产品级VO佣金比例
        Double productCommission = product.getCommon().getFields().getCommissionRate();
        //JM平台是按照品牌收取佣金
        String catId = cartId.intValue() == JM_CART ? cart.getpBrandId() : cart.getpCatId();

        //ShippingType存在cms_mt_channel_config里
        CmsChannelConfigBean cmsChannelConfigBean = CmsChannelConfigs.getConfigBean(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE, String.valueOf(cartId));

        if(cmsChannelConfigBean == null)
        {
            cmsChannelConfigBean = CmsChannelConfigs.getConfigBeanNoCode(channelId, CmsConstants.ChannelConfig.SHIPPING_TYPE);
        }

        String shippingType = cmsChannelConfigBean.getConfigValue1();


        Double exchangeRate = getExchangeRate("USD");
        Double voCommission =productCommission > 0 ? productCommission :  getVOCommission(channelId, platformId, cartId);
        Double pfCommission =getPFCommission(channelId, platformId, cartId, catId);
        Double returnRate = getReturn(channelId, platformId, cartId);
        Double taxRate = getTaxRate(shippingType, hsCode);
        Double otherFee = getOtherFee();


        List<CmsBtProductModel_Sku> commonSkus =  product.getCommon().getSkus();
        List<BaseMongoMap<String, Object>> platformSkus =  cart.getSkus();
        for(CmsBtProductModel_Sku commonSku: commonSkus)
        {
            Double clientNetPrice = commonSku.getClientNetPrice();
            Double weight = commonSku.getWeight();
            Double shippingFee = getShippingFee(shippingType, weight);

            Double retailPrice = getRetailPrice(clientNetPrice, shippingFee, exchangeRate, voCommission, pfCommission, returnRate, taxRate, otherFee);

            BaseMongoMap<String, Object> platformSku =  platformSkus.stream().filter(w -> commonSku.getSkuCode().equals(w.getStringAttribute("skuCode"))).findFirst().get();

            if(platformSku != null)
            {
                platformSku.setAttribute("priceRetail", retailPrice);
            }
        }
        return product;
    }

    /**
     * 批量计算product中各个sku的retailPrice,全部cart
     *
     * @param productList
     * @return
     */

    public List<CmsBtProductModel> setRetailPrice(List<CmsBtProductModel> productList)
    {
        for (CmsBtProductModel product : productList)
        {
            setRetailPrice(product);
        }

        return productList;
    }

    /**
     * 计算product中各个sku的retailPrice,全部cart
     *
     * @param product
     * @return
     */
    public CmsBtProductModel setRetailPrice(CmsBtProductModel product)
    {
        for (CartType cartType : CartType.values())
        {
            if (cartType.getPlatformId() != 0)
            {
                setRetailPrice(product, cartType.getCartId());
            }
        }
        return product;
    }

    /**
     * 批量计算product中各个sku的retailPrice
     *
     * @param productList
     * @param cartId
     * @return
     */
    public List<CmsBtProductModel> setRetailPrice(List<CmsBtProductModel> productList, Integer cartId)
    {
        for (CmsBtProductModel product : productList)
        {
            setRetailPrice(product,cartId);
        }

        return productList;
    }



    /**
     * 计算retailPrice
     *
     * @param clientNetPrice
     * @param shippingFee
     * @param exchangeRate
     * @param voCommission
     * @param pfCommission
     * @param returnRate
     * @param taxRate
     * @param otherFee
     * @return
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
            Double retailPrice = ((clientNetPrice + shippingFee + otherFee) * exchangeRate *100) / (100 - voCommission - pfCommission - returnRate - taxRate);
            return Math.ceil(retailPrice);
        } else {
            errorMsg = String.format("非法的价格参数[voCommission:%s], [pfCommission:%s], [returnRate:%s], [taxRate:%s]", voCommission, pfCommission, returnRate, taxRate);
            throw new BusinessException("", errorMsg);
        }
    }


    /**
     * 取运费
     *
     * @param shippingType
     * @param weight
     * @return
     */
    private Double getShippingFee(String shippingType, double weight) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("shippingType", shippingType);

        CmsMtFeeShippingModel shippingModel = cmsMtFeeShippingDao.selectOne(queryMap);

        if (shippingModel.getFeeType() == BY_WEIGHT) {
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
     *
     * @param currencyType
     * @return
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
     * 取其他费用
     *
     * @return
     */
    private Double getOtherFee() {
        //TODO
        return 0.0;
    }

    /**
     * 取VO佣金比例
     *
     * @param channelId
     * @param platformId
     * @param cartId
     * @return
     */
    private Double getVOCommission(String channelId, Integer platformId, Integer cartId) {
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


    /**
     * 取平台佣金比例
     *
     * @param platformId
     * @param cartId
     * @param catId
     * @return
     */
    private Double getPFCommission(String channelId, Integer platformId, Integer cartId, String catId) {


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


    /**
     * 取退货率
     *
     * @param platformId
     * @param cartId
     * @return
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
     * 取关税税率
     *
     * @param shippingType
     * @param hsCode
     * @return
     */
    private Double getTaxRate(String shippingType, String hsCode) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("shippingType", shippingType);
        queryMap.put("hsCode", hsCode);

        CmsMtFeeTaxModel cmsMtFeeTaxModel = cmsMtFeeTaxDao.selectOne(queryMap);
        if (cmsMtFeeTaxModel != null) {
            return cmsMtFeeTaxModel.getTaxRate();
        }

        return null;
    }


}
